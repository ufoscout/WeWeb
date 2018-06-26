package com.weweb.auth.web;

import com.ufoscout.coreutils.jwt.JwtService;
import com.weweb.auth.config.AuthContants;
import com.weweb.auth.dto.CreateUserDto;
import com.weweb.auth.dto.LoginDto;
import com.weweb.auth.dto.LoginResponseDto;
import com.weweb.auth.model.UserContext;
import com.weweb.auth.service.User;
import com.weweb.auth.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthContants.BASE_AUTH_API)
public class AuthenticationController {

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
        String token = jwtService.generate(login.getUsername(),
                new UserContext(login.getUsername(), login.getRoles().toArray(roles)));

        return new LoginResponseDto(token, login.getUsername());
    }

    @PostMapping("/create")
    public String createUser(@RequestBody CreateUserDto dto) {
        userService.createUser(dto);
        return "";
    }

}
