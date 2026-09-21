package com.simao.perfumehub.dtos.perfume;

import com.simao.perfumehub.entities.enums.Concentration;
import com.simao.perfumehub.entities.enums.Genre;
import com.simao.perfumehub.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PerfumeRequestDto(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull @Positive
        BigDecimal price,

        @NotNull @PositiveOrZero
        Integer quantityOfStock,

        @NotBlank
        @ValueOfEnum(enumClass = Genre.class, message = "Gender should be one of the following values: MASCULINE, FEMININE, UNISSEX")
        String genre,

        @NotBlank
        @ValueOfEnum(enumClass = Concentration.class, message = "Concentration should be one of the following values: PARFUM, EDP, EDC, BODY_SPLASH")
        String concentration,

        @NotNull Long brandId
        ) {
}
