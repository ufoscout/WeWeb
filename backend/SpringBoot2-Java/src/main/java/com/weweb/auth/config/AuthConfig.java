package com.weweb.auth.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Configuration
public class AuthConfig {

    public final static String JWT_TOKEN_HEADER_KEY = "Authorization";
    public final static String AUTH_USER_CONTEXT_ATTRIBUTE_KEY = "[Auth] UserContextAttributeKey";
    public static final String JWT_TOKEN_HEADER_SUFFIX = "Bearer ";

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserContextResolver userContextResolver() {
        return new UserContextResolver(AUTH_USER_CONTEXT_ATTRIBUTE_KEY);
    }
}
