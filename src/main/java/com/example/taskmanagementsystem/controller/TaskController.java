package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.annotations.TaskExists;
import com.example.taskmanagementsystem.dto.*;
import com.example.taskmanagementsystem.service.TaskManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskManagementService taskManagementService;

    @Operation(summary = "Add a user task")
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = "Task Successfully created")
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskCreateResponseDTO> addUserTask(@RequestBody TaskCreateRequestDTO taskCreateRequestDTO) {
        log.info("Request received for new task creation");
        return new ResponseEntity<>(taskManagementService.createNewUserTask(taskCreateRequestDTO), HttpStatus.CREATED);
    }

    @TaskExists
    @Operation(summary = "Add attachments to a user task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment added successfully"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @PostMapping(value = "/{taskId}/attachments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addUserTaskFiles(
            @PathVariable("taskId") String taskId,
            @RequestParam("files") List<MultipartFile> multipartFiles) {
        log.info("Request received for task files upload");
        taskManagementService.uploadTaskFiles(taskId, multipartFiles);
        return ResponseEntity.ok("Files uploaded Successfully");
    }

    @TaskExists
    @Operation(summary = "Get attachments for a user task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment fetched successfully"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @GetMapping(value = "/{taskId}/attachments")
    public ResponseEntity<List<String>> getUserTaskFiles(
            @PathVariable("taskId") String taskId) {
        log.info("Request received for task files download");
        return ResponseEntity.ok(taskManagementService.getTaskFilesPreSignedUrl(taskId));
    }

    @Operation(summary = "Get tasks via tags")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Tasks via tags fetched successfully")
    )
    @GetMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskInfoDTO>> getTasksViaTags(
            @RequestParam(value = "tags") List<String> tags,
            @RequestParam(required = false, value = "count", defaultValue = "10") int count) {
        log.info("Request received for all tasks with tags : {}", tags);
        return new ResponseEntity<>(taskManagementService.getTasksViaTags(tags, count), HttpStatus.OK);
    }

    @Operation(summary = "Get task via ID or all tasks if empty, pagination applied with pageNo and pageSize sorted by parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task(s) fetched successfully"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginatedResponse<TaskInfoDTO>> getTasks(
            @RequestParam(value = "taskId", required = false) String taskId,
            @Min(0) @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
            @Min(1) @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @Pattern(regexp = "deadLine|createdAt|updatedAt", message = "Must be one of deadLine, createdAt, updatedAt") @RequestParam(value = "sortBy", defaultValue = "deadLine", required = false) String sortBy,
            @Pattern(regexp = "ASC|DESC", message = "Must be one of ASC or DESC") @RequestParam(value = "sortDir", defaultValue = "DESC", required = false) String sortingDirection) {
        log.info("Request received for task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.getTasks(taskId, pageNo, pageSize, sortBy, sortingDirection), HttpStatus.OK);
    }

    @Operation(summary = "Get tasks basis fields")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Task(s) fetched successfully")
    )
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskInfoDTO>> getSpecificTasks(
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

    @TaskExists
    @Operation(summary = "Update task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @PatchMapping(value = "/{taskId}/status/{newStatus}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> markTaskStatusViaTaskId(@PathVariable("taskId") String taskId,
                                                          @PathVariable("newStatus") String newStatus) throws RuntimeException {
        log.info("Request received for task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.changeTaskStatus(taskId, newStatus), HttpStatus.OK);
    }

    @TaskExists
    @Operation(summary = "Task updated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @PatchMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTask(@PathVariable("taskId") String taskId,
                                             @RequestBody TaskUpdateRequestDTO taskUpdateRequestDTO) {
        log.info("Request received for updating task : {}", taskId);
        return ResponseEntity.ok(taskManagementService.updateTask(taskId, taskUpdateRequestDTO));
    }

    @TaskExists
    @Operation(summary = "Task deletion successful")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @DeleteMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTask(@PathVariable("taskId") String taskId) throws RuntimeException {
        log.info("Request received for deleting task : {}", taskId);
        return new ResponseEntity<>(taskManagementService.deleteTask(taskId), HttpStatus.OK);
    }

    @TaskExists
    @Operation(summary = "Post task comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task comment added"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @PostMapping(value = "/{taskId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> commentOnTask(@PathVariable String taskId, @RequestBody CommentRequestDTO commentRequestDTO) {
        log.info("Request received for adding comment : {}", commentRequestDTO);
        commentRequestDTO.setTaskId(taskId);
        return new ResponseEntity<>(taskManagementService.addComment(commentRequestDTO), HttpStatus.OK);
    }

    @TaskExists
    @Operation(summary = "Get task comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task comments fetched"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @GetMapping(value = "/{taskId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentResponseDTO>> getTaskComments(@PathVariable("taskId") String taskId) {
        log.info("Request received for getting task comments : {}", taskId);
        return new ResponseEntity<>(taskManagementService.getComments(taskId), HttpStatus.OK);
    }

    @TaskExists
    @Operation(summary = "Task reminder reverted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task reminder reverted"),
            @ApiResponse(responseCode = "404", description = "No such task exist")
    }
    )
    @PatchMapping(value = "/{taskId}/reminder/revert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> revertTaskReminder(@PathVariable("taskId") String taskId) {
        return new ResponseEntity<>(taskManagementService.revertReminder(taskId), HttpStatus.OK);
    }
}
