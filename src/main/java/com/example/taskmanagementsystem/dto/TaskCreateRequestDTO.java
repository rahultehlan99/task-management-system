package com.example.taskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TaskCreateRequestDTO {
    private String taskName;
    private String taskDescription;
    private int taskPriority;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadline;
    private boolean reminderEnabled = true;
    private Set<String> tags; // will create new tag if not present in TAGS table
}
