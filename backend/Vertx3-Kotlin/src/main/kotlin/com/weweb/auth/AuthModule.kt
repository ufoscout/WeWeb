package com.weweb.auth

import com.ufoscout.vertxk.VertxkModule
import com.weweb.auth.config.AuthConfig
import com.weweb.auth.service.*
import com.weweb.auth.web.AuthenticationControllerVerticle
import io.vertx.core.Vertx
import org.kodein.di.DKodein
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AuthModule : VertxkModule() {

    override fun module() = Kodein.Module {
        bind<AuthConfig>() with singleton { AuthConfig(instance()) }
        bind<PasswordEncoder>() with singleton { BCryptPasswordEncoder() }
        bind<UserService>() with singleton { InMemoryUserService(instance()) }
        bind<AuthContextService>() with singleton { AuthContextServiceImpl() }
    }

    override suspend fun onInit(vertx: Vertx, kodein: DKodein) {
       // deployVerticle<AuthenticationControllerVerticle>(vertx)
    }

}