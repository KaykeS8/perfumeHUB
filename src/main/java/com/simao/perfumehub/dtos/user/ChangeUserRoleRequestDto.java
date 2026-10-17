package com.simao.perfumehub.dtos.user;

import com.simao.perfumehub.entities.enums.Role;
import com.simao.perfumehub.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ChangeUserRoleRequestDto(
        @NotBlank
        @ValueOfEnum(enumClass = Role.class, message = "Role should be one of the following values: ADMIN, EMPLOYEE, CUSTOMER")
        @Schema(description = "Role of user", example = "EMPLOYEE")
        String role
) {
}
