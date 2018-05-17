package com.weweb.core.jwt

import com.ufoscout.vertxk.kodein.json.JacksonJsonSerializerService
import com.weweb.BaseTest
import com.weweb.core.config.JwtConfig
import io.jsonwebtoken.SignatureException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class JWTServiceJJWTTest : BaseTest() {

    private val expireMinutes: Long = 2
    private var jwtService: JwtServiceJJWT? = null

    @BeforeEach
    fun setUp() {
        jwtService = JwtServiceJJWT(
                JwtConfig("secret", "HS512", expireMinutes), JacksonJsonSerializerService())
    }

    @Test
    fun shouldGenerateAndParseCustomBeans() {
        val message = SimpleMailMessage()
        message.from = "from-" + UUID.randomUUID()
        message.subject = "sub-" + UUID.randomUUID()
        message.sentDate = Date()

        val jwt = jwtService!!.generate(message)
        logger().info("Generated JWT:\n{}", jwt)

        val parsed = jwtService!!.getAllClaimsFromToken(jwt).get(JwtServiceJJWT.PAYLOAD_CLAIM_KEY, String::class.java)
        logger().info("Parsed JWT:\n{}", parsed)
        assertNotNull(parsed)
        assertFalse(parsed.isEmpty())

        val parsedMessage = jwtService!!.parse(jwt, SimpleMailMessage::class)
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
        logger().info("Generated JWT:\n{}", jwt)

        val afterTime = Date().time + 1000
        val claims = jwtService!!.getAllClaimsFromToken(jwt)

        val issuedTime = claims.issuedAt.time
        assertTrue(issuedTime >= beforeTime)
        assertTrue(issuedTime <= afterTime)

        val expireTime = claims.expiration.time
        assertEquals(issuedTime + expireMinutes * 60 * 1000, expireTime)
    }


    @Test
    fun shouldFailParsingTamperedJwt() {
        assertThrows<SignatureException> {

            val message = SimpleMailMessage()
            message.from = "from-" + UUID.randomUUID()
            message.subject = "sub-" + UUID.randomUUID()
            message.sentDate = Date()

            val jwt = jwtService!!.generate(message)
            logger().info("Generated JWT:\n{}", jwt)

            jwtService!!.parse<String>(jwt + 1)
        }
    }

    @Test
    fun shouldFailParsingExpiredBeans() {
        assertThrows<TokenExpiredException> {
            val userContext = SimpleMailMessage()
            val JWT = jwtService!!.generate("", userContext, Date(), Date(System.currentTimeMillis() - 1))
            jwtService!!.parse(JWT, SimpleMailMessage::class)
        }
    }

    @Test
    fun shouldAcceptNotExpiredBeans() {
        val userContext = SimpleMailMessage()
        val jwt = jwtService!!.generate(userContext)
        assertNotNull(jwtService!!.parse<SimpleMailMessage>(jwt))
    }


    class SimpleMailMessage {

        var from: String? = null
        var sentDate: Date? = null
        var subject: String? = null

    }

}
