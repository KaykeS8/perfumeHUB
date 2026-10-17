package com.simao.perfumehub.docs.schemas;

import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Paginated response containing brands")
public record BrandPaginationResponseDto(

    @ArraySchema(
            schema = @Schema(
                    implementation = BrandResponseDto.class
            ),
            arraySchema = @Schema(
                    description = "List of brands returned for the current page"
            )
    )
    List<BrandResponseDto> data,

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

