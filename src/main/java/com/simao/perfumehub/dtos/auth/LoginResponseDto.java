package com.simao.perfumehub.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(
        @Schema(description = "token of access", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3OTIyNDc0OTIsImV4cCI6MTgyMzc4MzQ5MiwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.G2DtbZJOmPSs1d5ft_xJl3k-JworOWWa014uZ_XRqbo")
        String accessToken,

        @Schema(description = "type of token", example = "Bearer")
        String tokenType,

        @Schema(description = "expiration token in seconds", example = "12000")
        long expiresInSeconds
) {
}
