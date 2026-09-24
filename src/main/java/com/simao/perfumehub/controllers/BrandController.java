package com.simao.perfumehub.controllers;

import com.simao.perfumehub.dtos.brand.BrandRequestDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.dtos.brand.PaginationDto;
import com.simao.perfumehub.services.BrandService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<BrandResponseDto> createBrand(@RequestBody @Valid BrandRequestDto dto) {
       return ResponseEntity.status(HttpStatus.CREATED).body(brandService.createBrand(dto));
    }

    @GetMapping
    public ResponseEntity<PaginationDto<BrandResponseDto>> getAll(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(brandService.getall(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDto> findBrandById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(brandService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDto> updateBrand(@PathVariable Long id, @RequestBody @Valid BrandRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(brandService.updateBrand(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        Map<String, String> response = Map.of("Message", "Brand with ID: " + id + " was successfuly deleted");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}