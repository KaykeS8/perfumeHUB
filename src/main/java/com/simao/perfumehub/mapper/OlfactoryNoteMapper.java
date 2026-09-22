package com.simao.perfumehub.mapper;

import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteRequestDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteResponseDto;
import com.simao.perfumehub.entities.OlfactoryNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OlfactoryNoteMapper {
    @Mapping(target = "id", ignore = true)
    OlfactoryNote toEntity(OlfactoryNoteRequestDto dto);
    OlfactoryNoteResponseDto toDto(OlfactoryNote olfactoryNote);
}
