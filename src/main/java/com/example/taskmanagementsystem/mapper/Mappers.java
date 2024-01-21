package com.example.taskmanagementsystem.mapper;

import com.example.taskmanagementsystem.dto.TaskCreateRequestDTO;
import com.example.taskmanagementsystem.dto.TaskCreateResponseDTO;
import com.example.taskmanagementsystem.dto.TaskInfoDTO;
import com.example.taskmanagementsystem.entity.Tags;
import com.example.taskmanagementsystem.entity.Tasks;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mappers {

    private static final String imageLocationDirectory = System.getenv("image.upload.dir");

    public static Tasks taskRequestToTaskEntityMapper(TaskCreateRequestDTO taskCreateRequestDTO) {
        Tasks tasks = new Tasks();
        tasks.setTaskName(taskCreateRequestDTO.getTaskName());
        tasks.setTaskDescription(taskCreateRequestDTO.getTaskDescription());
        tasks.setDeadLine(taskCreateRequestDTO.getDeadline());
        tasks.setPriority(taskCreateRequestDTO.getTaskPriority());
        return tasks;
    }

    private static List<String> getImageNames(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(multipartFile ->
                imageLocationDirectory + "/" + multipartFile.getOriginalFilename()
        ).collect(Collectors.toList());
    }

    public static TaskCreateRequestDTO taskEntityToTaskRequestMapper(Tasks tasks) {
        TaskCreateRequestDTO taskCreateRequestDTO = new TaskCreateRequestDTO();
        taskCreateRequestDTO.setTaskName(tasks.getTaskName());
        taskCreateRequestDTO.setTaskDescription(tasks.getTaskDescription());
        taskCreateRequestDTO.setDeadline(tasks.getDeadLine());
        taskCreateRequestDTO.setTaskPriority(tasks.getPriority());
        taskCreateRequestDTO.setTags(tasks.getTags().stream().map(Tags::getTagName).collect(Collectors.toSet()));
        return taskCreateRequestDTO;
    }

    public static TaskInfoDTO taskEntityToTaskInfoDTO(Tasks tasks){
        TaskInfoDTO taskInfoDTO = new TaskInfoDTO();
        taskInfoDTO.setTaskName(tasks.getTaskName());
        taskInfoDTO.setTaskDescription(tasks.getTaskDescription());
        taskInfoDTO.setDeadline(tasks.getDeadLine());
        taskInfoDTO.setTaskPriority(tasks.getPriority());
        taskInfoDTO.setTaskStatus(tasks.getStatus());
        taskInfoDTO.setTags(tasks.getTags().stream().map(Tags::getTagName).collect(Collectors.toSet()));
        taskInfoDTO.setFiles(tasks.getFiles());
        return taskInfoDTO;
    }

    public static TaskCreateResponseDTO requestToResponseDTO(TaskCreateRequestDTO taskCreateRequestDTO, Tasks createdTask){
        TaskCreateResponseDTO taskCreateResponseDTO = new TaskCreateResponseDTO();
        taskCreateResponseDTO.setTaskCreateRequestDTO(taskCreateRequestDTO);
        taskCreateResponseDTO.setNewTaskId(createdTask.getTaskId()+"");
        return taskCreateResponseDTO;
    }
}
