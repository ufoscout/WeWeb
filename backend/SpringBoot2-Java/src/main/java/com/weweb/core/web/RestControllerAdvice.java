package com.weweb.core.web;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ufoscout.coreutils.validation.ValidationException;
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
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Error code: " + uuid, Collections.emptyMap());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception) {
        log.warn(getMessage(exception), exception);
        return response(HttpStatus.FORBIDDEN, "AccessDenied", Collections.emptyMap());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException exception) {
        log.warn(getMessage(exception));
        return response(HttpStatus.FORBIDDEN, "BadCredentials", Collections.emptyMap());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(ValidationException exception) {
        log.warn(getMessage(exception));
        return response(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(), exception.getViolations());
    }

    private ResponseEntity<ErrorDetails> response(HttpStatus status, String message, Map<String, List<String>> details) {
        return ResponseEntity
                .status(status)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDetails(status.value(), message, details));
    }

    private String getMessage(Exception exception) {
        String message = exception.getMessage();
        return message != null ? message : "";
    }

}