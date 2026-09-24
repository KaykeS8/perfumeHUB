package com.simao.perfumehub.controllers;

import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteRequestDto;
import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteResponseDto;
import com.simao.perfumehub.services.PerfumeNoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfumes/{perfumeId}/notes")
public class PerfumeNoteController {

    private final PerfumeNoteService perfumeNoteService;

    public PerfumeNoteController(PerfumeNoteService perfumeNoteService) {
        this.perfumeNoteService = perfumeNoteService;
    }

    @PostMapping
    public ResponseEntity<PerfumeNoteResponseDto> createPerfumeNote(@PathVariable Long perfumeId, @RequestBody @Valid PerfumeNoteRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfumeNoteService.createPerfumeNote(perfumeId, dto));
    }

    @GetMapping
    public ResponseEntity<List<PerfumeNoteResponseDto>> getAll(@PathVariable Long perfumeId) {
        return ResponseEntity.status(HttpStatus.OK).body(perfumeNoteService.getAll(perfumeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfumeNote(@PathVariable Long perfumeId, @PathVariable Long id) {
        perfumeNoteService.deletePerfumeNote(perfumeId, id);
        return ResponseEntity.noContent().build();
    }

}
