package com.simao.perfumehub.mapper;

import com.simao.perfumehub.dtos.brand.PaginationDto;
import org.springframework.data.domain.Page;

public class PaginationMapper {

    public static <T> PaginationDto<T> buildPaginationDto(Page<T> page) {
        return new PaginationDto<>(
          page.getContent(),
          page.getNumber(),
          page.getTotalPages(),
          page.getTotalElements(),
          page.getSize(),
          page.hasNext(),
          page.hasPrevious()
        );
    }
}
