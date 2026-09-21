package com.simao.perfumehub.dtos.perfume;

import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteResponseDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.entities.enums.Concentration;
import com.simao.perfumehub.entities.enums.Genre;

import java.math.BigDecimal;
import java.util.List;

public record PerfumeResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantityOfStock,
        Genre genre,
        Concentration concentration,
        BrandResponseDto brand,
        List<PerfumeNoteResponseDto> notes
) {
}
