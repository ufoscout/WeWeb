package com.weweb.auth.web

import com.ufoscout.vertxk.VertxKComponent
import com.weweb.auth.config.AuthContants
import com.weweb.auth.context.UserContext
import com.weweb.auth.dto.LoginDto
import com.weweb.auth.dto.LoginResponseDto
import com.weweb.auth.exception.BadCredentialsException
import com.weweb.auth.exception.UnauthenticatedException
import com.weweb.auth.exception.UnauthorizedException
import com.weweb.auth.service.UserService
import com.weweb.core.exception.WebException
import com.weweb.core.exception.WebExceptionService
import com.weweb.core.exception.registerTransformer
import com.weweb.core.json.JsonSerializerService
import com.weweb.core.json.fromJson
import com.weweb.core.jwt.JwtService
import com.weweb.core.jwt.TokenExpiredException
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthenticationController (val router: Router,
                                val userService: UserService,
                                val json: JsonSerializerService,
                                val jwt: JwtService,
                                val webExceptionService: WebExceptionService): VertxKComponent {

    override suspend fun start() {

        webExceptionService.registerTransformer<BadCredentialsException>({exp -> WebException(code = 401, message = "BadCredentials") })
        webExceptionService.registerTransformer<UnauthenticatedException>({exp -> WebException(code = 401, message = "NotAuthenticated") })
        webExceptionService.registerTransformer<TokenExpiredException>({exp -> WebException(code = 401, message = "TokenExpired") })
        webExceptionService.registerTransformer<UnauthorizedException>({exp -> WebException(code = 403, message = "AccessDenied") })

        router.post(AuthContants.BASE_AUTH_API + "/login").handler {
            val request = it.request();
            val body = it.getBodyAsString()
            println("body is: [${body}]")
            val loginDto = json.fromJson<LoginDto>(it.getBodyAsString())

            val login = userService.login(loginDto.username, loginDto.password)

            val roles = login.roles.toTypedArray()
            val token = jwt.generate(login.username, UserContext(login.username, roles))

            println("Return token: [${token}]")
            request.response().endWithJson(LoginResponseDto(token))
        }

    }

}