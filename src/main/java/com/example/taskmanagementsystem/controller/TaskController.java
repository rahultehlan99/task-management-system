package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.*;
import com.example.taskmanagementsystem.service.TaskManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
        return new ResponseEntity<>(taskManagementService.createNewUserTask(taskCreateRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{taskId}/attachments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addUserTaskFiles(
            @PathVariable("taskId") String taskId,
            @RequestParam("files") List<MultipartFile> multipartFiles) {
        log.info("Request received for task files upload");
        taskManagementService.uploadTaskFiles(taskId, multipartFiles);
        return ResponseEntity.ok("Files uploaded Successfully");
    }

    @GetMapping(value = "/{taskId}/attachments")
    public ResponseEntity<List<String>> getUserTaskFiles(
            @PathVariable("taskId") String taskId) {
        log.info("Request received for task files download");
        return ResponseEntity.ok(taskManagementService.getTaskFilesPreSignedUrl(taskId));
    }

    @GetMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskInfoDTO>> getTasksViaTags(
            @RequestParam(value = "tags") List<String> tags,
            @RequestParam(required = false, value = "count", defaultValue = "10") int count) {
        log.info("Request received for all tasks with tags : {}", tags);
        return new ResponseEntity<>(taskManagementService.getTasksViaTags(tags, count), HttpStatus.OK);
    }

    @GetMapping(value = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskInfoDTO>> getTasks(
            @RequestParam(value = "taskId", required = false) String taskId) {
        log.info("Request received for task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.getTask(taskId), HttpStatus.OK);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BulkTaskFetchDTO>> getSpecificTasks(
            @RequestParam(value = "status", required = false) List<String> status,
            @RequestParam(value = "deadline", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
            @RequestParam(value = "priority", required = false) Integer priority) {
        log.info("Request received for filtered tasks");
        return new ResponseEntity<>(taskManagementService.getFilteredTasks(GetBulkTasksRequestDTO.builder()
                .status(status)
                .deadline(deadline)
                .priority(priority)
                .build()), HttpStatus.OK);
    }

    @PatchMapping(value = "/{taskId}/status/{newStatus}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> markTaskStatusViaTaskId(@PathVariable("taskId") String taskId,
                                                          @PathVariable("newStatus") String newStatus) throws RuntimeException {
        log.info("Request received for task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.changeTaskStatus(taskId, newStatus), HttpStatus.OK);
    }

    @PatchMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTask(@PathVariable("taskId") String taskId,
                                             @RequestBody TaskUpdateRequestDTO taskUpdateRequestDTO) {
        log.info("Request recevied for updating task : {}", taskId);
        return ResponseEntity.ok(taskManagementService.updateTask(taskId, taskUpdateRequestDTO));
    }

    @DeleteMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTask(@PathVariable("taskId") String taskId) throws RuntimeException {
        log.info("Request received for deleting task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.deleteTask(taskId), HttpStatus.OK);
    }

    @PostMapping(value = "/{taskId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> commentOnTask(@PathVariable String taskId, @RequestBody CommentRequestDTO commentRequestDTO) {
        log.info("Request received for adding comment : {}", commentRequestDTO);
        commentRequestDTO.setTaskId(taskId);
        return new ResponseEntity<>(taskManagementService.addComment(commentRequestDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/{taskId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTaskComments(@PathVariable("taskId") String taskId) {
        log.info("Request received for getting task comments : {}", taskId);
        return new ResponseEntity<>(taskManagementService.getComments(taskId), HttpStatus.OK);
    }

    @PatchMapping(value = "/{taskId}/reminder/revert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> revertTaskReminder(@PathVariable("taskId") String taskId) {
        return new ResponseEntity<>(taskManagementService.revertReminder(taskId), HttpStatus.OK);
    }
}
