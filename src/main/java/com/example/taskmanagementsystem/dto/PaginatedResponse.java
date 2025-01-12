package com.example.taskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class PaginatedResponse<T> {
    private List<T> data;
    private long pageNumber;
    private long totalPages;
    private long totalItems;
}
