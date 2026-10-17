package com.simao.perfumehub.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank @Email
        @Schema(description = "Email to login", example = "alan.turing@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank
        @Schema(description = "email to login", example = "12345678", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
