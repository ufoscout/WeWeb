package com.weweb.auth

import com.ufoscout.coreutils.auth.RolesProvider
import com.ufoscout.vertk.Vertk
import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.ufoscout.vertk.kodein.deployKodeinVerticle
import com.weweb.auth.service.*
import com.weweb.auth.web.AuthControllerVerticle
import io.vertx.core.DeploymentOptions
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AuthModule(val deploymentOptions: DeploymentOptions): VertkKodeinModule {

    override fun module() = Kodein.Module {
        bind<PasswordEncoder>() with singleton { BCryptPasswordEncoder() }
        bind<RolesProvider>() with singleton { InMemoryRolesProvider() }
        bind<UserService>() with singleton { InMemoryUserService(instance(), instance()) }
    }

    override suspend fun onInit(vertk: Vertk, kodein: Kodein) {
        vertk.deployKodeinVerticle<AuthControllerVerticle>(deploymentOptions)
    }

}