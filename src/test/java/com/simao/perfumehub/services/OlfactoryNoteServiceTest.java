package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteRequestDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteResponseDto;
import com.simao.perfumehub.entities.OlfactoryNote;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.OlfactoryNoteMapper;
import com.simao.perfumehub.repositories.OlfactoryNoteRepository;
import org.junit.jupiter.api.BeforeEach;
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
@DisplayName("Olfactory note service - Test unit")
public class OlfactoryNoteServiceTest {

    @Mock
    private OlfactoryNoteRepository olfactoryNoteRepository;

    @Mock
    private OlfactoryNoteMapper olfactoryNoteMapper;

    @InjectMocks
    private OlfactoryNoteService olfactoryNoteService;

    private OlfactoryNote entity;
    private OlfactoryNoteRequestDto requestDto;
    private OlfactoryNoteResponseDto responseDto;

    @BeforeEach
    void setUp() {
         entity = new OlfactoryNote(1L, "Baunilha", new ArrayList<>());
         requestDto = new OlfactoryNoteRequestDto("Baunilha");
         responseDto = new OlfactoryNoteResponseDto(1L, "Baunilha");
    }


    @Nested
    @DisplayName("createOlfactoryNote()")
    class CreateOlfactoryNote {

        @Test
        @DisplayName("Should create a note when name is unique")
        void shouldCreateOlfactoryNote() {
            when(olfactoryNoteRepository.existsByNameIgnoreCase("Baunilha")).thenReturn(false);
            when(olfactoryNoteMapper.toEntity(requestDto)).thenReturn(entity);
            when(olfactoryNoteRepository.save(entity)).thenReturn(entity);
            when(olfactoryNoteMapper.toDto(entity)).thenReturn(responseDto);

            OlfactoryNoteResponseDto result = olfactoryNoteService.createOlfactoryNote(requestDto);

            assertThat(result).isEqualTo(responseDto);

            verify(olfactoryNoteRepository).save(any(OlfactoryNote.class));
        }

        @Test
        @DisplayName("Should throw exception when olfactory note name already exist")
        void shouldThrowExceptionWhenOlfactoryNoteNameAlreadyExist() {
            when(olfactoryNoteRepository.existsByNameIgnoreCase("Baunilha")).thenReturn(true);

            assertThrows(ResourceConflictException.class, () -> olfactoryNoteService.createOlfactoryNote(requestDto));

            verify(olfactoryNoteRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("findOlfactoryNoteById()")
    class FindOlfactoryNoteById {

        @Test
        @DisplayName("Should return olfactory note when find by id")
        void shouldFindOlfactoryNoteById() {
            Long id = 1L;
            when(olfactoryNoteRepository.findById(id)).thenReturn(Optional.of(entity));
            when(olfactoryNoteMapper.toDto(entity)).thenReturn(responseDto);

            OlfactoryNoteResponseDto result = olfactoryNoteService.findOlfactoryNoteById(id);

            assertThat(result).isEqualTo(responseDto);

            verify(olfactoryNoteRepository).findById(id);
            verify(olfactoryNoteMapper).toDto(entity);
        }

        @Test
        @DisplayName("Should throw exception when not found olfactory note ID")
        void shouldThrowExceptionWhenNotFoundOlfactoryNoteById() {
            Long id = 1L;
            when(olfactoryNoteRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> olfactoryNoteService.findOlfactoryNoteById(id));

            verify(olfactoryNoteMapper, never()).toDto(entity);
        }
    }

    @Nested
    @DisplayName("updateOlfactoryNote()")
    class UpdateOlfactoryNote {

        @Test
        @DisplayName("Should update olfactory note with data")
        void shouldUpdateOlfactoryNoteWithData() {
            Long id = 1L;
            OlfactoryNoteRequestDto request = new OlfactoryNoteRequestDto("Limao");
            OlfactoryNoteResponseDto response = new OlfactoryNoteResponseDto(1L, "Limao");

            when(olfactoryNoteRepository.findById(id)).thenReturn(Optional.of(entity));
            when(olfactoryNoteRepository.existsByNameIgnoreCase("Limao")).thenReturn(false);
            when(olfactoryNoteRepository.save(any(OlfactoryNote.class))).thenReturn(entity);
            when(olfactoryNoteMapper.toDto(entity)).thenReturn(response);

            OlfactoryNoteResponseDto result = olfactoryNoteService.updateOlfactoryNote(id, request);

            assertThat(result).isEqualTo(response);

            verify(olfactoryNoteRepository).findById(id);
            verify(olfactoryNoteRepository).save(any(OlfactoryNote.class));
            verify(olfactoryNoteMapper).toDto(entity);
        }

        @Test
        @DisplayName("Should throw exception when olfactory note name already exists")
        void shouldThrowExceptionWhenOlfactoryNoteNameAlreadyExists() {
            Long id = 1L;
            OlfactoryNoteRequestDto request = new OlfactoryNoteRequestDto("Limao");

            when(olfactoryNoteRepository.findById(id)).thenReturn(Optional.of(entity));
            when(olfactoryNoteRepository.existsByNameIgnoreCase("Limao")).thenReturn(true);

            assertThrows(ResourceConflictException.class, () -> olfactoryNoteService.updateOlfactoryNote(id, request));

            verify(olfactoryNoteRepository).existsByNameIgnoreCase("Limao");
            verify(olfactoryNoteRepository, never()).save(any(OlfactoryNote.class));
        }

        @Test
        @DisplayName("Should throw exception when not found olfactory note by id")
        void shouldThrowExceptionWhenNotFoundOlfactoryNoteById() {
            Long id = 1L;
            OlfactoryNoteRequestDto request = new OlfactoryNoteRequestDto("Limao");

            when(olfactoryNoteRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> olfactoryNoteService.updateOlfactoryNote(id, request));

            verify(olfactoryNoteRepository, never()).save(any(OlfactoryNote.class));
        }
    }

    @Nested
    @DisplayName("deleteOlfactoryNote()")
    class DeleteOlfactoryNote {

        @Test
        @DisplayName("Should delete olfactory note by id")
        void shouldDeleteOlfactoryNoteById() {
            Long id = 1L;

            when(olfactoryNoteRepository.existsById(id)).thenReturn(true);
            doNothing().when(olfactoryNoteRepository).deleteById(id);

            olfactoryNoteService.deleteOlfactoryNote(id);

            verify(olfactoryNoteRepository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Should throw exception when not found olfactory note by id")
        void shouldThrowExceptionWhenNotFoundOlfactoryNoteById() {
            Long id = 1L;
            when(olfactoryNoteRepository.existsById(id)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> olfactoryNoteService.deleteOlfactoryNote(id));

            verify(olfactoryNoteRepository, never()).deleteById(any());
        }
    }

}
