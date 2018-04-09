package com.weweb.auth.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Configuration
public class AuthConfig {

    private final String jwtHeaderKey = "Authorization";
    private final String userContextAttributeKey = "[Auth] UserContextAttributeKey";

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserContextResolver userContextResolver() {
        return new UserContextResolver(getUserContextAttributeKey());
    }
}
