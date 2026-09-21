package com.simao.perfumehub.mapper;

import com.simao.perfumehub.dtos.perfume.PerfumeRequestDto;
import com.simao.perfumehub.dtos.perfume.PerfumeResponseDto;
import com.simao.perfumehub.entities.Perfume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerfumeMapper {
    @Mapping(target = "id", ignore = true)
    Perfume toEntity(PerfumeRequestDto dto);
    PerfumeResponseDto toDto(Perfume perfume);
}
