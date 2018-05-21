package com.weweb.auth

import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.auth.AuthServiceImpl
import com.ufoscout.coreutils.auth.RolesEncoderToLong
import com.ufoscout.vertxk.kodein.VertxKModule
import com.ufoscout.vertxk.kodein.deployKodeinVerticle
import com.weweb.auth.config.AuthConfig
import com.weweb.auth.service.*
import com.weweb.auth.web.AuthenticationController
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AuthModule(val deploymentOptions: DeploymentOptions): VertxKModule {

    override fun module() = Kodein.Module {
        bind<AuthConfig>() with singleton { AuthConfig(instance()) }
        bind<PasswordEncoder>() with singleton { BCryptPasswordEncoder() }
        bind<AuthService<Long>>() with singleton {
            AuthServiceImpl<Long>(InMemoryRolesProvider(), RolesEncoderToLong())
        }
        bind<UserService>() with singleton { InMemoryUserService(instance()) }
        bind<AuthContextService>() with singleton {
            AuthContextServiceImpl(instance(), instance())
        }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        vertx.deployKodeinVerticle<AuthenticationController>(deploymentOptions)
    }

}