package com.weweb.core.jwt;

import lombok.Builder;
import lombok.Data;

@Data
public class JWTConfig {

    private String secret;
    private String signatureAlgorithm = "HS512";
    private long tokenValidityMinutes;

}