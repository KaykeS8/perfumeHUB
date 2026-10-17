package com.simao.perfumehub.dtos.perfume;

import com.simao.perfumehub.entities.enums.Concentration;
import com.simao.perfumehub.entities.enums.Genre;
import com.simao.perfumehub.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PerfumeRequestDto(
        @NotBlank
        @Schema(description = "Unique perfume name",
                example = "Acqua Di Giò",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotBlank
        @Schema(description = "Description about perfume",
                example = "Fragrância aquática e cítrica que remete à brisa do mar e ao sol do Mediterrâneo.",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String description,

        @NotNull @Positive
        @Schema(description = "Price of perfume is positive",
                example = "650",
                requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal price,

        @NotNull @PositiveOrZero
        @Schema(description = "Quantity of stock of perfume need be positive or zero",
                example = "5",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Integer quantityOfStock,

        @NotBlank
        @ValueOfEnum(enumClass = Genre.class, message = "Gender should be one of the following values: MASCULINE, FEMININE, UNISSEX")
        @Schema(description = "Genre of perfume",
                example = "MASCULINE",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String genre,

        @NotBlank
        @ValueOfEnum(enumClass = Concentration.class, message = "Concentration should be one of the following values: PARFUM, EDP, EDC, BODY_SPLASH")
        @Schema(description = "Concentration of perfume",
                example = "EDT",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String concentration,

        @NotNull
        @Schema(description = "brand reference",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Long brandId
        ) {
}
