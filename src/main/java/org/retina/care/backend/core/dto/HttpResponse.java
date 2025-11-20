package org.retina.care.backend.core.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class HttpResponse<T> {
    private final String message;
    private final String error;
    private final Integer status;
    private final T data;
    private final LocalDateTime timestamp;

    public HttpResponse(String message, String error, Integer status, T data) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> HttpResponse<T> Ok(String message, T data) {
        return new HttpResponse<>(
                message,
                null,
                HttpStatus.OK.value(),
                data
        );
    }

    public static <T> HttpResponse<T> Created(String message, T data) {
        return new HttpResponse<>(
                message,
                null,
                HttpStatus.CREATED.value(),
                data
        );
    }

    public static <T> HttpResponse<T> NotFound(String message, String error) {
        return new HttpResponse<>(
                message,
                error,
                HttpStatus.NOT_FOUND.value(),
                null
        );
    }

    public static <T> HttpResponse<T> BadRequest(String message, String error) {
        return new HttpResponse<>(
                message,
                error,
                HttpStatus.BAD_REQUEST.value(),
                null
        );
    }

    public static <T> HttpResponse<T> Unauthorized(String message, String error) {
        return new HttpResponse<>(
                message,
                error,
                HttpStatus.UNAUTHORIZED.value(),
                null
        );
    }

    public static <T> HttpResponse<T> Forbidden(String message, String error) {
        return new HttpResponse<>(
                message,
                error,
                HttpStatus.FORBIDDEN.value(),
                null
        );
    }
}