package com.weweb.auth.service

import com.ufoscout.coreutils.validation.SimpleValidatorService
import com.ufoscout.vertk.kodein.auth.BadCredentialsException
import com.weweb.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InMemoryUserServiceTest: BaseTest() {

    internal var service = InMemoryUserService(BCryptPasswordEncoder(), SimpleValidatorService())

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