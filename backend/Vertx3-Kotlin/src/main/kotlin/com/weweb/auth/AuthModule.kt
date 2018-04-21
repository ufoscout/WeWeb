package com.weweb.auth

import com.weweb.auth.config.AuthConfig
import com.weweb.auth.service.*
import com.weweb.auth.web.AuthenticationController
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object AuthModule {

    fun module() = Kodein.Module {
        bind<AuthConfig>() with singleton { AuthConfig(instance()) }
        bind<PasswordEncoder>() with singleton { BCryptPasswordEncoder() }
        bind<UserService>() with singleton { InMemoryUserService(instance()) }
        bind<AuthContextService>() with singleton { AuthContextServiceImpl(instance()) }
        bind<AuthenticationController>() with singleton { AuthenticationController(instance(), instance(), instance(), instance(), instance(), instance()) }
    }

}