package com.weweb.um.service

import com.ufoscout.vertk.kodein.auth.User
import com.weweb.um.dto.CreateUserDto

interface UserService {

    fun login(username: String, password: String): User
    fun createUser(dto: CreateUserDto): User
}
