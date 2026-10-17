package com.simao.perfumehub.dtos.olfactoryNote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record OlfactoryNoteRequestDto(
        @NotBlank
        @Schema(description = "Olfactory note name", example = "Baunilha", requiredMode = Schema.RequiredMode.REQUIRED)
        String name
) {
}