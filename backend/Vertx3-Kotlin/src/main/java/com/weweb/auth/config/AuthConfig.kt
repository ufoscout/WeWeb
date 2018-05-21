package com.weweb.auth.config

import com.ufoscout.coreutils.auth.UnauthenticatedException
import com.ufoscout.coreutils.auth.UnauthorizedException
import com.ufoscout.coreutils.jwt.TokenExpiredException
import com.ufoscout.vertxk.kodein.VertxKComponent
import com.ufoscout.vertxk.kodein.router.WebException
import com.ufoscout.vertxk.kodein.router.WebExceptionService
import com.ufoscout.vertxk.kodein.router.registerTransformer
import com.weweb.auth.exception.BadCredentialsException

class AuthConfig(val webExceptionService: WebExceptionService): VertxKComponent {

    override suspend fun start() {
        webExceptionService.registerTransformer<UnauthenticatedException>({ ex -> WebException(code = 401, message = "NotAuthenticated") })
        webExceptionService.registerTransformer<BadCredentialsException>({ ex -> WebException(code = 401, message = "BadCredentials") })
        webExceptionService.registerTransformer<UnauthorizedException>({ ex -> WebException(code = 403, message = "AccessDenied") })
        webExceptionService.registerTransformer<TokenExpiredException>({ exp -> WebException(code = 401, message = "TokenExpired") })
    }

}