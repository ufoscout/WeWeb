package io.vertx.starter.core.jwt

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.vertx.starter.BaseTest
import io.vertx.starter.core.json.JacksonJsonSerializerService
import java.util.Date
import java.util.UUID
import org.junit.Before
import org.junit.Test

class JWTServiceJJWTTest : BaseTest() {

    private val expireMinutes: Long = 2
    private var jwtService: JwtServiceJJWT? = null

    @Before
    fun setUp() {
        jwtService = JwtServiceJJWT("secret", SignatureAlgorithm.HS512, expireMinutes, JacksonJsonSerializerService())
    }

    @Test
    fun shouldGenerateAndParseCustomBeans() {
        val message = SimpleMailMessage()
        message.from = "from-" + UUID.randomUUID()
        message.subject = "sub-" + UUID.randomUUID()
        message.sentDate = Date()

        val jwt = jwtService!!.generate(message)
        getLogger().info("Generated JWT:\n{}", jwt)

        val parsed = jwtService!!.getAllClaimsFromToken(jwt).get(JwtServiceJJWT.PAYLOAD_CLAIM_KEY, String::class.java)
        getLogger().info("Parsed JWT:\n{}", parsed)
        assertNotNull(parsed)
        assertFalse(parsed.isEmpty())

        val parsedMessage = jwtService!!.parse(jwt, SimpleMailMessage::class.java)
        assertNotNull(parsedMessage)
        assertEquals(message.from, parsedMessage.from)
        assertEquals(message.subject, parsedMessage.subject)
        assertEquals(message.sentDate, parsedMessage.sentDate)
    }

    @Test
    fun shouldSetTheExpirationDate() {
        val message = SimpleMailMessage()
        message.from = "from-" + UUID.randomUUID()
        message.subject = "sub-" + UUID.randomUUID()
        message.sentDate = Date()

        val beforeTime = Date().time - 1000
        val jwt = jwtService!!.generate(message)
        getLogger().info("Generated JWT:\n{}", jwt)

        val afterTime = Date().time + 1000
        val claims = jwtService!!.getAllClaimsFromToken(jwt)

        val issuedTime = claims.issuedAt.time
        assertTrue(issuedTime >= beforeTime)
        assertTrue(issuedTime <= afterTime)

        val expireTime = claims.expiration.time
        assertEquals(issuedTime + expireMinutes * 60 * 1000, expireTime)
    }


    @Test(expected = SignatureException::class)
    fun shouldFailParsingTamperedJwt() {
        val message = SimpleMailMessage()
        message.from = "from-" + UUID.randomUUID()
        message.subject = "sub-" + UUID.randomUUID()
        message.sentDate = Date()

        val jwt = jwtService!!.generate(message)
        getLogger().info("Generated JWT:\n{}", jwt)

        jwtService!!.parse(jwt + 1, String::class.java)
    }

    @Test(expected = TokenExpiredException::class)
    fun shouldFailParsingExpiredBeans() {
        val userContext = SimpleMailMessage()
        val JWT = jwtService!!.generate("", userContext, Date(), Date(System.currentTimeMillis() - 1))
        jwtService!!.parse(JWT, SimpleMailMessage::class.java)
    }

    @Test
    fun shouldAcceptNotExpiredBeans() {
        val userContext = SimpleMailMessage()
        val jwt = jwtService!!.generate(userContext)
        assertNotNull(jwtService!!.parse(jwt, SimpleMailMessage::class.java))
    }


    class SimpleMailMessage {

        var from: String? = null
        var sentDate: Date? = null
        var subject: String? = null

    }

}
