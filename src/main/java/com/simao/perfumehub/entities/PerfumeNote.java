package com.simao.perfumehub.entities;

import com.simao.perfumehub.entities.enums.NoteType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(
    name = "tb_perfume_notes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"perfume_id", "node_id","type"})
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PerfumeNote implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfume_id", nullable = false)
    private Perfume perfume;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "note_id", nullable = false)
    private OlfactoryNote note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteType type;

}
