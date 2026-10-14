package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.stockMovement.StockMovementRequestDto;
import com.simao.perfumehub.dtos.stockMovement.StockMovementResponseDto;
import com.simao.perfumehub.entities.Brand;
import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.entities.StockMovement;
import com.simao.perfumehub.entities.enums.Concentration;
import com.simao.perfumehub.entities.enums.Genre;
import com.simao.perfumehub.entities.enums.MovementType;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.repositories.PerfumeRepository;
import com.simao.perfumehub.repositories.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Stock movements service - Test unit")
public class StockMovementServiceTest {

    @Mock
    private StockMovementRepository stockMovementRepository;

    @Mock
    private PerfumeRepository perfumeRepository;

    @InjectMocks
    private StockMovementService stockMovementService;

    private Perfume perfume;
    private StockMovementResponseDto responseDto;

    @BeforeEach
    void setup() {
        Brand brand = new Brand(1L, "O Boticario", new ArrayList<>());
        perfume = new Perfume(1L,"Malbec", "Muito bom", BigDecimal.valueOf(129), 5, Genre.MASCULINE, Concentration.EDP, brand,null);
    }

    @Nested
    @DisplayName("registerMovement()")
    class RegisterMovements {

        @Test
        @DisplayName("Should increase stock when movement type is INBOUND")
        void shouldIncreaseStockOnInBound() {
            StockMovementRequestDto requestDto = new StockMovementRequestDto(1L, "INBOUND", 5);
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
            when(perfumeRepository.save(any(Perfume.class))).thenReturn(perfume);

            StockMovement savedMovement = new StockMovement();
            savedMovement.setId(100L);
            savedMovement.setMovementType(MovementType.INBOUND);
            savedMovement.setPerfume(perfume);
            savedMovement.setQuantity(5);
            savedMovement.setResultingBalance(10);

            when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(savedMovement);


            StockMovementResponseDto result = stockMovementService.registerMovement(requestDto);

            assertThat(result.id()).isEqualTo(100L);
            assertThat(result.quantity()).isEqualTo(5);
            assertThat(result.newStockBalance()).isEqualTo(10);
            assertThat(result.perfumeId()).isEqualTo(1L);

            ArgumentCaptor<StockMovement> captor = ArgumentCaptor.forClass(StockMovement.class);
            verify(stockMovementRepository).save(captor.capture());

            StockMovement captured = captor.getValue();
            assertThat(captured.getResultingBalance()).isEqualTo(10);
            assertThat(captured.getQuantity()).isEqualTo(5);
            assertThat(captured.getPerfume()).isEqualTo(perfume);
            assertThat(captured.getMovementType()).isEqualTo(MovementType.INBOUND);

            ArgumentCaptor<Perfume> perfumeCaptor = ArgumentCaptor.forClass(Perfume.class);
            verify(perfumeRepository).save(perfumeCaptor.capture());
            assertThat(perfumeCaptor.getValue().getQuantityOfStock()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should decrease stock when movement type is OUTBOUND")
        void shouldDecreaseStockWhenOnOutBound() {
            StockMovementRequestDto requestDto  = new StockMovementRequestDto(1L, "OUTBOUND", 2);
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
            when(perfumeRepository.save(any(Perfume.class))).thenReturn(perfume);

            StockMovement savedMovement = new StockMovement();
            savedMovement.setId(100L);
            savedMovement.setPerfume(perfume);
            savedMovement.setMovementType(MovementType.OUTBOUND);
            savedMovement.setQuantity(2);
            savedMovement.setResultingBalance(3);

            when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(savedMovement);

            StockMovementResponseDto result = stockMovementService.registerMovement(requestDto);

            assertThat(result.id()).isEqualTo(100L);
            assertThat(result.perfumeId()).isEqualTo(1L);
            assertThat(result.quantity()).isEqualTo(2);
            assertThat(result.newStockBalance()).isEqualTo(3);

            ArgumentCaptor<StockMovement> captor = ArgumentCaptor.forClass(StockMovement.class);
            verify(stockMovementRepository).save(captor.capture());

            StockMovement stockCaptor = captor.getValue();
            assertThat(stockCaptor.getResultingBalance()).isEqualTo(3);
            assertThat(stockCaptor.getQuantity()).isEqualTo(2);
            assertThat(stockCaptor.getPerfume()).isEqualTo(perfume);
            assertThat(stockCaptor.getMovementType()).isEqualTo(MovementType.OUTBOUND);

            ArgumentCaptor<Perfume> perfumeCaptor = ArgumentCaptor.forClass(Perfume.class);
            verify(perfumeRepository).save(perfumeCaptor.capture());
            assertThat(perfumeCaptor.getValue().getQuantityOfStock()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should adjustment stock when movement type is ADJUSTMENT")
        void shouldAdjustmentStock() {
            StockMovementRequestDto requestDto = new StockMovementRequestDto(1L, "ADJUSTMENT", 12);
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
            when(perfumeRepository.save(any(Perfume.class))).thenReturn(perfume);

            StockMovement savedMovement = new StockMovement();
            savedMovement.setId(100L);
            savedMovement.setPerfume(perfume);
            savedMovement.setQuantity(12);
            savedMovement.setMovementType(MovementType.ADJUSTMENT);
            savedMovement.setResultingBalance(12);

            when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(savedMovement);

            StockMovementResponseDto result = stockMovementService.registerMovement(requestDto);
            assertThat(result.id()).isEqualTo(100L);
            assertThat(result.perfumeId()).isEqualTo(1L);
            assertThat(result.quantity()).isEqualTo(12);
            assertThat(result.newStockBalance()).isEqualTo(12);

            ArgumentCaptor<StockMovement> captor = ArgumentCaptor.forClass(StockMovement.class);
            verify(stockMovementRepository).save(captor.capture());

            StockMovement stockCaptor = captor.getValue();
            assertThat(stockCaptor.getResultingBalance()).isEqualTo(12);
            assertThat(stockCaptor.getQuantity()).isEqualTo(12);
            assertThat(stockCaptor.getPerfume()).isEqualTo(perfume);
            assertThat(stockCaptor.getMovementType()).isEqualTo(MovementType.ADJUSTMENT);

            ArgumentCaptor<Perfume> perfumeCaptor = ArgumentCaptor.forClass(Perfume.class);
            verify(perfumeRepository).save(perfumeCaptor.capture());
            assertThat(perfumeCaptor.getValue().getQuantityOfStock()).isEqualTo(12);
        }

        @Test
        @DisplayName("Should throw exception when not found perfume by ID")
        void shouldThrowExceptionWhenNotFoundPerfume() {
            StockMovementRequestDto requestDto = new StockMovementRequestDto(1L, "INBOUND", 2);
            when(perfumeRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> stockMovementService.registerMovement(requestDto));

            verify(perfumeRepository, never()).save(any());
            verify(stockMovementRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when insufficient stock")
        void shouldThrowExceptionWhenInsufficientStock() {
            StockMovementRequestDto requestDto = new StockMovementRequestDto(1L, "OUTBOUND", 10);
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));

            assertThrows(ResourceConflictException.class, () -> stockMovementService.registerMovement(requestDto));

            verify(perfumeRepository, never()).save(any(Perfume.class));
            verify(stockMovementRepository, never()).save(any(StockMovement.class));
        }
    }
}