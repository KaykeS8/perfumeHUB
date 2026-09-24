package com.simao.perfumehub.controllers;

import com.simao.perfumehub.dtos.brand.PaginationDto;
import com.simao.perfumehub.dtos.perfume.PerfumeRequestDto;
import com.simao.perfumehub.dtos.perfume.PerfumeResponseDto;
import com.simao.perfumehub.services.PerfumeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/perfumes")
public class PerfumeController {

    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @PostMapping
    public ResponseEntity<PerfumeResponseDto> createPerfume(@RequestBody @Valid PerfumeRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfumeService.createPerfume(dto));
    }

    @GetMapping
    public ResponseEntity<PaginationDto<PerfumeResponseDto>> getAll(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String concentration,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String note,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(perfumeService.getAll(brand, genre, concentration, minPrice, maxPrice, name, note, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfumeResponseDto> findPerfumeById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(perfumeService.findPerfumeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfumeResponseDto> updatePerfume(@PathVariable Long id, @RequestBody @Valid PerfumeRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(perfumeService.updatePerfume(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long id) {
        perfumeService.deletePerfume(id);
        return ResponseEntity.noContent().build();
    }
}