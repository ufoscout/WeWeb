package io.vertx.starter.monitoring

import com.ufoscout.vertxk.VertxkKodein
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.starter.HelloController
import io.vertx.starter.MainVerticle
import org.kodein.di.Kodein

object MonitoringModule {

    fun module(vertx: Vertx) = Kodein.Module {

    }

}