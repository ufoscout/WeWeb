package com.weweb.auth.web;


import static org.assertj.core.api.Assertions.assertThat;

import com.weweb.BaseMockitoTest;
import com.weweb.auth.web.RestControllerAdvice.ErrorDetails;
import com.weweb.core.jwt.TokenExpiredException;
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

}