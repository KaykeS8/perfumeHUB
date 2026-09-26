package com.simao.perfumehub.dtos.stockMovement;

import com.simao.perfumehub.entities.enums.MovementType;
import com.simao.perfumehub.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StockMovementRequestDto(
        @NotNull
        Long perfumeId,

        @NotBlank
        @ValueOfEnum(enumClass = MovementType.class, message = "Movement type should be one of the following values: INBOUND, OUTBOUND, ADJUSTMENT")
        String movementType,

        @NotNull
        @PositiveOrZero
        Integer quantity

) {
}
