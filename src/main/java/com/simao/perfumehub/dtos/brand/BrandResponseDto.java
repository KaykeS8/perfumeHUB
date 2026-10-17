package com.simao.perfumehub.dtos.brand;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "BrandResponse", description = "Brand returned by the API")
public record BrandResponseDto(
        @Schema(description = "Brand identifier", example = "1")
        Long id,
        @Schema(description = "Brand name", example = "Dior")
        String name
) {
}
