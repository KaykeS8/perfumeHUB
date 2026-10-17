package com.simao.perfumehub.dtos;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(name = "PaginationResponse", description = "Generic paginated response wrapper")
public class PaginationDto<T> {
    @Schema(description = "Items of the current page")
    private List<T> data;
    @Schema(description = "Zero-based current page index", example = "0")
    private int currentPage;
    @Schema(description = "Total number of pages", example = "1")
    private int totalPages;
    @Schema(description = "Total number of items across all pages", example = "1")
    private long totalItems;
    @Schema(description = "Requested page size", example = "20")
    private int pageSize;
    @Schema(description = "Whether a next page exists", example = "false")
    private boolean hasNext;
    @Schema(description = "Whether a previous page exists", example = "false")
    private boolean hasPrevious;
}