package com.simao.perfumehub.dtos.stockMovement;

import com.simao.perfumehub.entities.enums.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record StockMovementResponseDto(
        @Schema(description = "stock movement identifier", example = "1")
        Long id,

        @Schema(description = "reference to perfume", example = "1")
        Long perfumeId,

        @Schema(description = "perfume name", example = "Sauvage")
        String perfumeName,

        @Schema(description = "movement type", example = "OUTBOUND")
        MovementType type,

        @Schema(description = "quantity to operation", example = "3")
        Integer quantity,

        @Schema(description = "new quantity in stock", example = "1")
        Integer newStockBalance,

        @Schema(description = "date of operation", example = "2026-10-17T10:53:24.048788962")
        LocalDateTime date
) {
}
