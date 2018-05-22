package com.weweb.core

import com.ufoscout.vertxk.kodein.VertxKModule
import com.weweb.core.config.CoreConfig
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class CoreModule(val config: CoreConfig): VertxKModule {

    override fun module() = Kodein.Module {
            bind<CoreConfig>() with singleton { config }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}
