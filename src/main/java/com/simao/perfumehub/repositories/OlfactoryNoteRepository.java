package com.simao.perfumehub.repositories;

import com.simao.perfumehub.entities.OlfactoryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OlfactoryNoteRepository extends JpaRepository<OlfactoryNote, Long> {
    boolean existsByNameIgnoreCase(String name);
}
