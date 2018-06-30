package com.weweb.auth.service

import com.ufoscout.coreutils.auth.Role
import com.ufoscout.coreutils.auth.RolesProvider

class InMemoryRolesProvider: RolesProvider {

    companion object {
        val roles = mapOf(
                Pair(Roles.ADMIN, Role(0, Roles.ADMIN, arrayOf())),
                Pair(Roles.USER, Role(1, Roles.USER, arrayOf()))
        )
    }

    override fun getAll(): List<Role> {
        val roleByName = mutableListOf<Role>()
        roles.forEach() {
            roleByName.add(it.value)
        }
        return roleByName
    }

    override fun getByName(vararg roleNames: String?): MutableList<Role> {
        val roleByName = mutableListOf<Role>()
        roleNames.forEach() {
            if (roles.containsKey(it)) {
                roleByName.add(roles[it]!!)
            }
        }
        return roleByName
    }

}