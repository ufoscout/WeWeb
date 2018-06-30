package com.weweb.auth.web;

import com.ufoscout.coreutils.auth.Auth;
import com.ufoscout.coreutils.jwt.JwtService;
import com.weweb.auth.config.AuthConfig;
import com.weweb.auth.config.AuthContants;
import com.weweb.auth.dto.CreateUserDto;
import com.weweb.auth.dto.LoginDto;
import com.weweb.auth.dto.LoginResponseDto;
import com.weweb.auth.dto.TokenResponseDto;
import com.weweb.auth.service.User;
import com.weweb.auth.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthContants.BASE_UM_API)
public class AuthenticationController {

    public static final String CURRENT_USER_AUTH_URL = "/current";
    private final UserService userService;
    private final JwtService jwtService;

    AuthenticationController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto loginDto) {

        User login = userService.login(loginDto.getUsername(), loginDto.getPassword());
        String[] roles = new String[login.getRoles().size()];
        Auth auth = new Auth(login.getId(), login.getUsername(), login.getRoles().toArray(roles));
        String token = jwtService.generate(login.getUsername(), auth );

        return new LoginResponseDto(token, auth);
    }

    @PostMapping("/create")
    public String createUser(@RequestBody CreateUserDto dto) {
        userService.createUser(dto);
        return "";
    }

    @GetMapping(AuthContants.CURRENT_USER_AUTH_URL)
    public LoginResponseDto current(@RequestHeader(AuthConfig.JWT_TOKEN_HEADER_KEY) String tokenHeader) {
        try {
            if (tokenHeader != null && tokenHeader.startsWith(AuthConfig.JWT_TOKEN_HEADER_SUFFIX)) {
                String authToken = tokenHeader.substring(AuthConfig.JWT_TOKEN_HEADER_SUFFIX.length());
                Auth userContext = jwtService.parse(authToken, Auth.class);
                return new LoginResponseDto(authToken, userContext);
            }
            return new LoginResponseDto("", new Auth());
        } catch (RuntimeException e) {
            return new LoginResponseDto("", new Auth());
        }
    }

    @GetMapping("/token/refresh")
    public TokenResponseDto refreshToken(Auth userContext) {
        return new TokenResponseDto(jwtService.generate(userContext.getUsername(), userContext));
    }
}
