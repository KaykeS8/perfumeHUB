package com.simao.perfumehub.controllers;

import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.dtos.stockMovement.StockMovementRequestDto;
import com.simao.perfumehub.dtos.stockMovement.StockMovementResponseDto;
import com.simao.perfumehub.services.StockMovementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock/movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    public ResponseEntity<StockMovementResponseDto> registerMovement(@RequestBody @Valid StockMovementRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(stockMovementService.registerMovement(dto));
    }

    @GetMapping
    public ResponseEntity<PaginationDto<StockMovementResponseDto>> getAllMovements(
            @RequestParam(required = false) Long perfumeId,
            @RequestParam(required = false) String type,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(stockMovementService.getAllMovements(perfumeId, type, pageable));
    }
}
