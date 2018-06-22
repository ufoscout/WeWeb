package com.weweb.auth.service

import com.ufoscout.coreutils.auth.Role
import com.ufoscout.coreutils.auth.RolesProvider

class InMemoryRolesProvider: RolesProvider {

    companion object {
        val roles = listOf(
                Role(0, Roles.ADMIN, arrayOf()),
                Role(1, Roles.USER, arrayOf())
        )
    }

    override fun getAll(): List<Role> {
        return roles
    }

}