package com.weweb.core.web;


import static org.assertj.core.api.Assertions.assertThat;

import com.ufoscout.coreutils.validation.ValidationException;
import com.ufoscout.coreutils.validation.ValidationResult;
import com.ufoscout.coreutils.validation.ValidationResultImpl;
import com.weweb.BaseMockitoTest;

import java.util.UUID;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

public class RestControllerAdviceTest extends BaseMockitoTest {

    @InjectMocks
    private RestControllerAdvice advice;

    @Test
    public void handleException() throws Exception {
        // Arrange
        String message = UUID.randomUUID().toString();
        Exception exception = new Exception(message);
        // Act
        ResponseEntity<ErrorDetails> response = advice.handleException(exception);
        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMessage()).contains("Error code:");
    }

    @Test
    public void handleAccessDeniedException() throws Exception {
        // Arrange
        String message = UUID.randomUUID().toString();
        AccessDeniedException exception = new AccessDeniedException(message);
        // Act
        ResponseEntity<ErrorDetails> response = advice.handleAccessDeniedException(exception);
        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void handleBadCredentialsException() throws Exception {
        // Arrange
        String message = UUID.randomUUID().toString();
        BadCredentialsException exception = new BadCredentialsException(message);
        // Act
        ResponseEntity<ErrorDetails> response = advice.handleBadCredentialsException(exception);
        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void handleValidationException() throws Exception {
        // Arrange
        String message = UUID.randomUUID().toString();
        ValidationResultImpl<?> validationResult = new ValidationResultImpl<Object>(new Object());
        validationResult.addViolation("key", "errorForKey");
        ValidationException exception = new ValidationException(validationResult);
        // Act
        ResponseEntity<ErrorDetails> response = advice.handleValidationException(exception);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(422);
        assertThat(response.getBody().getDetails().size()).isEqualTo(1);
        assertThat(response.getBody().getDetails().get("key").get(0)).isEqualTo("errorForKey");
    }

}