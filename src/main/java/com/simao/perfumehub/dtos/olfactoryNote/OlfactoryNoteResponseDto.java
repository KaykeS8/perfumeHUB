package com.simao.perfumehub.dtos.olfactoryNote;

import io.swagger.v3.oas.annotations.media.Schema;

public record OlfactoryNoteResponseDto(
        @Schema(description = "olfactory note identifier", example = "1")
        Long id,

        @Schema(description = "name of olfactory note", example = "Baunilha")
        String name
) {
}
