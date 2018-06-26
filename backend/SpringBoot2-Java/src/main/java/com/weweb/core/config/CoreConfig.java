package com.weweb.core.config;

import com.ufoscout.coreutils.json.JacksonJsonSerializerService;
import com.ufoscout.coreutils.json.JsonSerializerService;
import com.ufoscout.coreutils.jwt.CoreJsonProvider;
import com.ufoscout.coreutils.jwt.JwtConfig;
import com.ufoscout.coreutils.jwt.JwtService;
import com.ufoscout.coreutils.jwt.JwtServiceJJWT;
import com.ufoscout.coreutils.validation.SimpleValidatorService;
import com.ufoscout.coreutils.validation.ValidatorService;
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
                new JwtConfig(
                        coreProperties.getSecret(),
                        coreProperties.getSignatureAlgorithm(),
                        coreProperties.getTokenValidityMinutes()),
                new CoreJsonProvider(jsonSerializerService())
        );
    }

    @Bean
    public ValidatorService validatorService() {
        return new SimpleValidatorService();
    }
}
