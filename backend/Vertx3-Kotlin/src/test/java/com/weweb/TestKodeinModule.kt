package com.weweb

import com.ufoscout.vertk.kodein.VertkKodeinModule
import io.vertx.core.Vertx
import org.kodein.di.Kodein

class TestKodeinModule: VertkKodeinModule {

    override fun module() = Kodein.Module {
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}