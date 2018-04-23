package com.weweb

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AppMainIT : BaseIT() {

    @Test
    fun vertxShouldNotBeNull() {
       assertNotNull(vertx())
    }

}