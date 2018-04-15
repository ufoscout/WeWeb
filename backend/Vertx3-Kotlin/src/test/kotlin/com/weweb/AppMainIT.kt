package com.weweb

import org.junit.Assert
import org.junit.Test

class AppMainIT : BaseIT() {

    @Test
    fun vertxShouldNotBeNull() {

       Assert.assertNotNull(vertx())

    }

}