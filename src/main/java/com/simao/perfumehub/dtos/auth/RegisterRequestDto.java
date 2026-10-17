package com.simao.perfumehub.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank
        @Schema(description = "name of user", example = "alan turing", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotBlank @Email
        @Schema(description = "email of user", example = "alan.turing@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
        @Schema(description = "password of user", example = "12345678", requiredMode = Schema.RequiredMode.REQUIRED)
        String password

) {
}
