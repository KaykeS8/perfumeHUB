package com.simao.perfumehub.mapper;

import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteRequestDto;
import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteResponseDto;
import com.simao.perfumehub.entities.PerfumeNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerfumeNoteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", source = "type")
    PerfumeNote toEntity(PerfumeNoteRequestDto dto);

    @Mapping(target = "noteId", source = "note.id")
    @Mapping(target = "noteName", source = "note.name")
    PerfumeNoteResponseDto toDto(PerfumeNote perfumeNote);

}
