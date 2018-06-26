package com.weweb.auth.web;

import com.ufoscout.coreutils.jwt.JwtService;
import com.weweb.BaseIT;
import com.weweb.auth.config.AuthConfig;
import com.weweb.auth.config.AuthContants;
import com.weweb.auth.dto.LoginDto;
import com.weweb.auth.dto.LoginResponseDto;
import com.weweb.auth.model.UserContext;
import com.weweb.auth.web.RestControllerAdvice.ErrorDetails;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.Date;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class AuthenticationControllerIT extends BaseIT {

    @Autowired
    private JwtService jwt;
    @Autowired
    private AuthConfig authConfig;


    @Test
    public void shouldGetUnauthorizedWithAnonymousUser() throws Exception {
        ResponseEntity<ErrorDetails> response = restTemplate
                .getForEntity(AuthContants.BASE_AUTH_API + "/test/authenticated", ErrorDetails.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("AccessDenied", response.getBody().getMessage());
    }

    @Test
    public void shouldGetUnauthorizedWithAnonymousUserOnProtectedUri() throws Exception {
        ResponseEntity<ErrorDetails> response = restTemplate
                .getForEntity(AuthContants.BASE_AUTH_API + "/test/protected", ErrorDetails.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("AccessDenied", response.getBody().getMessage());
    }

    @Test
    public void shouldSuccessfulAccessAuthenticatedApiWithToken() throws Exception {

        UserContext sentUserContext = new UserContext();
        sentUserContext.setUsername(UUID.randomUUID().toString());
        sentUserContext.setRoles(new String[]{"ADMIN", "OTHER"});

        String token = jwt.generate(sentUserContext);

        HttpHeaders headers = new HttpHeaders();
        headers.add(authConfig.getJwtHeaderKey(), "Bearer " + token);
        ResponseEntity<UserContext> response =
                restTemplate.exchange(AuthContants.BASE_AUTH_API + "/test/authenticated", HttpMethod.GET, new HttpEntity<>(headers), UserContext.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserContext receivedUserContext = response.getBody();
        assertNotNull(receivedUserContext);
        assertEquals(sentUserContext.getUsername(), receivedUserContext.getUsername());
    }


    @Test
    public void shouldAccessPublicUriWithAnonymousUser() throws Exception {

        ResponseEntity<UserContext> response = restTemplate
                .getForEntity(AuthContants.BASE_AUTH_API + "/test/public", UserContext.class);

        UserContext userContext = response.getBody();
        assertNotNull(userContext);
        assertTrue(userContext.getRoles().length == 0);
        assertTrue(userContext.getUsername().isEmpty());
    }


    @Test
    public void shouldSuccessfulLoginWithValidCredentials() throws Exception {

        LoginDto loginDto = new LoginDto("user", "user");

        ResponseEntity<LoginResponseDto> response = restTemplate
                .postForEntity(AuthContants.BASE_AUTH_API + "/login", loginDto, LoginResponseDto.class);

        LoginResponseDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertNotNull(responseDto.getToken());
        UserContext userContext = jwt.parse(responseDto.getToken(), UserContext.class);
        assertEquals("user", userContext.getUsername());
        assertEquals(1, userContext.getRoles().length);
        assertEquals("USER", userContext.getRoles()[0]);
    }

    @Test
    public void shouldFailLoginWithWrongCredentials() throws Exception {

        LoginDto loginDto = new LoginDto("user", UUID.randomUUID().toString());

        ResponseEntity<ErrorDetails> response = restTemplate
                .postForEntity(AuthContants.BASE_AUTH_API + "/login", loginDto, ErrorDetails.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("BadCredentials", response.getBody().getMessage());
    }


    @Test
    public void shouldNotAccessProtectedApiWithoutAdminRole() throws Exception {

        UserContext sentUserContext = new UserContext();
        sentUserContext.setUsername(UUID.randomUUID().toString());

        String token = jwt.generate(sentUserContext);

        HttpHeaders headers = new HttpHeaders();
        headers.add(authConfig.getJwtHeaderKey(), "Bearer " + token);
        ResponseEntity<ErrorDetails> response = restTemplate.exchange(AuthContants.BASE_AUTH_API + "/test/protected", HttpMethod.GET, new HttpEntity<>(headers), ErrorDetails.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("AccessDenied", response.getBody().getMessage());

    }

    @Test
    public void shouldSuccessfulAccessProtectedApiWithAdminRole() throws Exception {

        UserContext sentUserContext = new UserContext();
        sentUserContext.setUsername(UUID.randomUUID().toString());
        sentUserContext.setRoles(new String[]{"ADMIN"});

        String token = jwt.generate(sentUserContext);

        HttpHeaders headers = new HttpHeaders();
        headers.add(authConfig.getJwtHeaderKey(), "Bearer " + token);
        ResponseEntity<UserContext> response = restTemplate.exchange(AuthContants.BASE_AUTH_API + "/test/protected", HttpMethod.GET, new HttpEntity<>(headers), UserContext.class);

        UserContext receivedUserContext = response.getBody();
        assertNotNull(receivedUserContext);
        assertEquals(sentUserContext.getUsername(), receivedUserContext.getUsername());
    }

    @Test
    public void shouldGetTokenExpiredExceptionIfTokenNotValid() throws Exception {

        UserContext sentUserContext = new UserContext();
        sentUserContext.setUsername(UUID.randomUUID().toString());

        String token = jwt.generate("", sentUserContext, new Date(System.currentTimeMillis()-1000), new Date(System.currentTimeMillis()-1000));

        HttpHeaders headers = new HttpHeaders();
        headers.add(authConfig.getJwtHeaderKey(), "Bearer " + token);
        ResponseEntity<ErrorDetails> response = restTemplate.exchange(AuthContants.BASE_AUTH_API + "/test/protected", HttpMethod.GET, new HttpEntity<>(headers), ErrorDetails.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("TokenExpired", response.getBody().getMessage());

    }
}

