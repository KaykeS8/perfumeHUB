package com.simao.perfumehub.dtos.perfumeNote;

import com.simao.perfumehub.entities.enums.NoteType;
import com.simao.perfumehub.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PerfumeNoteRequestDto(
        @NotNull
        @Schema(description = "reference of note", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long noteId,

        @NotBlank
        @ValueOfEnum(enumClass = NoteType.class, message = "Note type should be one of the following values: TOP, MIDDLE, BASE")
        @Schema(description = "Type of note", example = "TOP", requiredMode = Schema.RequiredMode.REQUIRED)
        String type
) {
}
