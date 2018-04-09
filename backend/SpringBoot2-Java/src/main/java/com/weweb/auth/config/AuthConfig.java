package com.weweb.auth.config;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Configuration
public class AuthConfig {

    private String jwtTokenHeader = "Authorization";
    private String jwtSecret = "mySecret";
    private SignatureAlgorithm jwtSignatureAlgorithm = SignatureAlgorithm.HS512;
    private long jwtExpirationMinutes = 60 * 3;

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

}
