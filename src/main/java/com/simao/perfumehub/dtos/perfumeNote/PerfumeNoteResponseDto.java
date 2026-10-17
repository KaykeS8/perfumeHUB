package com.simao.perfumehub.dtos.perfumeNote;

import com.simao.perfumehub.entities.enums.NoteType;
import io.swagger.v3.oas.annotations.media.Schema;

public record PerfumeNoteResponseDto(
        @Schema(description = "perfume note identifier", example = "1")
        Long id,
        @Schema(description = "reference to note", example = "1")
        Long noteId,
        @Schema(description = "note name", example = "Baunilha")
        String noteName,
        @Schema(description = "Type of note", example = "TOP")
        NoteType type
) {
}
