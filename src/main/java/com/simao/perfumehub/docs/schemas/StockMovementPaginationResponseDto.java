package com.simao.perfumehub.docs.schemas;

import com.simao.perfumehub.dtos.perfume.PerfumeResponseDto;
import com.simao.perfumehub.dtos.stockMovement.StockMovementResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record StockMovementPaginationResponseDto(
        @ArraySchema(
                schema = @Schema(
                        implementation = StockMovementResponseDto.class
                ),
                arraySchema = @Schema(
                        description = "List of stock movements returned for the current page"
                )
        )
        List<StockMovementResponseDto> data,

        @Schema(description = "Current page index, starting at zero", example = "0")
        int currentPage,

        @Schema(description = "Total number of pages", example = "5")
        int totalPages,

        @Schema(description = "Total number of perfumes matching the filters", example = "95")
        long totalItems,

        @Schema(description = "Maximum number of items per page", example = "20")
        int pageSize,

        @Schema(description = "Whether another page is available", example = "true")
        boolean hasNext,

        @Schema(description = "Whether a previous page is available", example = "false")
        boolean hasPrevious
) {
}
