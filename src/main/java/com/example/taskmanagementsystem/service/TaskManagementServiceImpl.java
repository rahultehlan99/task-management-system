package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.*;
import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Tags;
import com.example.taskmanagementsystem.entity.Tasks;
import com.example.taskmanagementsystem.entity.Users;
import com.example.taskmanagementsystem.enums.TaskStatus;
import com.example.taskmanagementsystem.factory.FileStoreServiceFactory;
import com.example.taskmanagementsystem.mapper.Mappers;
import com.example.taskmanagementsystem.repository.TagsRepository;
import com.example.taskmanagementsystem.repository.TasksRepository;
import com.example.taskmanagementsystem.repository.UsersRepository;
import com.example.taskmanagementsystem.utils.LoggedUserInfo;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {

    private static final String uploadLocation = "S3";
    private final TasksRepository tasksRepository;
    private final TagsRepository tagsRepository;
    private final FileStoreServiceFactory fileStoreServiceFactory;
    private final UsersRepository usersRepository;
    private final MailService mailService;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public TaskCreateResponseDTO createNewUserTask(TaskCreateRequestDTO taskCreateRequestDTO) {
        log.info("Trying to create new task");
        Tasks createdTasks = Mappers.taskRequestToTaskEntityMapper(taskCreateRequestDTO);
        createdTasks.setTags(tagsRawToEntity(taskCreateRequestDTO.getTags()));
        createdTasks.setUsers(userForNewTask());
        createdTasks = tasksRepository.save(createdTasks);
        log.info("New task saved by thread : {}", Thread.currentThread().getName());
        mailService.sendMail("Task created", "rtehlan01@gmail.com");
        return Mappers.requestToResponseDTO(taskCreateRequestDTO, createdTasks);
    }

    private Users userForNewTask() {
        String loggedInUser = LoggedUserInfo.getCurrentLoggedInUser();
        return usersRepository.findByUserName(loggedInUser);
    }

    private Tasks getTaskById(String taskId) {
        return tasksRepository.findTaskByTaskId(taskId);
    }

    @Override
    public void uploadTaskFiles(String taskId, List<MultipartFile> multipartFiles) {
        Tasks task = getTaskById(taskId);
        FileStoreService fileStore = fileStoreServiceFactory.getFileStoreService(uploadLocation);
        List<String> uploadFileNames = fileStore.upload(multipartFiles);
        log.info("Files uploaded {}", uploadFileNames);
        uploadFileNames.forEach(task::addFileToTask);
        tasksRepository.save(task);
        log.info("Saved task with images : {}", taskId);
    }

    @Override
    public List<String> getTaskFilesPreSignedUrl(String taskId) {
        Tasks task = getTaskById(taskId);
        FileStoreService fileStore = fileStoreServiceFactory.getFileStoreService(uploadLocation);
        return fileStore.getObjectPreSignedUrl(task.getFiles());
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
    public List<TaskInfoDTO> getTasks(String taskId, int pageNo, int pageSize, String sortBy, String sortingDirection) {
        String loggedUser = LoggedUserInfo.getCurrentLoggedInUser();
        log.info("Getting task info via id : {} for user : {}", taskId, loggedUser);
        if (StringUtils.isBlank(taskId)) {
            List<TaskInfoDTO> tasksInfoList = new ArrayList<>();
            Sort sort = Sort.by(Sort.Direction.fromString(sortingDirection), sortBy);
            long userId = usersRepository.findByUserName(loggedUser).getUserId();
            tasksRepository.findAllTasksOrderByUpdatedAtDesc(userId, PageRequest.of(pageNo, pageSize, sort)).forEach(task -> {
                tasksInfoList.add(Mappers.taskEntityToTaskInfoDTO(task));
            });
            return tasksInfoList;
        }
        long startTime = System.currentTimeMillis();
        Tasks task = getTaskById(taskId);
        log.info("Time taken is : {}", System.currentTimeMillis() - startTime);
        return Collections.singletonList(Mappers.taskEntityToTaskInfoDTO(task));
    }

    @Override
    public List<TaskInfoDTO> getFilteredTasks(GetBulkTasksRequestDTO tasksRequestDTO) {
        StringBuilder query = new StringBuilder("Select * from tasks t where 1=1 ");
        if (tasksRequestDTO.getDeadline() != null) {
            query.append(String.format(" AND dead_Line <= %s", "'" + tasksRequestDTO.getDeadline()) + "'");
        }
        if (tasksRequestDTO.getPriority() != null) {
            query.append(String.format(" AND priority <= %s", tasksRequestDTO.getPriority()));
        }
        if (!CollectionUtils.isEmpty(tasksRequestDTO.getStatus())) {
            query.append(String.format(" AND status in (%s)", tasksRequestDTO.getStatus().stream().map(TaskStatus::valueOf).map(status -> "'" + status + "'").collect(Collectors.joining(","))));
        }
        List<Tasks> tasks = jdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<>(Tasks.class));
        return tasks.stream().map(Mappers::taskEntityToFilteredTaskInfoDTO).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDTO> getComments(String taskId) {
        Tasks tasks = getTaskById(taskId);
        List<CommentResponseDTO> commentResponseDTOS = new ArrayList<>();
        tasks.getComments().forEach(comment -> {
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
            commentResponseDTO.setCommentId(comment.getCommentId());
            commentResponseDTO.setDescription(comment.getCommentDescription());
            commentResponseDTO.setUserId(comment.getUserId());
            commentResponseDTOS.add(commentResponseDTO);
        });
        return commentResponseDTOS;
    }

    @Override
    public String changeTaskStatus(String taskId, String newStatus) {
        log.info("Request received for changing task status for {} to {}", taskId, newStatus);
        if (TaskStatus.checkIfExists(newStatus)) {
            Tasks updatedTask = getTaskById(taskId);
            updatedTask.setStatus(TaskStatus.valueOf(newStatus));
            tasksRepository.save(updatedTask);
            return String.format("OK, task status for %s changed to %s", updatedTask.getTaskName(), newStatus);
        } else {
            log.info("{} is not an allowed status for the task", newStatus);
            return String.format("%s is not an allowed status for the task", newStatus);
        }
    }

    @Override
    public String updateTask(String taskId, TaskUpdateRequestDTO taskUpdateRequestDTO) {
        Tasks task = getTaskById(taskId);
        if (taskUpdateRequestDTO.getTaskName() != null)
            task.setTaskName(taskUpdateRequestDTO.getTaskName());
        if (taskUpdateRequestDTO.getTaskDescription() != null)
            task.setTaskDescription(taskUpdateRequestDTO.getTaskDescription());
        if (taskUpdateRequestDTO.getPriority() != null)
            task.setPriority(task.getPriority());
        if (taskUpdateRequestDTO.getDeadLine() != null)
            task.setDeadLine(taskUpdateRequestDTO.getDeadLine());
        tasksRepository.save(task);
        return String.format("OK, task %s updated successfully", taskId);
    }

    @Override
    public String addComment(CommentRequestDTO commentRequestDTO) {
        Tasks tasks = getTaskById(commentRequestDTO.getTaskId());
        Comment comment = new Comment();
        comment.setCommentDescription(commentRequestDTO.getDescription());
        comment.setUserId(LoggedUserInfo.getCurrentLoggedInUser());
        tasks.addComment(comment);
        tasksRepository.save(tasks);
        return "OK, comment added";
    }

    @Override
    public String revertReminder(String taskId) {
        Tasks task = getTaskById(taskId);
        task.setReminderEnabled(!task.isReminderEnabled());
        tasksRepository.save(task);
        return String.format("OK, reminder is %s now", task.isReminderEnabled());
    }

    @Override
    @CacheEvict(cacheNames = "taskInfo", key = "#taskId")
    public String deleteTask(String taskId) {
        log.info("Request received for deleting task {}", taskId);
        Tasks task = getTaskById(taskId);
        tasksRepository.delete(task);
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
