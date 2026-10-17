package com.simao.perfumehub.dtos.perfume;

import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteResponseDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.entities.enums.Concentration;
import com.simao.perfumehub.entities.enums.Genre;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public record PerfumeResponseDto(
        @Schema(description = "Perfume identifier", example = "1")
        Long id,
        @Schema(description = "Perfume name", example = "Acqua Di Giò")
        String name,
        @Schema(description = "Perfume description", example = "Fragrância aquática e cítrica que remete à brisa do mar e ao sol do Mediterrâneo.")
        String description,
        @Schema(description = "Perfume price", example = "650")
        BigDecimal price,
        @Schema(description = "Perfume quantity of stock", example = "5")
        Integer quantityOfStock,
        @Schema(description = "Perfume genre", example = "MASCULINE")
        Genre genre,
        @Schema(description = "Perfume concentration", example = "EDT")
        Concentration concentration,
        @Schema(description = "Brand associated with perfume", implementation = BrandResponseDto.class)
        BrandResponseDto brand,
        @ArraySchema(arraySchema = @Schema(description = "Olfactory notes associated with the perfume"), schema = @Schema(implementation = PerfumeNoteResponseDto.class))
        List<PerfumeNoteResponseDto> notes
) {
}
