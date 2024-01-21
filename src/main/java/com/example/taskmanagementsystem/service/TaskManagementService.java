package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.TaskCreateRequestDTO;
import com.example.taskmanagementsystem.dto.TaskCreateResponseDTO;
import com.example.taskmanagementsystem.dto.TaskInfoDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskManagementService {
    TaskCreateResponseDTO createNewUserTask(TaskCreateRequestDTO taskCreateRequestDTO);
    List<TaskInfoDTO> getTasksViaTags(List<String> tags, int count);

    List<TaskInfoDTO> getTask(String taskId);

    String changeTaskStatus(String taskId, String newStatus);

    String deleteTask(String taskId);

    void uploadTaskFiles(String taskId, List<MultipartFile> multipartFiles);

    List<Resource> downloadTaskFiles(String taskId);
}
