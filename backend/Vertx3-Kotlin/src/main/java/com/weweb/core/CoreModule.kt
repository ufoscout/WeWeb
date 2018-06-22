package com.weweb.core

import com.ufoscout.coreutils.validation.SimpleValidatorService
import com.ufoscout.coreutils.validation.ValidatorService
import com.ufoscout.vertk.Vertk
import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.weweb.core.config.CoreConfig
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class CoreModule(val config: CoreConfig) : VertkKodeinModule {

    override fun module() = Kodein.Module {
        bind<CoreConfig>() with singleton { config }
        bind<ValidatorService>() with singleton { SimpleValidatorService() }

    }

    override suspend fun onInit(vertk: Vertk, kodein: Kodein) {
    }

}
