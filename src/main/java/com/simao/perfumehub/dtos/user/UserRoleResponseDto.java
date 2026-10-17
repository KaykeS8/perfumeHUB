package com.simao.perfumehub.dtos.user;

import com.simao.perfumehub.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserRoleResponseDto(
        @Schema(description = "user identifier", example = "1")
        Long userId,

        @Schema(description = "Role of user", example = "EMPLOYEE")
        Role role
) {
}
