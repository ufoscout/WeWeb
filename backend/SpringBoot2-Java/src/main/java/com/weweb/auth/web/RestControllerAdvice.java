package com.weweb.auth.web;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.weweb.core.jwt.TokenExpiredException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + " : " + getMessage(exception), exception);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Fatal error: " + uuid);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception) {
        log.warn(getMessage(exception), exception);
        return response(HttpStatus.FORBIDDEN, "AccessDenied");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException exception) {
        log.warn(getMessage(exception));
        return response(HttpStatus.FORBIDDEN, "BadCredentials");
    }

    private ResponseEntity<ErrorDetails> response(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDetails(status.value(), message));
    }

    private String getMessage(Exception exception) {
        String message = exception.getMessage();
        return message != null ? message : "";
    }

    @Value
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    public static class ErrorDetails {
        private int code;
        private String message;
    }

}
