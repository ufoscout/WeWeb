package com.weweb.auth.web;

import com.ufoscout.coreutils.auth.Auth;
import com.ufoscout.coreutils.jwt.JwtService;
import com.weweb.auth.config.AuthContants;
import com.weweb.auth.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthContants.BASE_UM_API)
public class TestAuthenticationController {

    private final UserService userService;
    private final JwtService jwtService;

    TestAuthenticationController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/test/public")
    public Auth testPublic(Auth userContext) {
        return userContext;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test/authenticated")
    public Auth testAuthenticated(Auth userContext) {
        return userContext;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/test/protected")
    public Auth testProtected(Auth userContext) {
        return userContext;
    }

}
