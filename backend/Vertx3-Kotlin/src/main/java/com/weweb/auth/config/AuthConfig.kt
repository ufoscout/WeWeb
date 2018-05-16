package com.weweb.auth.config

import com.ufoscout.vertxk.kodein.router.WebException
import com.ufoscout.vertxk.kodein.router.WebExceptionService
import com.ufoscout.vertxk.kodein.router.registerTransformer
import com.weweb.auth.exception.BadCredentialsException
import com.weweb.auth.exception.UnauthenticatedException
import com.weweb.auth.exception.UnauthorizedException

class AuthConfig(val webExceptionService: WebExceptionService) {

    init {
        webExceptionService.registerTransformer<UnauthenticatedException>({ ex -> WebException(code = 401, message = "Unauthenticated") })
        webExceptionService.registerTransformer<BadCredentialsException>({ ex -> WebException(code = 401, message = "BadCredentials") })
        webExceptionService.registerTransformer<UnauthorizedException>({ ex -> WebException(code = 403, message = "AccessDenied") })
    }

}