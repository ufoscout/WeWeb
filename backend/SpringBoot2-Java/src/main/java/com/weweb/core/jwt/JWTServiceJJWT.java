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
 * Implementation of the {@link JWTService} based on JJWT
 *
 * @author Francesco Cina'
 *
 */
public class JWTServiceJJWT implements JWTService {

    final static String PAYLOAD_CLAIM_KEY = "payload";
    private final Clock clock = DefaultClock.INSTANCE;
    private final SignatureAlgorithm signatureAlgorithm;
    private final JsonSerializerService jsonSerializerService;
    private final JWTConfig config;

    public JWTServiceJJWT(final JWTConfig config, final JsonSerializerService jsonSerializerService) {
        this.config = config;
        this.jsonSerializerService = jsonSerializerService;
        signatureAlgorithm = SignatureAlgorithm.forName(config.getSignatureAlgorithm());
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

    <T> String generate(final String subject, final T payload, Date createdDate, Date expirationDate) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(PAYLOAD_CLAIM_KEY, jsonSerializerService.toJson(payload))
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, config.getSecret())
                .compact();
    }

    @Override
    public <T> T parse(final String jwt, final Class<T> payloadClass) {
        final Claims claims = getAllClaimsFromToken(jwt);
        return jsonSerializerService.fromJson(payloadClass, (String) claims.get(PAYLOAD_CLAIM_KEY));
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + config.getTokenValidityMinutes() * 1000);
    }

    Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(config.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }
    }

}
