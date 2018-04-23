package com.weweb.core.jwt

import com.weweb.core.config.JwtConfig
import com.weweb.core.json.JsonSerializerService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock
import java.util.*
import kotlin.reflect.KClass

/**
 * Implementation of the [JwtService] based on JJWT
 *
 * @author Francesco Cina'
 */
class JwtServiceJJWT(jwtConfig: JwtConfig,
                     private val jsonSerializerService: JsonSerializerService) : JwtService {

    private val secret: String;
    private val signatureAlgorithm: SignatureAlgorithm;
    private val tokenValidityMinutes: Long;

    companion object {
        internal val PAYLOAD_CLAIM_KEY = "payload"
    }

    init {
        secret = jwtConfig.secret
        signatureAlgorithm = SignatureAlgorithm.forName(jwtConfig.signatureAlgorithm)
        tokenValidityMinutes = jwtConfig.tokenValidityMinutes
    }

    private val clock = DefaultClock.INSTANCE

    override fun generate(payload: Any): String {
        return generate("", payload)
    }

    override fun generate(subject: String, payload: Any): String {
        val createdDate = clock.now()
        return generate(subject, payload, createdDate, calculateExpirationDate(createdDate))
    }

    override fun generate(subject: String, payload: Any, createdDate: Date, expirationDate: Date): String {
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
