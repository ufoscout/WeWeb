package com.weweb.auth

import com.ufoscout.vertxk.VertxkModule
import com.weweb.auth.config.AuthConfig
import com.weweb.auth.service.BCryptPasswordEncoder
import com.weweb.auth.service.InMemoryUserService
import com.weweb.auth.service.PasswordEncoder
import com.weweb.auth.service.UserService
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
    }

    override suspend fun onInit(vertx: Vertx, kodein: DKodein) {
        deployVerticle<AuthenticationControllerVerticle>(vertx)
    }

}