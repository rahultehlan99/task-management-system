package com.example.taskmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBulkTasksRequestDTO {
    private List<String> status;
    private LocalDateTime deadline;
    private Integer priority;
}
