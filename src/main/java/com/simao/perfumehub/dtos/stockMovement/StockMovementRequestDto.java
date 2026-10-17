package com.simao.perfumehub.dtos.stockMovement;

import com.simao.perfumehub.entities.enums.MovementType;
import com.simao.perfumehub.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StockMovementRequestDto(
        @NotNull
        @Schema(description = "perfume identifier", example = "1")
        Long perfumeId,

        @NotBlank
        @ValueOfEnum(enumClass = MovementType.class, message = "Movement type should be one of the following values: INBOUND, OUTBOUND, ADJUSTMENT")
        @Schema(description = "movement type", example = "OUTBOUND")
        String movementType,

        @NotNull
        @PositiveOrZero
        @Schema(description = "quantity to operation", example = "3")
        Integer quantity

) {
}
