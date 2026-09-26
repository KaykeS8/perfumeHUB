package com.simao.perfumehub.dtos.stockMovement;

import com.simao.perfumehub.entities.enums.MovementType;

import java.time.LocalDateTime;

public record StockMovementResponseDto(
        Long id,
        Long perfumeId,
        String perfumeName,
        MovementType type,
        Integer quantity,
        Integer newStockBalance,
        LocalDateTime date
) {
}
