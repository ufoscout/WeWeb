package com.weweb.core.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.weweb.BaseTest;
import com.weweb.core.json.JacksonJsonSerializerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class JWTServiceJJWTTest extends BaseTest {

	private final long expireMinutes = 2;
	private JwtServiceJJWT jwtService;

	@Before
	public void setUp() {
		jwtService = new JwtServiceJJWT("secret", SignatureAlgorithm.HS512, expireMinutes, new JacksonJsonSerializerService());
	}

    @Test
    public void shouldGenerateAndParseCustomBeans() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.from = "from-" + UUID.randomUUID();
        message.subject = "sub-" + UUID.randomUUID();
        message.sentDate = new Date();

        final String jwt = jwtService.generate(message);
        getLogger().info("Generated JWT:\n{}", jwt);

        final String parsed = jwtService.getAllClaimsFromToken(jwt).get(JwtServiceJJWT.PAYLOAD_CLAIM_KEY, String.class);
        getLogger().info("Parsed JWT:\n{}", parsed);
        assertNotNull(parsed);
        assertFalse(parsed.isEmpty());

        final SimpleMailMessage parsedMessage = jwtService.parse(jwt, SimpleMailMessage.class);
        assertNotNull(parsedMessage);
        assertEquals( message.from, parsedMessage.from );
        assertEquals( message.subject, parsedMessage.subject );
        assertEquals( message.sentDate, parsedMessage.sentDate );
    }

    @Test
    public void shouldSetTheExpirationDate() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.from = "from-" + UUID.randomUUID();
        message.subject = "sub-" + UUID.randomUUID();
        message.sentDate = new Date();

        long beforeTime = new Date().getTime() - 1000;
        final String jwt = jwtService.generate(message);
        getLogger().info("Generated JWT:\n{}", jwt);

        long afterTime = new Date().getTime() + 1000;
        Claims claims = jwtService.getAllClaimsFromToken(jwt);

        long issuedTime = claims.getIssuedAt().getTime();
        assertTrue( issuedTime >= beforeTime );
        assertTrue( issuedTime <= afterTime );

        long expireTime = claims.getExpiration().getTime();
        assertEquals( issuedTime + (expireMinutes * 60 * 1000), expireTime );
    }


    @Test(expected=SignatureException.class)
    public void shouldFailParsingTamperedJwt() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.from = "from-" + UUID.randomUUID();
        message.subject = "sub-" + UUID.randomUUID();
        message.sentDate = new Date();

        final String jwt = jwtService.generate(message);
        getLogger().info("Generated JWT:\n{}", jwt);

        jwtService.parse(jwt + 1, String.class);
    }

    @Test(expected=TokenExpiredException.class)
    public void shouldFailParsingExpiredBeans() {
        final SimpleMailMessage userContext = new SimpleMailMessage();
        final String JWT = jwtService.generate("", userContext, new Date(), new Date(System.currentTimeMillis() -1 ));
        jwtService.parse(JWT, SimpleMailMessage.class);
    }

    @Test
    public void shouldAcceptNotExpiredBeans() {
        final SimpleMailMessage userContext = new SimpleMailMessage();
        final String jwt = jwtService.generate(userContext);
        assertNotNull(jwtService.parse(jwt, SimpleMailMessage.class));
    }


    public static class SimpleMailMessage {

        public String from;
        public Date sentDate;
        public String subject;

    }

}
