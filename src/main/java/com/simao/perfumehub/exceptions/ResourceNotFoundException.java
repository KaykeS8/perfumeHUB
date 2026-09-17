package com.simao.perfumehub.exceptions;

import org.springframework.http.HttpStatus;

public class BrandException extends RuntimeException {

    private HttpStatus status;

    public BrandException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
