package com.weweb.auth.web;

import static org.springframework.http.MediaType.APPLICATION_JSON;

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
        return errorResponse(new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Fatal error: " + uuid));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
        log.error(getMessage(exception), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
        log.warn(getMessage(exception));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<ErrorDetails> errorResponse(ErrorDetails error) {
        log.error(error.getMessage());
        return ResponseEntity
                .status(error.getCode())
                .contentType(APPLICATION_JSON)
                .body(error);
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
