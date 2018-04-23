package com.weweb.core.jwt;

import com.weweb.core.json.JsonSerializerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import java.util.Date;

/**
 * Implementation of the {@link JwtService} based on JJWT
 *
 * @author Francesco Cina'
 *
 */
public class JwtServiceJJWT implements JwtService {

    final static String PAYLOAD_CLAIM_KEY = "payload";
    private final Clock clock = DefaultClock.INSTANCE;
    private final SignatureAlgorithm signatureAlgorithm;
    private String secret;
    private long tokenValidityMinutes;
    private final JsonSerializerService jsonSerializerService;

    public JwtServiceJJWT(String secret,
            SignatureAlgorithm signatureAlgorithm,
            long tokenValidityMinutes,
            final JsonSerializerService jsonSerializerService) {
        this.secret = secret;
        this.signatureAlgorithm = signatureAlgorithm;
        this.tokenValidityMinutes = tokenValidityMinutes;
        this.jsonSerializerService = jsonSerializerService;

    }

    @Override
    public <T> String generate(final T payload) {
        return generate("", payload);
    }

    @Override
    public <T> String generate(final String subject, final T payload) {
        final Date createdDate = clock.now();
        return generate(subject, payload, createdDate, calculateExpirationDate(createdDate));
    }

    @Override
    public <T> String generate(final String subject, final T payload, Date createdDate, Date expirationDate) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(PAYLOAD_CLAIM_KEY, jsonSerializerService.toJson(payload))
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, secret)
                .compact();
    }

    @Override
    public <T> T parse(final String jwt, final Class<T> payloadClass) {
        final Claims claims = getAllClaimsFromToken(jwt);
        return jsonSerializerService.fromJson(payloadClass, (String) claims.get(PAYLOAD_CLAIM_KEY));
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + tokenValidityMinutes * 60 * 1000);
    }

    Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }
    }

}
