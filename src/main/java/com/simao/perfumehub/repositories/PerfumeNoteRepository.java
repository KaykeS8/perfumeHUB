package com.simao.perfumehub.repositories;

import com.simao.perfumehub.entities.PerfumeNote;
import com.simao.perfumehub.entities.enums.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumeNoteRepository extends JpaRepository<PerfumeNote, Long> {
    boolean existsByPerfumeIdAndNoteIdAndType(Long perfumeId, Long noteId, NoteType type);
    List<PerfumeNote> findAllByPerfumeId(Long id);
}
