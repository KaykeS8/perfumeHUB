package com.simao.perfumehub;

import com.simao.perfumehub.mapper.PerfumeNoteMapper;
import com.simao.perfumehub.repositories.OlfactoryNoteRepository;
import com.simao.perfumehub.repositories.PerfumeNoteRepository;
import com.simao.perfumehub.repositories.PerfumeRepository;
import com.simao.perfumehub.services.PerfumeNoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PerfumeNote service - Test unit")
public class PerfumeNoteServiceTest {

    @Mock
    private PerfumeNoteRepository perfumeNoteRepository;

    @Mock
    private PerfumeRepository perfumeRepository;

    @Mock
    private OlfactoryNoteRepository olfactoryNoteRepository;

    @Mock
    private PerfumeNoteMapper mapper;

    @InjectMocks
    private PerfumeNoteService perfumeNoteService;

    @BeforeEach
    void setup() {

    }

    @Nested
    @DisplayName("createPerfumeNote()")
    class CreatePerfumeNote {

        void should
    }
}
