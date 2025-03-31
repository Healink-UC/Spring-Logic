package com.healink.integrador.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ApiValidationError extends ApiError {
    private final Map<String, String> validationErrors;

    public ApiValidationError(HttpStatus status, String error, String message, LocalDateTime timestamp,
            Map<String, String> validationErrors) {
        super(status, error, message, timestamp);
        this.validationErrors = validationErrors;
    }
}