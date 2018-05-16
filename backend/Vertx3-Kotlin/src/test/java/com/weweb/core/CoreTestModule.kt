package com.weweb.core

import com.ufoscout.vertxk.kodein.VertxKModule
import io.vertx.core.Vertx
import org.kodein.di.Kodein

class CoreTestModule(): VertxKModule {

    override fun module() = Kodein.Module(allowSilentOverride = true) {
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}