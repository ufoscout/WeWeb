package com.weweb.um.login

import com.ufoscout.vertxk.kodein.auth.BadCredentialsException
import com.weweb.BaseTest
import com.weweb.um.login.BCryptPasswordEncoder
import com.weweb.um.login.InMemoryUserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InMemoryUserServiceTest: BaseTest() {

    internal var service = InMemoryUserService(BCryptPasswordEncoder())

    @Test
    fun shouldReturnUser() {
        val user = service.login("user", "user")
        assertNotNull(user)
        assertEquals("user", user.username)
    }

    @Test
    fun shouldThrowBadCredentialException() {
        assertThrows<BadCredentialsException> {
            service.login("user", "admin")
        }
    }

}