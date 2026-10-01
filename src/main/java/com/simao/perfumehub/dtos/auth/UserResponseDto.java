package com.simao.perfumehub.dtos.auth;

import com.simao.perfumehub.entities.enums.Role;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Role role
) {
}
