package com.weweb.auth.web

import com.weweb.auth.exception.BadCredentialsException
import com.weweb.auth.exception.UnauthenticatedException
import com.weweb.auth.exception.UnauthorizedException
import com.weweb.core.exception.WebException
import com.weweb.core.exception.WebExceptionService
import com.weweb.core.exception.registerTransformer
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthExceptionController(val webExceptionService: WebExceptionService): CoroutineVerticle() {

    override suspend fun start() {

        webExceptionService.registerTransformer<UnauthenticatedException>({ex -> WebException(code = 401, message = "Unauthenticated")})
        webExceptionService.registerTransformer<BadCredentialsException>({ex -> WebException(code = 401, message = "BadCredentials")})
        webExceptionService.registerTransformer<UnauthorizedException>({ex -> WebException(code = 403, message = "AccessDenied")})

    }

}