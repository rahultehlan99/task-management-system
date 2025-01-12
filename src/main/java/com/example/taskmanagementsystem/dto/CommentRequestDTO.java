package com.example.taskmanagementsystem.dto;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    @Transient
    private String taskId;
    private String description;
}
