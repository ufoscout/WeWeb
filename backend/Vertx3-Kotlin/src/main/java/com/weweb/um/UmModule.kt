package com.weweb.um

import com.ufoscout.vertxk.kodein.VertxKModule
import com.ufoscout.vertxk.kodein.deployKodeinVerticle
import com.weweb.um.login.BCryptPasswordEncoder
import com.weweb.um.login.InMemoryUserService
import com.weweb.um.login.PasswordEncoder
import com.weweb.um.login.UserService
import com.weweb.um.web.LoginControllerVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class UmModule(val deploymentOptions: DeploymentOptions): VertxKModule {

    override fun module() = Kodein.Module {
        bind<PasswordEncoder>() with singleton { BCryptPasswordEncoder() }
        bind<UserService>() with singleton { InMemoryUserService(instance()) }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        vertx.deployKodeinVerticle<LoginControllerVerticle>(deploymentOptions)
    }

}