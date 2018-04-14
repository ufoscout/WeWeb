package com.weweb.auth.service

import com.weweb.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull

import com.weweb.auth.exception.BadCredentialsException
import org.junit.Test

class InMemoryUserServiceTest: BaseTest() {

    internal var service = InMemoryUserService(BCryptPasswordEncoder())

    @Test
    fun shouldReturnUser() {
        val user = service.login("user", "user")
        assertNotNull(user)
        assertEquals("user", user.username)
        assertNotEquals("user", user.encodedPassword)
    }

    @Test(expected = BadCredentialsException::class)
    fun shouldThrowBadCredentialException() {
        service.login("user", "admin")
    }

}