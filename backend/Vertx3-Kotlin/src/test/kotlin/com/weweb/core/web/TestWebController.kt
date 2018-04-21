package com.weweb.core.web

import com.weweb.core.exception.WebException
import com.weweb.core.exception.WebExceptionService
import com.weweb.core.exception.registerTransformer
import io.vertx.ext.web.Router

class TestWebController(val router: Router, val webExceptionService: WebExceptionService) {

    init {

        webExceptionService.registerTransformer<CustomTestException>({exp -> WebException(code = 12345, message = "CustomTestExceptionMessage")})

        router.get("/core/test/fatal/:message").handler {
            var message = it.request().getParam("message")
            throw Exception(message)
        }

        router.get("/core/test/customException").handler {
            throw CustomTestException()
        }

        router.get("/core/test/webException/:code/:message").handler {
            var code = it.request().getParam("code")
            var message = it.request().getParam("message")
            throw WebException(message, code.toInt())
        }

        router.get("/core/test/slow").handler {
            Thread.sleep(2)
            println("Replying from: [${Thread.currentThread().name}]")
            it.response().end("ok")
        }

    }

}