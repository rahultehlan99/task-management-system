package com.example.taskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskInfoDTO {
    private String taskId;
    private String taskName;
    private String taskDescription;
    private int taskPriority;
    private LocalDateTime deadline;
    private String taskStatus;
    private Set<String> tags;
    private List<String> files;
    private LocalDateTime createdAt;
    private boolean reminderEnabled;
}
