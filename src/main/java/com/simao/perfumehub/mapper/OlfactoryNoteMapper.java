package com.simao.perfumehub.mapper;

import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteRequestDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteResponseDto;
import com.simao.perfumehub.entities.OlfactoryNote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OlfactoryNoteMapper {
    OlfactoryNote toEntity(OlfactoryNoteRequestDto dto);
    OlfactoryNoteResponseDto toDto(OlfactoryNote olfactoryNote);
}
