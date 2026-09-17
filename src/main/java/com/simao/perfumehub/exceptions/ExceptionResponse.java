package com.simao.perfumehub.exceptions;

import java.time.LocalDateTime;

public record ExceptionResponse(
        LocalDateTime timestamp,
        Object message,
        String details,
        int status
) {
}
