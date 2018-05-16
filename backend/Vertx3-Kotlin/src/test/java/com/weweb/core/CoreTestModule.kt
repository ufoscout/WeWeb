package com.weweb.core

import com.weweb.core.web.TestWebController
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance


/**
 * Created by ufo on 18/07/17.
 */

object CoreTestModule {

    fun module() = Kodein.Module(allowSilentOverride = true) {
        bind<TestWebController>() with eagerSingleton { TestWebController(instance(), instance()) }
    }

}