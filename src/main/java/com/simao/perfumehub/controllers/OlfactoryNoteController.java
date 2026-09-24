package com.simao.perfumehub.controllers;

import com.simao.perfumehub.dtos.brand.PaginationDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteRequestDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteResponseDto;
import com.simao.perfumehub.services.OlfactoryNoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class OlfactoryNoteController {

    private final OlfactoryNoteService olfactoryNoteService;

    public OlfactoryNoteController(OlfactoryNoteService olfactoryNoteService) {
        this.olfactoryNoteService = olfactoryNoteService;
    }

    @PostMapping
    public ResponseEntity<OlfactoryNoteResponseDto> createOlfactoryNote(@RequestBody @Valid OlfactoryNoteRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(olfactoryNoteService.createOlfactoryNote(dto));
    }

    @GetMapping
    public ResponseEntity<PaginationDto<OlfactoryNoteResponseDto>> getAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(olfactoryNoteService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OlfactoryNoteResponseDto> findOlfactoryNoteById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(olfactoryNoteService.findOlfactoryNoteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OlfactoryNoteResponseDto> updateOlfactoryNote(@PathVariable Long id, @RequestBody @Valid OlfactoryNoteRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(olfactoryNoteService.updateOlfactoryNote(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOlfactoryNote(@PathVariable Long id) {
        olfactoryNoteService.deleteOlfactoryNote(id);
        return ResponseEntity.noContent().build();
    }
}