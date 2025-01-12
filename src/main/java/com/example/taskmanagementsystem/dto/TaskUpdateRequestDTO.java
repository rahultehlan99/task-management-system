package com.example.taskmanagementsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateRequestDTO {
    private String taskName;
    private LocalDateTime deadLine;
    private Integer priority;
    private String taskDescription;

}
