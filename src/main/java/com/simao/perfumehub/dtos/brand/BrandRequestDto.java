package com.simao.perfumehub.dtos.brand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "BrandRequest", description = "Payload to create or update a brand")
public record BrandRequestDto(
        @NotBlank
        @Schema(description = "Unique brand name", example = "Dior", requiredMode = Schema.RequiredMode.REQUIRED)
        String name
) {
}
