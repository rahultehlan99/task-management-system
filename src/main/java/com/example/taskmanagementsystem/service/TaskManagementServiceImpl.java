package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.TaskCreateRequestDTO;
import com.example.taskmanagementsystem.dto.TaskCreateResponseDTO;
import com.example.taskmanagementsystem.dto.TaskInfoDTO;
import com.example.taskmanagementsystem.entity.Tags;
import com.example.taskmanagementsystem.entity.Tasks;
import com.example.taskmanagementsystem.enums.TaskStatus;
import com.example.taskmanagementsystem.factory.FileStoreServiceFactory;
import com.example.taskmanagementsystem.mapper.Mappers;
import com.example.taskmanagementsystem.repository.TagsRepository;
import com.example.taskmanagementsystem.repository.TasksRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {

    private static final String uploadLocation = "S3";
    private final TasksRepository tasksRepository;
    private final TagsRepository tagsRepository;
    private final FileStoreServiceFactory fileStoreServiceFactory;
    private FileStoreService fileStoreService;

    @Override
    public TaskCreateResponseDTO createNewUserTask(TaskCreateRequestDTO taskCreateRequestDTO) {
        log.info("Trying to create new task");
        Tasks createdTasks = Mappers.taskRequestToTaskEntityMapper(taskCreateRequestDTO);
        createdTasks.setTags(tagsRawToEntity(taskCreateRequestDTO.getTags()));
        log.info("Saving new task in db");
        createdTasks = tasksRepository.save(createdTasks);
        log.info("New task saved in db");
        return Mappers.requestToResponseDTO(taskCreateRequestDTO, createdTasks);
    }

    @Override
    public void uploadTaskFiles(String taskId, List<MultipartFile> multipartFiles) {
        List<Resource> inputResources = new ArrayList<>();
        multipartFiles.forEach(multipartFile -> inputResources.add(multipartFile.getResource()));
        Optional<Tasks> optionalTasks = tasksRepository.findTaskByTaskId(taskId);
        if (!optionalTasks.isPresent()) {
            throw new RuntimeException(String.format("No task with given id : %s", taskId));
        }
        this.fileStoreService = fileStoreServiceFactory.getFileStoreService(uploadLocation);
        List<String> uploadFileNames = fileStoreService.upload(inputResources);
        log.info("Files uploaded {}", uploadFileNames);
        Tasks task = optionalTasks.get();
        task.setFiles(uploadFileNames);
        tasksRepository.save(task);
        log.info("Saved task with images : {}", taskId);
    }

    @Override
    public List<Resource> downloadTaskFiles(String taskId) {
        Optional<Tasks> optionalTasks = tasksRepository.findTaskByTaskId(taskId);
        if (!optionalTasks.isPresent()) {
            throw new RuntimeException(String.format("No task with given id : %s", taskId));
        }
        return fileStoreService.download(optionalTasks.get().getFiles());
    }

    @Override
    public List<TaskInfoDTO> getTasksViaTags(List<String> tags, int count) {
        List<TaskInfoDTO> tasks = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, count);
        List<Tasks> userTasks = tasksRepository.findAllByTags(tags, pageable);
        userTasks.forEach(task -> tasks.add(Mappers.taskEntityToTaskInfoDTO(task)));
        log.info("Task data found : {}", tasks);
        return tasks;
    }

    @Override
    @Cacheable(cacheNames = "taskInfo", key = "#taskId", condition = "#taskId!=null")
    public List<TaskInfoDTO> getTask(String taskId) {
        log.info("Getting task info via id : {}", taskId);
        if (StringUtils.isBlank(taskId)) {
            List<TaskInfoDTO> tasksInfoList = new ArrayList<>();
            tasksRepository.findAll().forEach(task -> {
                tasksInfoList.add(Mappers.taskEntityToTaskInfoDTO(task));
            });
            return tasksInfoList;
        }
        long startTime = System.currentTimeMillis();
        Optional<Tasks> optionalTasks = tasksRepository.findTaskByTaskId(taskId);
        if (!optionalTasks.isPresent()) {
            throw new RuntimeException(String.format("No task with given id : %s", taskId));
        }
        log.info("Time taken is : {}", System.currentTimeMillis() - startTime);
        return Collections.singletonList(Mappers.taskEntityToTaskInfoDTO(optionalTasks.get()));
    }

    @Override
    @CachePut(cacheNames = "taskInfo")
    public String changeTaskStatus(String taskId, String newStatus) {
        log.info("Request received for changing task status for {} to {}", taskId, newStatus);
        if (TaskStatus.checkIfExists(newStatus)) {
            Optional<Tasks> optionalTasks = tasksRepository.findTaskByTaskId(taskId);
            if (!optionalTasks.isPresent()) {
                throw new RuntimeException(String.format("No task with given id : %s", taskId));
            }
            Tasks updatedTask = optionalTasks.get();
            updatedTask.setStatus(TaskStatus.valueOf(newStatus));
            tasksRepository.save(updatedTask);
        } else {
            log.info("{} is not an allowed status for the task", newStatus);
            return String.format("%s is not an allowed status for the task", newStatus);
        }
        return String.format("OK, task status for %s changed to %s", taskId, newStatus);
    }

    @Override
    @CacheEvict(cacheNames = "taskInfo")
    public String deleteTask(String taskId) {
        log.info("Request received for deleting task {}", taskId);
        Optional<Tasks> optionalTasks = tasksRepository.findTaskByTaskId(taskId);
        if (!optionalTasks.isPresent()) {
            throw new RuntimeException(String.format("No task with given id : %s", taskId));
        }
        Tasks updatedTask = optionalTasks.get();
        tasksRepository.delete(updatedTask);
        return String.format("OK, task deleted : %s", taskId);
    }

    private Set<Tags> tagsRawToEntity(Set<String> tags) {
        Set<Tags> result = new HashSet<>();
        Set<Tags> tagsToPersist = new HashSet<>();
        tags.forEach(tag -> {
            Optional<Tags> tagFromDBOptional = tagsRepository.findByTagName(tag);
            if (tagFromDBOptional.isPresent()) {
                result.add(tagFromDBOptional.get());
            } else {
                Tags newTag = new Tags();
                newTag.setTagName(tag);
                tagsToPersist.add(newTag);
                result.add(newTag);
            }
        });
        try {
            tagsRepository.saveAll(tagsToPersist);
        } catch (Exception e) {
            log.info("Exception occurred while saving new tags in db : ", e);
        }
        return result;
    }
}
