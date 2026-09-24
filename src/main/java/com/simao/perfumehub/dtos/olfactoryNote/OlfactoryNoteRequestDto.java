package com.simao.perfumehub.dtos.olfactoryNote;

import jakarta.validation.constraints.NotBlank;

public record OlfactoryNoteRequestDto(@NotBlank String name) {
}