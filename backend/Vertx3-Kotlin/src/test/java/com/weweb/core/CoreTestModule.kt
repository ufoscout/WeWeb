package com.weweb.core

import com.ufoscout.vertxk.kodein.VertxKModule
import com.weweb.core.web.TestWebController
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance

class CoreTestModule(): VertxKModule {

    override fun module() = Kodein.Module(allowSilentOverride = true) {
        bind<TestWebController>() with eagerSingleton { TestWebController(instance(), instance()) }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}