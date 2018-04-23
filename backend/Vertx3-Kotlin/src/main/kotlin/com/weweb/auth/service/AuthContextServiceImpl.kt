package com.weweb.auth.service

import com.weweb.auth.config.AuthContants
import com.weweb.auth.context.AuthContext
import com.weweb.auth.context.UserContext
import com.weweb.core.jwt.JwtService
import io.vertx.core.http.HttpServerRequest

class AuthContextServiceImpl(val jwtService: JwtService) : AuthContextService {

    override fun get(request: HttpServerRequest): AuthContext {
        val header = request.getHeader(AuthContants.JWT_TOKEN_HEADER);
        if (header!=null && header.startsWith(AuthContants.JWT_TOKEN_HEADER_SUFFIX)) {
            val userContext = jwtService.parse(header.substring(AuthContants.JWT_TOKEN_HEADER_SUFFIX.length), UserContext::class)
            return AuthContext(userContext, mapOf())
        }
        return AuthContext(UserContext("", arrayOf()), mapOf())
    }

}