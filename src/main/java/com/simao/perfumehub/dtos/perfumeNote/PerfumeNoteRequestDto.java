package com.simao.perfumehub.dtos.perfumeNote;

import com.simao.perfumehub.entities.enums.NoteType;
import com.simao.perfumehub.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PerfumeNoteRequestDto(
        @NotNull
        Long noteId,

        @NotBlank
        @ValueOfEnum(enumClass = NoteType.class, message = "Note type should be one of the following values: TOP, MIDDLE, BASE")
        String type
) {
}
