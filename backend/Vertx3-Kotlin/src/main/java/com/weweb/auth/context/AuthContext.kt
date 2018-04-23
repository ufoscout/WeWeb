package com.weweb.auth.context

import com.weweb.auth.exception.UnauthenticatedException
import com.weweb.auth.exception.UnauthorizedException

class AuthContext(val user: UserContext, val permissionsMap: Map<String, Array<String>>) {

    fun isAuthenticated(): AuthContext {
        if (user.username.isEmpty()) {
            throw UnauthenticatedException("User needs to be authenticated.")
        }
        return this
    }

    fun hasRole(role: String): AuthContext {
        isAuthenticated()
        if (booleanHasRole(role)) {
            return this;
        }
        throw UnauthorizedException("User [${user.username}] does not have the required roles.")
    }

    fun hasAnyRole(vararg roles: String): AuthContext {
        isAuthenticated()
        for (role in roles) {
            if (booleanHasRole(role)) {
                return this;
            }
        }
        throw UnauthorizedException("User [${user.username}] does not have the required roles.")
    }

    fun hasAllRoles(vararg roles: String): AuthContext {
        isAuthenticated()
        for (role in roles) {
            if (!booleanHasRole(role)) {
                throw UnauthorizedException("User [${user.username}] does not have the required roles.")
            }
        }
        return this
    }

    fun hasPermission(permission: String): AuthContext {
        isAuthenticated()
        if (booleanHasPermission(permission)) {
            return this
        }
        throw UnauthorizedException("User [${user.username}] does not have the required permissions.")
    }

    fun hasAnyPermission(vararg permissions: String): AuthContext {
        isAuthenticated()
        for (permission in permissions) {
            if (booleanHasPermission(permission)) {
                return this
            }
        }
        throw UnauthorizedException("User [${user.username}] does not have the required permissions.")
    }

    fun hasAllPermissions(vararg permissions: String): AuthContext {
        isAuthenticated()
        for (permission in permissions) {
            if (!booleanHasPermission(permission)) {
                throw UnauthorizedException("User [${user.username}] does not have the required permissions.")
            }
        }
        return this
    }

    private fun booleanHasRole(role: String): Boolean {
        for (userRole in user.roles) {
            if (userRole.equals(role))
                return true
        }
        return false
    }

    private fun booleanHasPermission(permission: String): Boolean {
        val permissionRoles = permissionsMap[permission]
        if (permissionRoles != null) {
            for (permissionRole in permissionRoles) {
                if (booleanHasRole(permissionRole)) {
                    return true
                }
            }
        }
        return false
    }

}
