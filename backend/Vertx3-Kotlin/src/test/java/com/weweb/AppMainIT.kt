package com.weweb

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class AppMainIT : BaseIT() {

    @Test
    fun vertxShouldNotBeNull() {
       assertNotNull(vertk())
    }

}