package com.weweb.auth.web;

import com.ufoscout.coreutils.jwt.JwtService;
import com.weweb.auth.config.AuthContants;
import com.weweb.auth.dto.LoginDto;
import com.weweb.auth.dto.LoginResponseDto;
import com.weweb.auth.model.UserContext;
import com.weweb.auth.service.User;
import com.weweb.auth.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthContants.BASE_AUTH_API)
public class TestAuthenticationController {

    private final UserService userService;
    private final JwtService jwtService;

    TestAuthenticationController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/test/public")
    public UserContext testPublic(UserContext userContext) {
        return userContext;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test/authenticated")
    public UserContext testAuthenticated(UserContext userContext) {
        return userContext;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/test/protected")
    public UserContext testProtected(UserContext userContext) {
        return userContext;
    }

}
