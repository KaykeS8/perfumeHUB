package com.simao.perfumehub.controllers;

import com.simao.perfumehub.docs.BrandControllerDocs;
import com.simao.perfumehub.dtos.brand.BrandRequestDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.services.BrandService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/brands")
public class BrandController implements BrandControllerDocs {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<BrandResponseDto> createBrand(@RequestBody @Valid BrandRequestDto dto) {
       return ResponseEntity.status(HttpStatus.CREATED).body(brandService.createBrand(dto));
    }

    @Override
    @GetMapping
    public ResponseEntity<PaginationDto<BrandResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(brandService.getall(pageable));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDto> findBrandById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(brandService.findById(id));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<BrandResponseDto> updateBrand(@PathVariable Long id, @RequestBody @Valid BrandRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(brandService.updateBrand(id, dto));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}