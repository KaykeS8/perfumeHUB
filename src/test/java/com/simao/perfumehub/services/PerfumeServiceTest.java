package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.dtos.perfume.PerfumeRequestDto;
import com.simao.perfumehub.dtos.perfume.PerfumeResponseDto;
import com.simao.perfumehub.entities.Brand;
import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.entities.enums.Concentration;
import com.simao.perfumehub.entities.enums.Genre;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.PerfumeMapper;
import com.simao.perfumehub.repositories.BrandRepository;
import com.simao.perfumehub.repositories.PerfumeRepository;
import com.simao.perfumehub.repositories.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Perfume service - Test unit")
public class PerfumeServiceTest {

    @Mock
    private PerfumeRepository perfumeRepository;

    @Mock
    private PerfumeMapper mapper;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @InjectMocks
    private PerfumeService perfumeService;

    private Perfume entity;
    private Brand brandEntity;
    private BrandResponseDto brandResponseDto;
    private PerfumeRequestDto requestDto;
    private PerfumeResponseDto responseDto;

    @BeforeEach
    void setup() {
        brandEntity = new Brand(1L, "O Boticario", new ArrayList<>());
        brandResponseDto = new BrandResponseDto(1L, "O Boticario");

        entity = new Perfume(1L,"Malbec", "Muito bom", BigDecimal.valueOf(129), 5, Genre.MASCULINE,Concentration.EDP, brandEntity,null);
        requestDto = new PerfumeRequestDto("Malbec", "Muito bom", BigDecimal.valueOf(129), 5, "MASCULINE", "EDP",1L);
        responseDto = new PerfumeResponseDto(
                1L,
                "Malbec",
                "Muito bom",
                BigDecimal.valueOf(129),
                5,
                Genre.MASCULINE,
                Concentration.EDP,
                brandResponseDto,
                null
                );

    }

    @Nested
    @DisplayName("createPerfume()")
    class CreatePerfume {

        @Test
        @DisplayName("Should create perfume when brand exists and name is unique")
        void shouldCreatePerfumeSuccessfully() {
            when(perfumeRepository.existsByNameIgnoreCase("Malbec")).thenReturn(false);
            when(brandRepository.findById(1L)).thenReturn(Optional.of(brandEntity));
            when(mapper.toEntity(requestDto)).thenReturn(entity);
            when(perfumeRepository.save(entity)).thenReturn(entity);
            when(mapper.toDto(entity)).thenReturn(responseDto);

            PerfumeResponseDto result = perfumeService.createPerfume(requestDto);

            assertThat(result).isEqualTo(responseDto);
            assertThat(entity.getBrand()).isEqualTo(brandEntity);

            verify(perfumeRepository).existsByNameIgnoreCase("Malbec");
            verify(brandRepository).findById(1L);
            verify(perfumeRepository).save(entity);
            verify(mapper).toDto(entity);
        }

