package com.simao.perfumehub.dtos.auth;

import com.simao.perfumehub.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto(
        @Schema(description = "user identifier", example = "1")
        Long id,

        @Schema(description = "user name", example = "alan turing")
        String name,

        @Schema(description = "email of user", example = "alan.turing@gmail.com")
        String email,

        @Schema(description = "role of user", example = "ADMIN")
        Role role
) {
}
