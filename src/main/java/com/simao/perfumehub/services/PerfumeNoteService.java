package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteRequestDto;
import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteResponseDto;
import com.simao.perfumehub.entities.OlfactoryNote;
import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.entities.PerfumeNote;
import com.simao.perfumehub.entities.enums.NoteType;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.PerfumeNoteMapper;
import com.simao.perfumehub.repositories.OlfactoryNoteRepository;
import com.simao.perfumehub.repositories.PerfumeNoteRepository;
import com.simao.perfumehub.repositories.PerfumeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfumeNoteService {

    private final PerfumeNoteRepository perfumeNoteRepository;
    private final PerfumeRepository perfumeRepository;
    private final OlfactoryNoteRepository olfactoryNoteRepository;
    private final Logger log = LoggerFactory.getLogger(PerfumeNoteService.class);
    private final PerfumeNoteMapper perfumeNoteMapper;

    public PerfumeNoteService(
            PerfumeNoteRepository perfumeNoteRepository,
            PerfumeRepository perfumeRepository,
            PerfumeNoteMapper perfumeNoteMapper,
            OlfactoryNoteRepository olfactoryNoteRepository
    ) {
        this.perfumeNoteRepository = perfumeNoteRepository;
        this.perfumeRepository = perfumeRepository;
        this.perfumeNoteMapper = perfumeNoteMapper;
        this.olfactoryNoteRepository = olfactoryNoteRepository;
    }

    @Transactional
    public PerfumeNoteResponseDto createPerfumeNote(Long perfumeId, PerfumeNoteRequestDto dto) {
        log.info("Creating note of perfume...");
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfume not found with ID: " + perfumeId));

        OlfactoryNote olfactoryNote = olfactoryNoteRepository.findById(dto.noteId())
                .orElseThrow(() -> new ResourceNotFoundException("Olfactory note not found with ID: " + dto.noteId()));
        if (perfumeNoteRepository.existsByPerfumeIdAndNoteIdAndType(perfumeId, dto.noteId(), NoteType.valueOf(dto.type()))) {
            throw new ResourceConflictException("This note with this type is already associated with the perfume");
        }

        PerfumeNote perfumeNote = perfumeNoteMapper.toEntity(dto);
        perfumeNote.setNote(olfactoryNote);
        perfumeNote.setPerfume(perfume);
        return perfumeNoteMapper.toDto(perfumeNoteRepository.save(perfumeNote));
    }

    public List<PerfumeNoteResponseDto> getAll(Long perfumeId) {
        log.info("Get all notes of perfume: ");
        if (!perfumeRepository.existsById(perfumeId)) throw new ResourceNotFoundException("Perfume not found with ID: " + perfumeId);
        return perfumeNoteRepository.findAllByPerfumeId(perfumeId).stream().map(perfumeNoteMapper::toDto).toList();
    }

    public void deletePerfumeNote(Long perfumeId, Long id) {
        log.info("Deleting perfume note with ID: {}", id);
        if (!perfumeRepository.existsById(perfumeId)) {
            throw new ResourceNotFoundException("Perfume not found with ID: " + perfumeId);
        }
        if (!perfumeNoteRepository.existsByIdAndPerfumeId(perfumeId, id))  {
            throw new ResourceNotFoundException("Perfume note not found with ID: " + id + " for perfume: " + perfumeId);
        }
        perfumeNoteRepository.deleteById(id);
    }

}
