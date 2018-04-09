package com.weweb.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CoreProperties {

    @Value("${core.jwt.secret}")
    private String secret;
    @Value("${core.jwt.signatureAlgorithm:HS512}")
    private String signatureAlgorithm ;
    @Value("${core.jwt.tokenValidityMinutes}")
    private long tokenValidityMinutes;

}
