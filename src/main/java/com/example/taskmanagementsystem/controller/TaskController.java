package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.TaskCreateRequestDTO;
import com.example.taskmanagementsystem.dto.TaskCreateResponseDTO;
import com.example.taskmanagementsystem.dto.TaskInfoDTO;
import com.example.taskmanagementsystem.service.TaskManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskManagementService taskManagementService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskCreateResponseDTO> addUserTask(@RequestBody TaskCreateRequestDTO taskCreateRequestDTO) {
        log.info("Request received for new task creation");
        return ResponseEntity.ok(taskManagementService.createNewUserTask(taskCreateRequestDTO));
    }

    @PostMapping(value = "/upload/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addUserTaskFiles(
            @PathVariable("taskId") String taskId,
            @RequestParam("files") List<MultipartFile> multipartFiles) {
        log.info("Request received for task files upload");
        taskManagementService.uploadTaskFiles(taskId, multipartFiles);
        return ResponseEntity.ok("Files uploaded Successfully");
    }

    // TODO : download multiple images
    @GetMapping(value = "/download/{taskId}")
    public ResponseEntity<Resource> getUserTaskFiles(
            @PathVariable("taskId") String taskId) {
        log.info("Request received for task files upload");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(taskManagementService.downloadTaskFiles(taskId).get(0));
    }

    @GetMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskInfoDTO>> getTasksViaTags(
            @RequestParam(value = "tags") List<String> tags,
            @RequestParam(value = "count") int count) {
        log.info("Request received for all tasks with tags : {}", tags);
        return new ResponseEntity<>(taskManagementService.getTasksViaTags(tags, count), HttpStatus.OK);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskInfoDTO>> getTaskById(
            @RequestParam(value = "taskId", required = false) String taskId) {
        log.info("Request received for task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.getTask(taskId), HttpStatus.OK);
    }

    @GetMapping(value = "/{taskId}/{newStatus}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> markTaskStatusViaTaskId(@PathVariable("taskId") String taskId,
                                                          @PathVariable("newStatus") String newStatus) throws RuntimeException {
        log.info("Request received for task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.changeTaskStatus(taskId, newStatus), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTask(@PathVariable("taskId") String taskId) throws RuntimeException {
        log.info("Request received for deleting task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.deleteTask(taskId), HttpStatus.OK);
    }
}
