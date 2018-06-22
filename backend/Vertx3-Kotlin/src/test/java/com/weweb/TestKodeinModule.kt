package com.weweb

import com.ufoscout.vertk.Vertk
import com.ufoscout.vertk.kodein.VertkKodeinModule
import org.kodein.di.Kodein

class TestKodeinModule: VertkKodeinModule {

    override fun module() = Kodein.Module {
    }

    override suspend fun onInit(vertk: Vertk, kodein: Kodein) {
    }

}