package com.weweb.auth.service

import com.weweb.BaseTest
import com.weweb.auth.exception.BadCredentialsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InMemoryUserServiceTest: BaseTest() {

    internal var service = InMemoryUserService(BCryptPasswordEncoder())

    @Test
    fun shouldReturnUser() {
        val user = service.login("user", "user")
        assertNotNull(user)
        assertEquals("user", user.username)
        assertNotEquals("user", user.encodedPassword)
    }

    @Test
    fun shouldThrowBadCredentialException() {
        assertThrows<BadCredentialsException> {
            service.login("user", "admin")
        }
    }

}