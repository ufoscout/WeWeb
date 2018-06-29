package com.weweb.auth.web;

import com.ufoscout.coreutils.jwt.JwtService;
import com.weweb.BaseIT;
import com.weweb.auth.config.AuthConfig;
import com.weweb.auth.config.AuthContants;
import com.weweb.auth.dto.LoginDto;
import com.weweb.auth.dto.LoginResponseDto;
import com.weweb.auth.dto.TokenResponseDto;
import com.weweb.auth.model.UserContext;
import com.weweb.core.web.ErrorDetails;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.Date;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


public class AuthenticationControllerIT extends BaseIT {

    @Autowired
    private JwtService jwt;

    @Test
    public void shouldGetUnauthorizedWithAnonymousUser() throws Exception {
        ResponseEntity<ErrorDetails> response = restTemplate
                .getForEntity(AuthContants.BASE_UM_API + "/test/authenticated", ErrorDetails.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("AccessDenied", response.getBody().getMessage());
    }

    @Test
    public void shouldGetUnauthorizedWithAnonymousUserOnProtectedUri() throws Exception {
        ResponseEntity<ErrorDetails> response = restTemplate
                .getForEntity(AuthContants.BASE_UM_API + "/test/protected", ErrorDetails.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("AccessDenied", response.getBody().getMessage());
    }

    @Test
    public void shouldSuccessfulAccessAuthenticatedApiWithToken() throws Exception {

        UserContext sentUserContext = new UserContext("", new String[0]);
        sentUserContext.setUsername(UUID.randomUUID().toString());
        sentUserContext.setRoles(new String[]{"ADMIN", "OTHER"});

        String token = jwt.generate(sentUserContext);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + token);
        ResponseEntity<UserContext> response =
                restTemplate.exchange(AuthContants.BASE_UM_API + "/test/authenticated", HttpMethod.GET, new HttpEntity<>(headers), UserContext.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserContext receivedUserContext = response.getBody();
        assertNotNull(receivedUserContext);
        assertEquals(sentUserContext.getUsername(), receivedUserContext.getUsername());
    }


    @Test
    public void shouldAccessPublicUriWithAnonymousUser() throws Exception {

        ResponseEntity<UserContext> response = restTemplate
                .getForEntity(AuthContants.BASE_UM_API + "/test/public", UserContext.class);

        UserContext userContext = response.getBody();
        assertNotNull(userContext);
        assertTrue(userContext.getRoles().length == 0);
        assertTrue(userContext.getUsername().isEmpty());
    }


    @Test
    public void shouldSuccessfulLoginWithValidCredentials() throws Exception {

        LoginDto loginDto = new LoginDto("user", "user");

        ResponseEntity<LoginResponseDto> response = restTemplate
                .postForEntity(AuthContants.BASE_UM_API + "/login", loginDto, LoginResponseDto.class);

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
                .postForEntity(AuthContants.BASE_UM_API + "/login", loginDto, ErrorDetails.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("BadCredentials", response.getBody().getMessage());
    }


    @Test
    public void shouldNotAccessProtectedApiWithoutAdminRole() throws Exception {

        UserContext sentUserContext = new UserContext("", new String[0]);
        sentUserContext.setUsername(UUID.randomUUID().toString());

        String token = jwt.generate(sentUserContext);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + token);
        ResponseEntity<ErrorDetails> response = restTemplate.exchange(AuthContants.BASE_UM_API + "/test/protected", HttpMethod.GET, new HttpEntity<>(headers), ErrorDetails.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("AccessDenied", response.getBody().getMessage());

    }

    @Test
    public void shouldSuccessfulAccessProtectedApiWithAdminRole() throws Exception {

        UserContext sentUserContext = new UserContext("", new String[0]);
        sentUserContext.setUsername(UUID.randomUUID().toString());
        sentUserContext.setRoles(new String[]{"ADMIN"});

        String token = jwt.generate(sentUserContext);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + token);
        ResponseEntity<UserContext> response = restTemplate.exchange(AuthContants.BASE_UM_API + "/test/protected", HttpMethod.GET, new HttpEntity<>(headers), UserContext.class);

        UserContext receivedUserContext = response.getBody();
        assertNotNull(receivedUserContext);
        assertEquals(sentUserContext.getUsername(), receivedUserContext.getUsername());
    }

    @Test
    public void shouldGetTokenExpiredExceptionIfTokenNotValid() throws Exception {

        UserContext sentUserContext = new UserContext("", new String[0]);
        sentUserContext.setUsername(UUID.randomUUID().toString());

        String token = jwt.generate("", sentUserContext, new Date(System.currentTimeMillis()-1000), new Date(System.currentTimeMillis()-1000));

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + token);
        ResponseEntity<ErrorDetails> response = restTemplate.exchange(AuthContants.BASE_UM_API + "/test/protected", HttpMethod.GET, new HttpEntity<>(headers), ErrorDetails.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("TokenExpired", response.getBody().getMessage());

    }

    @Test
    public void shouldReturnCurrentAuthLinkedWithTheToken() {

        UserContext user = new UserContext("", new String[0]);
        user.setUsername(UUID.randomUUID().toString());
        String tokenString = jwt.generate(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + tokenString);

        ResponseEntity<LoginResponseDto> response = restTemplate
                .exchange(AuthContants.BASE_UM_API + "/current", HttpMethod.GET, new HttpEntity<>(headers), LoginResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponseDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(tokenString, responseDto.getToken());
        assertEquals(user.getUsername(), responseDto.getAuth().getUsername());
    }

    @Test
    public void shouldReturnEmptyAuthIfBadToken() {

        UserContext user = new UserContext(UUID.randomUUID().toString(), new String[0]);
        user.setUsername(UUID.randomUUID().toString());
        String tokenString = UUID.randomUUID().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + tokenString);

        ResponseEntity<LoginResponseDto> response = restTemplate
                .exchange(AuthContants.BASE_UM_API + "/current", HttpMethod.GET, new HttpEntity<>(headers), LoginResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponseDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals("", responseDto.getToken());
        assertEquals("", responseDto.getAuth().getUsername());
    }

    @Test
    public void shouldReturnEmptyAuthIfExpiredToken() {

        UserContext user = new UserContext(UUID.randomUUID().toString(), new String[0]);
        user.setUsername(UUID.randomUUID().toString());
        String tokenString = jwt.generate("", user, new Date(System.currentTimeMillis()-1000), new Date(System.currentTimeMillis()-1000));

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + tokenString);

        ResponseEntity<LoginResponseDto> response = restTemplate
                .exchange(AuthContants.BASE_UM_API + "/current", HttpMethod.GET, new HttpEntity<>(headers), LoginResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponseDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals("", responseDto.getToken());
        assertEquals("", responseDto.getAuth().getUsername());
    }

    @Test
    public void shouldReturnNewToken() {

        UserContext user = new UserContext("", new String[0]);
        user.setUsername(UUID.randomUUID().toString());
        String tokenString = jwt.generate("", user, new Date(System.currentTimeMillis()-1000), new Date(System.currentTimeMillis()+11000));

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + tokenString);

        ResponseEntity<TokenResponseDto> response = restTemplate
                .exchange(AuthContants.BASE_UM_API + "/token/refresh", HttpMethod.GET, new HttpEntity<>(headers), TokenResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        TokenResponseDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertNotEquals(tokenString, responseDto.getToken());
        assertEquals(user.getUsername(), jwt.parse(responseDto.getToken(), UserContext.class).getUsername());
    }

    @Test
    public void shouldNotRefreshTokenIfExpired() {

        UserContext user = new UserContext("", new String[0]);
        user.setUsername(UUID.randomUUID().toString());
        String tokenString = jwt.generate("", user, new Date(System.currentTimeMillis()-1000), new Date(System.currentTimeMillis()-11000));

        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConfig.JWT_TOKEN_HEADER_KEY, AuthConfig.JWT_TOKEN_HEADER_SUFFIX + tokenString);

        ResponseEntity<ErrorDetails> response = restTemplate
                .exchange(AuthContants.BASE_UM_API + "/token/refresh", HttpMethod.GET, new HttpEntity<>(headers), ErrorDetails.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}

