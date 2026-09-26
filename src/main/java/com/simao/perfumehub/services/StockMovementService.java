package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.dtos.stockMovement.StockMovementRequestDto;
import com.simao.perfumehub.dtos.stockMovement.StockMovementResponseDto;
import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.entities.StockMovement;
import com.simao.perfumehub.entities.enums.MovementType;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.PaginationMapper;
import com.simao.perfumehub.repositories.PerfumeRepository;
import com.simao.perfumehub.repositories.StockMovementRepository;
import com.simao.perfumehub.specifications.StockMovementSpecification;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final PerfumeRepository perfumeRepository;
    private final Logger log = LoggerFactory.getLogger(StockMovementService.class);

    public StockMovementService(StockMovementRepository stockMovementRepository, PerfumeRepository perfumeRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.perfumeRepository = perfumeRepository;
    }

    @Transactional
    public StockMovementResponseDto registerMovement(StockMovementRequestDto dto) {
        log.info("Register movements...");
        Perfume perfume = perfumeRepository.findById(dto.perfumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfume not found with ID: " + dto.perfumeId()));

        MovementType type = MovementType.valueOf(dto.movementType());
        int newStock = switch (type) {
            case OUTBOUND -> perfume.getQuantityOfStock() - dto.quantity();
            case INBOUND -> perfume.getQuantityOfStock() + dto.quantity();
            case ADJUSTMENT -> dto.quantity();
        };

        if (newStock < 0) {
            throw new ResourceConflictException(
                    "Insufficient stock for this operation. Current: " + perfume.getQuantityOfStock() +
                    ", requested: " + dto.quantity()
            );
        }

        perfume.setQuantityOfStock(newStock);
        perfumeRepository.save(perfume);

        StockMovement stockMovement = new StockMovement();
        stockMovement.setPerfume(perfume);
        stockMovement.setMovementType(type);
        stockMovement.setQuantity(dto.quantity());
        stockMovement.setResultingBalance(newStock);

        stockMovement = stockMovementRepository.save(stockMovement);


        return convertToDto(stockMovement);
    }

    public PaginationDto<StockMovementResponseDto> getAllMovements(Long perfumeId, String type, Pageable pageable) {
        Specification<StockMovement> spec = Specification
                .where(StockMovementSpecification.hasPerfumeId(perfumeId))
                .and(StockMovementSpecification.hasType(type));
        Page<StockMovementResponseDto> movements = stockMovementRepository.findAll(spec, pageable).map(this::convertToDto);

        return PaginationMapper.buildPaginationDto(movements);
    }


    private StockMovementResponseDto convertToDto(StockMovement stockMovement) {
        return new StockMovementResponseDto(
                stockMovement.getId(),
                stockMovement.getPerfume().getId(),
                stockMovement.getPerfume().getName(),
                stockMovement.getMovementType(),
                stockMovement.getQuantity(),
                stockMovement.getResultingBalance(),
                stockMovement.getDate()
        );
    }

}