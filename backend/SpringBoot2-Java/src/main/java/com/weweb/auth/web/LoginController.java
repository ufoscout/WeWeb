package com.weweb.auth.web;

import com.weweb.auth.config.AuthContants;
import com.weweb.auth.config.UserContext;
import com.weweb.auth.dto.LoginDto;
import com.weweb.auth.dto.LoginResponseDto;
import com.weweb.auth.service.User;
import com.weweb.auth.service.UserService;
import com.weweb.core.jwt.JwtService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthContants.BASE_AUTH_API)
public class LoginController {

    private final UserService userService;
    private final JwtService jwtService;

    LoginController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto loginDto) {

        User login = userService.login(loginDto.getUsername(), loginDto.getPassword());
        String[] roles = new String[login.getRoles().size()];
        String token = jwtService.generate(login.getUsername(),
                new UserContext(login.getUsername(), login.getRoles().toArray(roles)));

        return new LoginResponseDto(token);
    }

    @GetMapping("/test/public")
    public UserContext testPublic(UserContext userContext) {
        return userContext;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test/protected")
    public UserContext testProtected(UserContext userContext) {
        return userContext;
    }

}
