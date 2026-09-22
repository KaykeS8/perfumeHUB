package com.simao.perfumehub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OlfactoryNote extends JpaRepository<OlfactoryNote, Long> {
}
