package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.brand.BrandRequestDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.entities.Brand;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.BrandMapper;
import com.simao.perfumehub.mapper.PaginationMapper;
import com.simao.perfumehub.repositories.BrandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final Logger log = LoggerFactory.getLogger(BrandService.class);
    private final BrandMapper mapper;

    public BrandService(BrandRepository brandRepository, BrandMapper mapper) {
        this.brandRepository = brandRepository;
        this.mapper = mapper;
    }

    public BrandResponseDto createBrand(BrandRequestDto dto) {
        log.info("Creating brand...");
        if (brandRepository.existsByNameIgnoreCase(dto.name())){
            throw new ResourceConflictException("Brand already exist with name: " + dto.name());
        }
        Brand brand = mapper.toEntity(dto);
        return mapper.toDto(brandRepository.save(brand));
    }

    public PaginationDto<BrandResponseDto> getall(Pageable pageable) {
        log.info("Listing brands");
        Page<BrandResponseDto> page = brandRepository.findAll(pageable).map(mapper::toDto);
        return PaginationMapper.buildPaginationDto(page);
    }

    public BrandResponseDto findById(Long id) {
        log.info("Finding brand by ID: {}", id);
        return brandRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));
    }

    public BrandResponseDto updateBrand(Long id, BrandRequestDto dto) {
        log.info("Updating brand by ID: {}", id);
        return brandRepository.findById(id)
                .map(brand -> {
                    if (!brand.getName().equalsIgnoreCase(dto.name()) && brandRepository.existsByNameIgnoreCase(dto.name())){
                        throw new ResourceConflictException("Brand already exist with name: " + dto.name());
                    }
                    brand.setName(dto.name());
                    return mapper.toDto(brandRepository.save(brand));
                }).orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));
    }

    public void deleteBrand(Long id) {
        log.info("deleting brand by ID: {}", id);
        if (!brandRepository.existsById(id)) throw new ResourceNotFoundException("Brand not found with ID: " + id);
        brandRepository.deleteById(id);
    }
}