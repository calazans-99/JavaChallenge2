package br.com.fiap.universidade_fiap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        Instant timestamp,
        String path,
        int status,
        String error,
        String message,
        Object details
) {
    public static ApiError of(String path, int status, String error, String message) {
        return new ApiError(Instant.now(), path, status, error, message, null);
    }

    public static ApiError of(String path, int status, String error, String message, Object details) {
        return new ApiError(Instant.now(), path, status, error, message, details);
    }
}
