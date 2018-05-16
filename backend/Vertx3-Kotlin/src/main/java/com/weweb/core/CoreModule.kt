package com.weweb.core

import com.ufoscout.vertxk.kodein.VertxKModule
import com.weweb.core.config.CoreConfig
import com.weweb.core.jwt.JwtService
import com.weweb.core.jwt.JwtServiceJJWT
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class CoreModule(val config: CoreConfig): VertxKModule {

    override fun module() = Kodein.Module {
            bind<CoreConfig>() with singleton { config }
            bind<JwtService>() with singleton {JwtServiceJJWT(config.jwt, instance())}
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}
