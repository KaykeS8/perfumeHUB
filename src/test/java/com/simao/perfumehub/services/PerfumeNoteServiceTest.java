package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteRequestDto;
import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteResponseDto;
import com.simao.perfumehub.entities.Brand;
import com.simao.perfumehub.entities.OlfactoryNote;
import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.entities.PerfumeNote;
import com.simao.perfumehub.entities.enums.Concentration;
import com.simao.perfumehub.entities.enums.Genre;
import com.simao.perfumehub.entities.enums.NoteType;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.PerfumeNoteMapper;
import com.simao.perfumehub.repositories.OlfactoryNoteRepository;
import com.simao.perfumehub.repositories.PerfumeNoteRepository;
import com.simao.perfumehub.repositories.PerfumeRepository;
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
@DisplayName("PerfumeNote service - Test unit")
public class PerfumeNoteServiceTest {

    @Mock
    private PerfumeNoteRepository perfumeNoteRepository;

    @Mock
    private PerfumeRepository perfumeRepository;

    @Mock
    private OlfactoryNoteRepository olfactoryNoteRepository;

    @Mock
    private PerfumeNoteMapper mapper;

    @InjectMocks
    private PerfumeNoteService perfumeNoteService;

    private Perfume perfume;
    private OlfactoryNote note;
    private PerfumeNoteRequestDto requestDto;
    private PerfumeNoteResponseDto responseDto;

    @BeforeEach
    void setup() {
        perfume = new Perfume(1L,"Malbec", "Muito bom", BigDecimal.valueOf(129), 5, Genre.MASCULINE, Concentration.EDP, new Brand(),null);
        note = new OlfactoryNote(1L, "Baunilha", new ArrayList<>());
        requestDto = new PerfumeNoteRequestDto(1L, "BASE");
        responseDto = new PerfumeNoteResponseDto(1L, 1L, "Baunilha", NoteType.BASE);
    }

    @Nested
    @DisplayName("createPerfumeNote()")
    class CreatePerfumeNote {

        @Test
        @DisplayName("Should associate note to perfume successfully")
        void shouldCreatePerfumeNoteToPerfume() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
            when(olfactoryNoteRepository.findById(1L)).thenReturn(Optional.of(note));
            when(perfumeNoteRepository.existsByPerfumeIdAndNoteIdAndType(1L, 1L, NoteType.BASE)).thenReturn(false);

            PerfumeNote perfumeNote = new PerfumeNote();
            perfumeNote.setId(1L);
            perfumeNote.setType(NoteType.BASE);

            when(mapper.toEntity(requestDto)).thenReturn(perfumeNote);
            when(perfumeNoteRepository.save(perfumeNote)).thenReturn(perfumeNote);
            when(mapper.toDto(perfumeNote)).thenReturn(responseDto);

            PerfumeNoteResponseDto result = perfumeNoteService.createPerfumeNote(1L, requestDto);
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.type()).isEqualTo(NoteType.BASE);
            assertThat(result.noteId()).isEqualTo(1L);
            assertThat(result.noteName()).isEqualTo("Baunilha");

            ArgumentCaptor<PerfumeNote> captor = ArgumentCaptor.forClass(PerfumeNote.class);
            verify(perfumeNoteRepository).save(captor.capture());

            PerfumeNote noteCaptor = captor.getValue();
            assertThat(noteCaptor.getNote()).isEqualTo(note);
            assertThat(noteCaptor.getPerfume()).isEqualTo(perfume);
            assertThat(noteCaptor.getType()).isEqualTo(NoteType.BASE);
        }

        @Test
        @DisplayName("Should throw exception when perfume not found")
        void shouldThrowExceptionWhenPerfumeNotFound() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> perfumeNoteService.createPerfumeNote(1L, requestDto));

            verify(perfumeRepository).findById(1L);
            verify(perfumeNoteRepository, never()).save(any(PerfumeNote.class));
        }

        @Test
        @DisplayName("Should throw exception when olfactory note not found")
        void shouldThrowExceptionWhenOlfactoryNoteNotFound() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
            when(olfactoryNoteRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> perfumeNoteService.createPerfumeNote(1L, requestDto));

            verify(perfumeRepository).findById(1L);
            verify(olfactoryNoteRepository).findById(1L);
            verify(perfumeNoteRepository, never()).save(any(PerfumeNote.class));
        }

        @Test
        @DisplayName("Should throw exception when association already exists")
        void shouldThrowExceptionWhenAssociationAlreadyExists() {
            when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
            when(olfactoryNoteRepository.findById(1L)).thenReturn(Optional.of(note));
            when(perfumeNoteRepository.existsByPerfumeIdAndNoteIdAndType(1L, 1L, NoteType.BASE)).thenReturn(true);

            assertThrows(ResourceConflictException.class, () -> perfumeNoteService.createPerfumeNote(1L, requestDto));

            verify(perfumeRepository).findById(1L);
            verify(olfactoryNoteRepository).findById(1L);
            verify(perfumeNoteRepository).existsByPerfumeIdAndNoteIdAndType(1L, 1L, NoteType.BASE);
            verify(perfumeNoteRepository, never()).save(any(PerfumeNote.class));
        }
    }

    @Nested
    @DisplayName("deletePerfumeNote()")
    class DeletePerfumeNote {

        @Test
        @DisplayName("Should delete perfume note by id")
        void shouldDeletePerfumeNoteById() {
            when(perfumeRepository.existsById(1L)).thenReturn(true);
            when(perfumeNoteRepository.existsByIdAndPerfumeId(1L, 1L)).thenReturn(true);
            doNothing().when(perfumeNoteRepository).deleteById(1L);

            perfumeNoteService.deletePerfumeNote(1L, 1L);

            verify(perfumeNoteRepository).deleteById(1L);
            verify(perfumeRepository).existsById(1L);
            verify(perfumeNoteRepository).existsByIdAndPerfumeId(1L, 1L);
        }

        @Test
        @DisplayName("Should throw exception when not found perfume")
        void shouldThrowExceptionWhenNotFoundPerfume() {
            when(perfumeRepository.existsById(1L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> perfumeNoteService.deletePerfumeNote(1L, 1L));

            verify(perfumeRepository).existsById(1L);
            verify(perfumeNoteRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Should throw exception when note does not belongs to perfume")
        void shouldThrowExceptionWhenNoteDoesNotBelongToPerfume() {
            when(perfumeRepository.existsById(1L)).thenReturn(true);
            when(perfumeNoteRepository.existsByIdAndPerfumeId(1L, 1L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> perfumeNoteService.deletePerfumeNote(1L, 1L));

            verify(perfumeRepository).existsById(1L);
            verify(perfumeNoteRepository).existsByIdAndPerfumeId(1L, 1L);
            verify(perfumeNoteRepository, never()).deleteById(any());
        }
    }
}
