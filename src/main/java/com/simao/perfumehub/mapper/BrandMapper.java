package com.simao.perfumehub.mapper;

import com.simao.perfumehub.dtos.brand.BrandRequestDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.entities.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toEntity(BrandRequestDto dto);
    BrandResponseDto toDto(Brand brand);
}
