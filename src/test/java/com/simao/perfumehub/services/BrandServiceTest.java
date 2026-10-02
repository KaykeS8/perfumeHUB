package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.brand.BrandRequestDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.entities.Brand;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.BrandMapper;
import com.simao.perfumehub.repositories.BrandRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Brand service - Test unit")
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandService brandService;

    @Nested
    @DisplayName("createBrand()")
    class CreateBrand {
        @Test
        @DisplayName("Should create a brand when all args is correctly")
        void shouldCreateBrandSuccessfully() {
            //Arrange (Given)
            BrandRequestDto brandRequestDto = new BrandRequestDto("Dior");
            Brand brandEntity = new Brand(1L, "Dior", new ArrayList<>());
            BrandResponseDto expectedResponse = new BrandResponseDto(1L, "Dior");

            when(brandRepository.existsByNameIgnoreCase("Dior")).thenReturn(false);
            when(brandMapper.toEntity(brandRequestDto)).thenReturn(brandEntity);
            when(brandRepository.save(brandEntity)).thenReturn(brandEntity);
            when(brandMapper.toDto(brandEntity)).thenReturn(expectedResponse);

            //Act (When)
            BrandResponseDto result = brandService.createBrand(brandRequestDto);

            //Assert (then)
            assertThat(result).isEqualTo(expectedResponse);
            verify(brandRepository).save(brandEntity);
        }

        @Test
        @DisplayName("Should throw exception when brand name already exists")
        void shouldThrowExceptionWhenBrandNameAlreadyExists() {
             //Arrange
             BrandRequestDto brandRequestDto = new BrandRequestDto("Dior");
             when(brandRepository.existsByNameIgnoreCase("Dior")).thenReturn(true);

             //Act
            assertThrows(ResourceConflictException.class, () -> brandService.createBrand(brandRequestDto));
            verify(brandRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindBrandById {
        @Test
        @DisplayName("Should return brand with valid ID")
        void shouldReturnBrandWithValidId() {
            Brand brandEntity = new Brand(1L, "Dior", new ArrayList<>());
            BrandResponseDto response = new BrandResponseDto(1L, "Dior");
            when(brandRepository.findById(1L))
                    .thenReturn(Optional.of(brandEntity));

            when(brandMapper.toDto(brandEntity))
                    .thenReturn(response);

            BrandResponseDto result = brandService.findById(1L);

            assertThat(result).isEqualTo(response);

            verify(brandRepository).findById(1L);
            verify(brandMapper).toDto(brandEntity);
        }

        @Test
        @DisplayName("Should return exception when not found brand by ID")
        void shouldThrowExceptionWhenNotFoundBrandByID() {
            when(brandRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> brandService.findById(1L));
            verify(brandMapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("updateBrand()")
    class UpdateBrand {

        @Test
        @DisplayName("Should update brand with data")
        void shouldUpdateBrandWithData() {
            BrandRequestDto requestDto = new BrandRequestDto("Lattafa");
            Brand entity = new Brand(1L, "Lattafa", new ArrayList<>());
            BrandResponseDto response = new BrandResponseDto(1L,"Lattafa");

            when(brandRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(brandRepository.save(any(Brand.class))).thenReturn(entity);
            when(brandMapper.toDto(entity)).thenReturn(response);

            BrandResponseDto result = brandService.updateBrand(1L, requestDto);

            assertThat(result).isEqualTo(response);

            verify(brandRepository).findById(1L);
            verify(brandRepository).save(any(Brand.class));
            verify(brandMapper).toDto(entity);
        }

        @Test
        @DisplayName("Should throw exception when brand name already exist")
        void shouldThrowExceptionWhenBrandNameAlreadyExists() {
            Long brandId = 1L;
            BrandRequestDto brandRequestDto = new BrandRequestDto("Dior");

            Brand entity = new Brand(1L, "Chanel", new ArrayList<>());

            when(brandRepository.findById(brandId)).thenReturn(Optional.of(entity));
            when(brandRepository.existsByNameIgnoreCase("Dior")).thenReturn(true);

            assertThrows(ResourceConflictException.class, () -> brandService.updateBrand(brandId, brandRequestDto));

            verify(brandRepository).existsByNameIgnoreCase("Dior");
            verify(brandRepository, never()).save(any(Brand.class));
        }

        @Test
        @DisplayName("Should throw exception when not found brand by ID")
        void shouldThrowExceptionWhenBrandNotFoundById() {
            Long brandId = 1L;
            BrandRequestDto brandRequestDto = new BrandRequestDto("Dior");
            when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> brandService.updateBrand(brandId, brandRequestDto));

            verify(brandRepository, never()).save(any(Brand.class));
        }
    }

    @Nested
    @DisplayName("deleteBrand()")
    class DeleteBrand {

        @Test
        @DisplayName("Should delete brand by id")
        void shouldDeleteBrandById() {
            when(brandRepository.existsById(1L)).thenReturn(true);
            doNothing().when(brandRepository).deleteById(1L);

            brandService.deleteBrand(1L);

            verify(brandRepository, times(1)).deleteById(1L);
        }


        @Test
        @DisplayName("Should throw exception when not found brand by ID")
        void shouldThrowsExceptionWhenNotFoundBrandById() {
            when(brandRepository.existsById(1L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> brandService.deleteBrand(1L));

            verify(brandRepository, never()).deleteById(any());
        }
    }
}
