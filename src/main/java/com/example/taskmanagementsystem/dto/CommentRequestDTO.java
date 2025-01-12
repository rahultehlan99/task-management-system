package com.example.taskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    @JsonIgnore
    private String taskId;
    private String description;
}
