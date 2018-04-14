package com.weweb.auth.context

import com.weweb.BaseTest
import com.weweb.auth.exception.UnauthenticatedException
import com.weweb.auth.exception.UnauthorizedException
import org.junit.Test

class AuthContextTest: BaseTest() {

    @Test
    fun shouldBeAuthenticated() {
        val user = UserContext(isValid = true)
        val authContext = AuthContext(user, HashMap())
        authContext.isAuthenticated()
    }

    @Test(expected = UnauthenticatedException::class)
    fun shouldBeNotAuthenticated() {
        val user = UserContext()
        val authContext = AuthContext(user, HashMap())
        authContext.isAuthenticated()
    }

    @Test(expected = UnauthenticatedException::class)
    fun shouldBeNotAuthenticatedEvenIfHasRole() {
        val user = UserContext(roles = arrayOf("ADMIN"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasRole("ADMIN")
    }

    @Test
    fun shouldHaveRole() {
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasRole("ADMIN")
    }


    @Test
    fun shouldHaveRole2() {
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasRole("USER")
    }

    @Test(expected = UnauthorizedException::class)
    fun shouldNotHaveRole() {
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN"))
        val authContext = AuthContext(user, HashMap())
        authContext.isAuthenticated()
        authContext.hasRole("USER")
    }

    @Test
    fun shouldHaveAnyRole() {
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasAnyRole("USER", "FRIEND")
    }

    @Test(expected = UnauthorizedException::class)
    fun shouldNotHaveAnyRole() {
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "OWNER"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasAnyRole("USER", "FRIEND")
    }

    @Test
    fun shouldHaveAllRoles() {
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER", "FRIEND"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasAllRoles("USER", "FRIEND")
    }

    @Test(expected = UnauthorizedException::class)
    fun shouldNotHaveAllRoles() {
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasAllRoles("USER", "FRIEND")
    }

    @Test(expected = UnauthenticatedException::class)
    fun shouldBeNotAuthenticatedEvenIfHasPermission() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER", "ADMIN"))
        val user = UserContext(roles = arrayOf("ADMIN"))
        val authContext = AuthContext(user, HashMap())
        authContext.hasPermission("delete")
    }


    @Test
    fun shouldHavePermissions() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER", "ADMIN"))
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN"))
        val authContext = AuthContext(user, permissions)
        authContext.hasPermission("delete")
    }


    @Test
    fun shouldHavePermission2() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER", "ADMIN"))
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasPermission("delete")
    }


    @Test(expected = UnauthorizedException::class)
    fun shouldNotHavePermission() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER"))
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasPermission("delete")
    }

    @Test
    fun shouldHaveAnyPermission() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER"))
        permissions.put("superDelete", arrayOf("ADMIN"))
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasAnyPermission("delete", "superDelete")
    }

    @Test(expected = UnauthorizedException::class)
    fun shouldNotHaveAnyPermission() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER", "ADMIN"))
        permissions.put("superDelete", arrayOf("ADMIN"))
        val user = UserContext(isValid = true, roles = arrayOf("USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasAnyPermission("delete", "superAdmin")
    }

    @Test
    fun shouldHaveAllPermissions() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER", "USER"))
        permissions.put("superDelete", arrayOf("ADMIN"))
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasAllPermissions("delete", "superDelete")
    }

    @Test(expected = UnauthorizedException::class)
    fun shouldNotHaveAllPermissions() {
        val permissions = HashMap<String, Array<String>>()
        permissions.put("delete", arrayOf("OWNER"))
        permissions.put("superDelete", arrayOf("ADMIN"))
        val user = UserContext(isValid = true, roles = arrayOf("ADMIN", "USER"))
        val authContext = AuthContext(user, permissions)
        authContext.hasAllPermissions("delete", "superDelete")
    }

}