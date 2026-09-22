package com.simao.perfumehub.services;

import com.simao.perfumehub.dtos.brand.PaginationDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteRequestDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteResponseDto;
import com.simao.perfumehub.entities.OlfactoryNote;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.mapper.OlfactoryNoteMapper;
import com.simao.perfumehub.mapper.PaginationMapper;
import com.simao.perfumehub.repositories.OlfactoryNoteRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OlfactoryNoteService {

    private final OlfactoryNoteRepository olfactoryNoteRepository;
    private final Logger log = LoggerFactory.getLogger(OlfactoryNoteService.class);
    private final OlfactoryNoteMapper mapper;

    public OlfactoryNoteService(OlfactoryNoteRepository olfactoryNoteRepository, OlfactoryNoteMapper mapper) {
        this.olfactoryNoteRepository = olfactoryNoteRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OlfactoryNoteResponseDto createOlfactoryNote(OlfactoryNoteRequestDto dto) {
        log.info("Creating olfactory Note...");
        if (olfactoryNoteRepository.existsByNameIgnoreCase(dto.name())) {
           throw new ResourceConflictException("Olfactory note already exist with name: " + dto.name());
        }
        OlfactoryNote olfactoryNote = mapper.toEntity(dto);
        return mapper.toDto(olfactoryNoteRepository.save(olfactoryNote));
    }


    public PaginationDto<OlfactoryNoteResponseDto> getAll(Pageable pageable) {
        log.info("Get all olfactory notes...");
        Page<OlfactoryNoteResponseDto> page = olfactoryNoteRepository.findAll(pageable).map(mapper::toDto);
        return PaginationMapper.buildPaginationDto(page);
    }

    public OlfactoryNoteResponseDto findOlfactoryNoteById(Long id) {
        log.info("Finding olfactory note by ID: {}", id);
        return olfactoryNoteRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Olfactory note not found with ID: " + id));
    }

    @Transactional
    public OlfactoryNoteResponseDto updateOlfactoryNote(Long id, OlfactoryNoteRequestDto dto) {
        log.info("Updating olfactory note by ID: {}", id);
        return olfactoryNoteRepository.findById(id)
                .map(note -> {
                    if (!note.getName().equalsIgnoreCase(dto.name()) && olfactoryNoteRepository.existsByNameIgnoreCase(dto.name())) {
                        throw new ResourceConflictException("Olfactory note already exist with name: " + dto.name());
                    }
                    note.setName(dto.name());
                    return mapper.toDto(olfactoryNoteRepository.save(note));
                }).orElseThrow(()  -> new ResourceNotFoundException("Olfactory note not found with ID: " + id));
    }


    public void deleteOlfactoryNote(Long id) {
        if (!olfactoryNoteRepository.existsById(id)) throw new ResourceNotFoundException("Olfactory note not found with ID: " + id);
        log.info("Deleting olfactory note by ID: {}", id);
        olfactoryNoteRepository.deleteById(id);
    }

}
