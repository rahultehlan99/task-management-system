package com.example.taskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskCreateResponseDTO {
    @JsonProperty("taskId")
    private String newTaskId;
    @JsonProperty("taskInfo")
    private TaskCreateRequestDTO taskCreateRequestDTO;
}
