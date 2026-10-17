package com.simao.perfumehub.docs.schemas;

import com.simao.perfumehub.dtos.perfume.PerfumeResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Paginated response containing perfumes")
public record PerfumePaginationResponseDto(

        @ArraySchema(
                schema = @Schema(
                        implementation = PerfumeResponseDto.class
                ),
                arraySchema = @Schema(
                        description = "List of perfumes returned for the current page"
                )
        )
        List<PerfumeResponseDto> data,

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