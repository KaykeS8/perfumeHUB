package com.simao.perfumehub.repositories;

import com.simao.perfumehub.entities.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    boolean existsByNameIgnoreCase(String name);
}
