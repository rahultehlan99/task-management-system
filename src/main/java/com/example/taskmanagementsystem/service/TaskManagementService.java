package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskManagementService {
    TaskCreateResponseDTO createNewUserTask(TaskCreateRequestDTO taskCreateRequestDTO);
    List<TaskInfoDTO> getTasksViaTags(List<String> tags, int count);

    PaginatedResponse<TaskInfoDTO> getTasks(String taskId, int pageNo, int pageSize, String sortBy, String sortingDirection);

    List<TaskInfoDTO> getFilteredTasks(GetBulkTasksRequestDTO tasksRequestDTO);

    String changeTaskStatus(String taskId, String newStatus);

    String updateTask(String taskid, TaskUpdateRequestDTO taskUpdateRequestDTO);
    String deleteTask(String taskId);

    void uploadTaskFiles(String taskId, List<MultipartFile> multipartFiles);

    List<String> getTaskFilesPreSignedUrl(String taskId);

    String addComment(CommentRequestDTO commentRequestDTO);

    List<CommentResponseDTO> getComments(String taskId);

    String revertReminder(String taskId);
}
