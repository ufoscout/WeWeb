package com.weweb.auth.service;

import static org.junit.Assert.*;

import com.ufoscout.coreutils.validation.SimpleValidatorService;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class InMemoryUserServiceTest {

    InMemoryUserService service = new InMemoryUserService(new BCryptPasswordEncoder(), new SimpleValidatorService());

    @Test
    public void shouldReturnUser() {
        User user = service.login("user", "user");
        assertNotNull(user);
        assertEquals("user", user.getUsername());
        assertNotEquals("user", user.getEncodedPassword());
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldThrowBadCredentialException() {
        service.login("user", "admin");
    }

}