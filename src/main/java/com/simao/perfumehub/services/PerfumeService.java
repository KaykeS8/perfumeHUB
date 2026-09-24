package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.brand.PaginationDto;
import com.simao.perfumehub.dtos.perfume.PerfumeRequestDto;
import com.simao.perfumehub.dtos.perfume.PerfumeResponseDto;
import com.simao.perfumehub.entities.Brand;
import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.entities.enums.Concentration;
import com.simao.perfumehub.entities.enums.Genre;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.PaginationMapper;
import com.simao.perfumehub.mapper.PerfumeMapper;
import com.simao.perfumehub.repositories.BrandRepository;
import com.simao.perfumehub.repositories.PerfumeRepository;
import com.simao.perfumehub.specifications.PerfumeSpecifications;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;
    private final Logger log = LoggerFactory.getLogger(PerfumeService.class);
    private final PerfumeMapper mapper;


    public PerfumeService(PerfumeRepository perfumeRepository, PerfumeMapper mapper, BrandRepository brandRepository) {
        this.perfumeRepository = perfumeRepository;
        this.brandRepository = brandRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PerfumeResponseDto createPerfume(PerfumeRequestDto dto) {
        log.info("Creating perfume...");

        if (perfumeRepository.existsByNameIgnoreCase(dto.name())) {
          throw new ResourceConflictException("Perfume already exist by name: " + dto.name());
        }

        Brand brand = brandRepository.findById(dto.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + dto.brandId()));
        Perfume perfume = mapper.toEntity(dto);
        perfume.setBrand(brand);
        return mapper.toDto(perfumeRepository.save(perfume));
    }

    public PaginationDto<PerfumeResponseDto> getAll(
            String brand,
            String genre,
            String concentration,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String name,
            Pageable pageable
    ) {
        log.info("Get all perfumes...");
        Specification<Perfume> spec = Specification
                .where(PerfumeSpecifications.hasBrand(brand))
                .and(PerfumeSpecifications.hasGenre(genre))
                .and(PerfumeSpecifications.hasConcentration(concentration))
                .and(PerfumeSpecifications.priceBetween(minPrice, maxPrice))
                .and(PerfumeSpecifications.nameContains(name));

        Page<PerfumeResponseDto> page = perfumeRepository.findAll(spec, pageable).map(mapper::toDto);
        return PaginationMapper.buildPaginationDto(page);
    }

    public PerfumeResponseDto findPerfumeById(Long id) {
        log.info("Finding perfume by ID: {}", id);
        return perfumeRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Perfume not found with ID: " + id));
    }

    @Transactional
    public PerfumeResponseDto updatePerfume(Long id, PerfumeRequestDto dto) {
        log.info("Updating perfume with ID: {}", id);
        Brand brand = brandRepository.findById(dto.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + dto.brandId()));

        return perfumeRepository.findById(id)
                .map(perfume -> {
                    if (!perfume.getName().equalsIgnoreCase(dto.name()) && perfumeRepository.existsByNameIgnoreCase(dto.name())) {
                        throw new ResourceConflictException("Perfume already exist by name: " + dto.name());
                    }
                    perfume.setName(dto.name());
                    perfume.setDescription(dto.description());
                    perfume.setPrice(dto.price());
                    perfume.setQuantityOfStock(dto.quantityOfStock());
                    perfume.setGenre(Genre.valueOf(dto.genre()));
                    perfume.setConcentration(Concentration.valueOf(dto.concentration()));
                    perfume.setBrand(brand);
                    return mapper.toDto(perfumeRepository.save(perfume));
                }).orElseThrow(() -> new ResourceNotFoundException("Perfume not found with ID: " + id));
    }

    public void deletePerfume(Long id) {
        log.info("Deleting perfume with ID: {}",id);
        if (!perfumeRepository.existsById(id)) throw new ResourceNotFoundException("Perfume not found with ID: " + id);
        perfumeRepository.deleteById(id);
    }
}