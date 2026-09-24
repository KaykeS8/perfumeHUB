package com.simao.perfumehub.dtos.perfumeNote;

import com.simao.perfumehub.entities.enums.NoteType;

public record PerfumeNoteResponseDto(
        Long id,
        Long noteId,
        String noteName,
        NoteType type
) {
}
