package com.weweb.auth

import com.weweb.auth.web.TestAuthenticationController
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance


object AuthTestModule {

    fun module() = Kodein.Module(allowSilentOverride = true) {
        bind<TestAuthenticationController>() with eagerSingleton { TestAuthenticationController(instance(), instance()) }
    }

}