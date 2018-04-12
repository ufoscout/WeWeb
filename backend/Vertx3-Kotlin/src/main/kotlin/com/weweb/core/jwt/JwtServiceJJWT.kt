package com.weweb.core.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock
import com.weweb.core.json.JsonSerializerService
import java.util.*
import kotlin.reflect.KClass

/**
 * Implementation of the [JwtService] based on JJWT
 *
 * @author Francesco Cina'
 */
class JwtServiceJJWT(private val secret: String,
                     private val signatureAlgorithm: SignatureAlgorithm,
                     private val tokenValidityMinutes: Long,
                     private val jsonSerializerService: JsonSerializerService) : JwtService {

    companion object {
        internal val PAYLOAD_CLAIM_KEY = "payload"
    }

    private val clock = DefaultClock.INSTANCE

    override fun generate(payload: Any): String {
        return generate("", payload)
    }

    override fun generate(subject: String, payload: Any): String {
        val createdDate = clock.now()
        return generate(subject, payload, createdDate, calculateExpirationDate(createdDate))
    }

    fun generate(subject: String, payload: Any, createdDate: Date, expirationDate: Date): String {
        return Jwts.builder()
                .setSubject(subject)
                .claim(PAYLOAD_CLAIM_KEY, jsonSerializerService.toJson(payload))
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, secret)
                .compact()
    }

    override fun <T: Any> parse(jwt: String, payloadClass: KClass<T>): T {
        val claims = getAllClaimsFromToken(jwt)
        return jsonSerializerService.fromJson(payloadClass, claims[PAYLOAD_CLAIM_KEY] as String)
    }

    private fun calculateExpirationDate(createdDate: Date): Date {
        return Date(createdDate.time + tokenValidityMinutes * 60 * 1000)
    }

    internal fun getAllClaimsFromToken(token: String): Claims {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body
        } catch (e: ExpiredJwtException) {
            throw TokenExpiredException()
        }

    }

}
