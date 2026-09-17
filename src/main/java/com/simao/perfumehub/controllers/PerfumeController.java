package com.simao.perfumehub.controllers;

import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.services.PerfumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perfumes")
public class PerfumeController {

    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @PostMapping
    public ResponseEntity<Perfume> createPerfume() {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfumeService.createPerfume());
    }

}