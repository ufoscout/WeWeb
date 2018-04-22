package com.ufoscout.vertxk.ext

import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.launch
import java.lang.Exception
import kotlin.reflect.KClass

interface RouterExt {

    fun Router.restGet(path: String, handler: suspend (rc: RoutingContext) -> Any) {
        get(path)
                .produces("application/json")
                .handler({
                    launch(Vertx.currentContext().dispatcher()) {
                        try {
                            val result = handler(it)
                            it.response().end(Json.encode(result))
                        } catch (e: Exception) {
                            it.fail(e)
                        }
                    }
                })
    }

    fun <I : Any> Router.restPost(path: String, kClass: KClass<I>, handler: suspend (rc: RoutingContext, body: I) -> Any) {
        post(path)
                .consumes("application/json")
                .produces("application/json")
                .handler({
                    launch(Vertx.currentContext().dispatcher()) {
                        try {
                            val body = it.bodyAsJson.mapTo(kClass.javaObjectType)
                            val result = handler(it, body)
                            it.response().end(Json.encode(result))
                        } catch (e: Exception) {
                            it.fail(e)
                        }
                    }
                })
    }

}