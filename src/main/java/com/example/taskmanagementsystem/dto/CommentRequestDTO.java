package com.example.taskmanagementsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    private String taskId;
    private String description;
}
