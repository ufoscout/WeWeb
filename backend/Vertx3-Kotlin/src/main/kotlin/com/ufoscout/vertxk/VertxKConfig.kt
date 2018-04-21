package com.ufoscout.vertxk

import io.vertx.core.shareddata.Shareable
import org.kodein.di.Kodein

internal class VertxKConfig(
        val modules: Array<Kodein.Module>) : Shareable {

    var kodein: Kodein? = null

}