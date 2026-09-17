package com.simao.perfumehub.services;

import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.repositories.PerfumeRepository;
import org.springframework.stereotype.Service;

@Service
public class PerfumeService {

    private final PerfumeRepository  perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public Perfume createPerfume() {
        return new Perfume();
    }
}
