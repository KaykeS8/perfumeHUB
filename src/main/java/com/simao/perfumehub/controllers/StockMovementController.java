package com.simao.perfumehub.controllers;

import com.simao.perfumehub.docs.StockMovementsControllerDocs;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stock/movements")
public class StockMovementController implements StockMovementsControllerDocs {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<StockMovementResponseDto> registerMovement(@RequestBody @Valid StockMovementRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(stockMovementService.registerMovement(dto));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<PaginationDto<StockMovementResponseDto>> getAllMovements(
            @RequestParam(required = false) Long perfumeId,
            @RequestParam(required = false) String type,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockMovementService.getAllMovements(perfumeId, type, pageable));
    }
}