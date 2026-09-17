package com.simao.perfumehub.dtos.brand;

import jakarta.validation.constraints.NotBlank;

public record BrandRequestDto(
        @NotBlank String name
) {
}