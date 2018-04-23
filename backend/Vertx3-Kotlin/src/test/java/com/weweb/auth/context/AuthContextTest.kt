package com.weweb.auth.context

import com.weweb.BaseTest
import com.weweb.auth.exception.UnauthenticatedException
import com.weweb.auth.exception.UnauthorizedException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthContextTest: BaseTest() {

    @Test
    fun shouldBeAuthenticated() {
        val user = UserContext("name")
        val authContext = AuthContext(user, HashMap())
        authContext.isAuthenticated()
    }

    @Test
    fun shouldBeNotAuthenticated() {
        assertThrows<UnauthenticatedException> {
            val user = UserContext()
            val authContext = AuthContext(user, HashMap())
            authContext.isAuthenticated()
        }
    }

    @Test
    fun shouldBeNotAuthenticatedEvenIfHasRole() {
        assertThrows<UnauthenticatedException> {
            val user = UserContext(roles = arrayOf("ADMIN"))
            val authContext = AuthContext(user, HashMap())
            authContext.hasRole("ADMIN")
        }
    }

    @Test
    fun shouldHaveRole() {
        val user = UserContext("name", roles = arrayOf("ADMIN"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasRole("ADMIN")
    }


    @Test
    fun shouldHaveRole2() {
        val user = UserContext("name", roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasRole("USER")
    }

    @Test
    fun shouldNotHaveRole() {
        assertThrows<UnauthorizedException> {
            val user = UserContext("name", roles = arrayOf("ADMIN"))
            val authContext = AuthContext(user, HashMap())
            authContext.isAuthenticated()
            authContext.hasRole("USER")
        }
    }

    @Test
    fun shouldHaveAnyRole() {
        val user = UserContext("name", roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasAnyRole("USER", "FRIEND")
    }

    @Test
    fun shouldNotHaveAnyRole() {
        assertThrows<UnauthorizedException> {
            val user = UserContext("name", roles = arrayOf("ADMIN", "OWNER"))
            val authContext = AuthContext(user, HashMap())
            authContext.hasAnyRole("USER", "FRIEND")
        }
    }

    @Test
    fun shouldHaveAllRoles() {
        val user = UserContext("name", roles = arrayOf("ADMIN", "USER", "FRIEND"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasAllRoles("USER", "FRIEND")
    }

    @Test
    fun shouldNotHaveAllRoles() {
        assertThrows<UnauthorizedException> {
            val user = UserContext("name", roles = arrayOf("ADMIN", "USER"))
            val authContext = AuthContext(user, HashMap())
            authContext.hasAllRoles("USER", "FRIEND")
        }
    }

    @Test
    fun shouldBeNotAuthenticatedEvenIfHasPermission() {
        assertThrows<UnauthenticatedException> {
            val permissions = HashMap<String, Array<String>>()
            permissions.put("delete", arrayOf("OWNER", "ADMIN"))
            val user = UserContext(roles = arrayOf("ADMIN"))
            val authContext = AuthContext(user, HashMap())
            authContext.hasPermission("delete")
        }
    }


    @Test
    fun shouldHavePermissions() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER", "ADMIN"))
        val user = UserContext("name", roles = arrayOf("ADMIN"))
        val authContext = AuthContext(user, permissions)
        authContext.hasPermission("delete")
    }


    @Test
    fun shouldHavePermission2() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER", "ADMIN"))
        val user = UserContext("name", roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasPermission("delete")
    }


    @Test
    fun shouldNotHavePermission() {
        assertThrows<UnauthorizedException> {
            val permissions = HashMap<String, Array<String>>()
            permissions.put("delete", arrayOf("OWNER"))
            val user = UserContext("name", roles = arrayOf("ADMIN", "USER"))
            val authContext = AuthContext(user, permissions)
            authContext.hasPermission("delete")
        }
    }

    @Test
    fun shouldHaveAnyPermission() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER"))
        permissions.put("superDelete", arrayOf("ADMIN"))
        val user = UserContext("name", roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasAnyPermission("delete", "superDelete")
    }

    @Test
    fun shouldNotHaveAnyPermission() {
        assertThrows<UnauthorizedException> {
            val permissions = HashMap<String, Array<String>>()
            permissions.put("delete", arrayOf("OWNER", "ADMIN"))
            permissions.put("superDelete", arrayOf("ADMIN"))
            val user = UserContext("name", roles = arrayOf("USER"))
            val authContext = AuthContext(user, permissions)
            authContext.hasAnyPermission("delete", "superAdmin")
        }
    }

    @Test
    fun shouldHaveAllPermissions() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER", "USER"))
        permissions.put("superDelete", arrayOf("ADMIN"))
        val user = UserContext("name", roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasAllPermissions("delete", "superDelete")
    }

    @Test
    fun shouldNotHaveAllPermissions() {
        assertThrows<UnauthorizedException> {
            val permissions = HashMap<String, Array<String>>()
            permissions.put("delete", arrayOf("OWNER"))
            permissions.put("superDelete", arrayOf("ADMIN"))
            val user = UserContext("name", roles = arrayOf("ADMIN", "USER"))
            val authContext = AuthContext(user, permissions)
            authContext.hasAllPermissions("delete", "superDelete")
        }
    }

}