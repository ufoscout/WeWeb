package com.weweb.core.config;

import com.weweb.core.json.JacksonJsonSerializerService;
import com.weweb.core.json.JsonSerializerService;
import com.weweb.core.jwt.JwtService;
import com.weweb.core.jwt.JwtServiceJJWT;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    private final CoreProperties coreProperties;

    public CoreConfig(CoreProperties coreProperties) {
        this.coreProperties = coreProperties;
    }

    @Bean
    public JsonSerializerService jsonSerializerService() {
        return new JacksonJsonSerializerService();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtServiceJJWT(
                coreProperties.getSecret(),
                SignatureAlgorithm.forName(coreProperties.getSignatureAlgorithm()),
                coreProperties.getTokenValidityMinutes(),
                jsonSerializerService()
        );
    }

}
