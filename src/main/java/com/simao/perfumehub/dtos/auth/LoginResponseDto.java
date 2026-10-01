package com.simao.perfumehub.dtos.auth;

public record LoginResponseDto(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
}