        @Test
        @DisplayName("Should throw exception when perfume name already exists")
        void shouldThrowExceptionWhenPerfumeNameAlreadyExists() {
            when(perfumeRepository.existsByNameIgnoreCase("Malbec")).thenReturn(true);

            assertThrows(ResourceConflictException.class, () -> perfumeService.createPerfume(requestDto));

            verify(perfumeRepository).existsByNameIgnoreCase("Malbec");
            verify(brandRepository, never()).findById(1L);
            verify(perfumeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when not found brand")
        void shouldThrowExceptionWheNotFoundBrand() {
            when(perfumeRepository.existsByNameIgnoreCase("Malbec")).thenReturn(false);
            when(brandRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> perfumeService.createPerfume(requestDto));

            verify(perfumeRepository).existsByNameIgnoreCase("Malbec");
            verify(brandRepository).findById(1L);
            verify(perfumeRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("findPerfumeById()")
    class FindPerfumeById {

        @Test
        @DisplayName("Should return perfume by ID")
        void shouldReturnPerfumeById() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(mapper.toDto(entity)).thenReturn(responseDto);

            PerfumeResponseDto result = perfumeService.findPerfumeById(1L);

            assertThat(result).isEqualTo(responseDto);

            verify(perfumeRepository).findById(1L);
            verify(mapper).toDto(entity);
        }

        @Test
        @DisplayName("Should throw exception when not found id")
        void shouldThrowExceptionWheNotFoundPerfumeById() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> perfumeService.findPerfumeById(1L));

            verify(perfumeRepository).findById(1L);
            verify(mapper, never()).toDto(entity);
        }
    }

    @Nested
    @DisplayName("updatePerfume()")
    class UpdatePerfume {

        private PerfumeRequestDto requestUpdate;
        private PerfumeResponseDto responseUpdate;

        @BeforeEach
        void setup() {
            requestUpdate = new PerfumeRequestDto("Aventus", "TOP das galaxias", BigDecimal.valueOf(1200), 5, "MASCULINE", "EDP",1L);
            responseUpdate = new PerfumeResponseDto(
                    1L,
                    "Aventus",
                    "TOP das galaxias",
                    BigDecimal.valueOf(1200),
                    5,
                    Genre.MASCULINE,
                    Concentration.EDP,
                    brandResponseDto,
                    null
            );

        }

        @Test
        @DisplayName("Should update perfume with data")
        void shouldUpdatePerfumeWithData() {

            when(brandRepository.findById(1L)).thenReturn(Optional.of(brandEntity));
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(perfumeRepository.existsByNameIgnoreCase("Aventus")).thenReturn(false);
            when(perfumeRepository.save(any(Perfume.class))).thenReturn(entity);
            when(mapper.toDto(entity)).thenReturn(responseUpdate);

            PerfumeResponseDto result = perfumeService.updatePerfume(1L, requestUpdate);

            assertThat(result).isEqualTo(responseUpdate);
            assertThat(result.name()).isEqualTo("Aventus");
            assertThat(result.description()).isEqualTo("TOP das galaxias");
            assertThat(result.price()).isEqualTo(BigDecimal.valueOf(1200));
            assertThat(result.quantityOfStock()).isEqualTo(5);
            assertThat(result.genre()).isEqualTo(Genre.valueOf("MASCULINE"));
            assertThat(result.concentration()).isEqualTo(Concentration.valueOf("EDP"));
            assertThat(result.brand()).isEqualTo(brandResponseDto);


            verify(brandRepository).findById(1L);
            verify(perfumeRepository).findById(1L);
            verify(perfumeRepository).existsByNameIgnoreCase("Aventus");
            verify(perfumeRepository).save(entity);
            verify(mapper).toDto(entity);
        }

        @Test
        @DisplayName("Should throw exception when not found brand to update perfume")
        void shouldThrowExceptionWhenNotFoundBrandToUpdatePerfume() {
            when(brandRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->  perfumeService.updatePerfume(1L, requestUpdate));

            verify(brandRepository).findById(1L);
            verify(perfumeRepository, never()).findById(any());
            verify(perfumeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when perfume name already exists")
        void shouldThrowExceptionWhenPerfumeNameAlreadyExists() {
            when(brandRepository.findById(1L)).thenReturn(Optional.of(brandEntity));
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(perfumeRepository.existsByNameIgnoreCase("Aventus")).thenReturn(true);

            assertThrows(ResourceConflictException.class, () -> perfumeService.updatePerfume(1L, requestUpdate));

            verify(brandRepository).findById(1L);
            verify(perfumeRepository).findById(1L);
            verify(perfumeRepository).existsByNameIgnoreCase("Aventus");
            verify(perfumeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when perfume not found")
        void shouldThrowExceptionWhenPerfumeNotFound() {
            when(brandRepository.findById(1L)).thenReturn(Optional.of(brandEntity));
            when(perfumeRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> perfumeService.updatePerfume(1L, requestUpdate));

            verify(brandRepository).findById(1L);
            verify(perfumeRepository).findById(1L);
            verify(perfumeRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deletePerfume()")
    class DeletePerfume {

        @Test
        @DisplayName("Should delete perfume if exists id and not have stock movements")
        void shouldDeletePerfumeIfExistsIdAndNotHaveStockMovements() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(stockMovementRepository.existsByPerfumeId(1L)).thenReturn(false);
            doNothing().when(perfumeRepository).deleteById(1L);

            perfumeService.deletePerfume(1L);

            verify(perfumeRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw exception when not found perfume")
        void shouldThrowExceptionWhenNotFoundPerfume() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> perfumeService.deletePerfume(1L));

            verify(perfumeRepository).findById(1L);
            verify(stockMovementRepository, never()).existsByPerfumeId(1L);
            verify(perfumeRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Should throw exception when exists stock movements to perfume")
        void shouldThrowExceptionWhenExistsStockMovementsToPerfume() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(stockMovementRepository.existsByPerfumeId(1L)).thenReturn(true);

            assertThrows(ResourceConflictException.class, () -> perfumeService.deletePerfume(1L));

            verify(perfumeRepository).findById(1L);
            verify(stockMovementRepository).existsByPerfumeId(1L);
            verify(perfumeRepository, never()).deleteById(any());
        }
    }
}
