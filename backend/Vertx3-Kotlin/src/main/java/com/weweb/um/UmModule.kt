package com.weweb.um

import com.ufoscout.vertk.Vertk
import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.ufoscout.vertk.kodein.deployKodeinVerticle
import com.weweb.um.service.BCryptPasswordEncoder
import com.weweb.um.service.InMemoryUserService
import com.weweb.um.service.PasswordEncoder
import com.weweb.um.service.UserService
import com.weweb.um.web.UmControllerVerticle
import io.vertx.core.DeploymentOptions
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class UmModule(val deploymentOptions: DeploymentOptions): VertkKodeinModule {

    override fun module() = Kodein.Module {
        bind<PasswordEncoder>() with singleton { BCryptPasswordEncoder() }
        bind<UserService>() with singleton { InMemoryUserService(instance(), instance()) }
    }

    override suspend fun onInit(vertk: Vertk, kodein: Kodein) {
        vertk.deployKodeinVerticle<UmControllerVerticle>(deploymentOptions)
    }

}