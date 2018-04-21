package com.weweb.auth.service

import com.weweb.auth.context.AuthContext
import com.weweb.auth.context.UserContext
import com.weweb.core.jwt.JwtService
import io.vertx.core.http.HttpServerRequest

class AuthContextServiceImpl(val jwtService: JwtService) : AuthContextService {

    val TOKEN_HEADER = "Authorization"
    val TOKEN_SUFFIX = "Bearer "
    val TOKEN_SUFFIX_LENGTH = TOKEN_SUFFIX.length

    override fun get(request: HttpServerRequest): AuthContext {
        val header = request.getHeader(TOKEN_HEADER);
        if (header!=null && header.startsWith(TOKEN_SUFFIX)) {
            val userContext = jwtService.parse(header.substring(TOKEN_SUFFIX_LENGTH), UserContext::class)
            return AuthContext(userContext, mapOf())
        }
        return AuthContext(UserContext("", arrayOf()), mapOf())
    }

}