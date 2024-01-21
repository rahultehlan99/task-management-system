package com.example.taskmanagementsystem.dto;

import com.example.taskmanagementsystem.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TaskInfoDTO {
    private String taskName;
    private String taskDescription;
    private int taskPriority;
    private LocalDateTime deadline;
    private TaskStatus taskStatus;
    private Set<String> tags;
    private List<String> files;
}
