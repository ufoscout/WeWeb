package com.weweb.auth.web

import com.weweb.auth.config.AuthContants
import com.weweb.auth.context.UserContext
import com.weweb.auth.exception.BadCredentialsException
import com.weweb.auth.exception.UnauthenticatedException
import com.weweb.auth.exception.UnauthorizedException
import com.weweb.auth.service.UserService
import com.weweb.core.exception.WebException
import com.weweb.core.exception.WebExceptionService
import com.weweb.core.exception.registerTransformer
import com.weweb.core.json.JsonSerializerService
import com.weweb.core.json.fromJson
import com.weweb.core.jwt.TokenExpiredException
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthenticationControllerVerticle (val router: Router,
                                        val userService: UserService,
                                        val json: JsonSerializerService,
                                        val webExceptionService: WebExceptionService): CoroutineVerticle() {

    override suspend fun start() {

        webExceptionService.registerTransformer<BadCredentialsException>({exp -> WebException(code = 401, message = "BadCredentials") })
        webExceptionService.registerTransformer<UnauthenticatedException>({exp -> WebException(code = 401, message = "NotAuthenticated") })
        webExceptionService.registerTransformer<TokenExpiredException>({exp -> WebException(code = 401, message = "TokenExpired") })
        webExceptionService.registerTransformer<UnauthorizedException>({exp -> WebException(code = 403, message = "AccessDenied") })

        router.get(AuthContants.BASE_AUTH_API + "/login").handler {
            val request = it.request();
            val body = it.getBodyAsString()
            println("body is: [${body}]")
            val userContext = json.fromJson<UserContext>(it.getBodyAsString())
            /*
            launch(vertx.dispatcher()) {
                val body = awaitResult<JsonObject> {  request.bodyHandler { it.toJsonObject()} }
                println("body is: [${body}]")
            }
            */
            request.response().end("ok")
        }

    }

}