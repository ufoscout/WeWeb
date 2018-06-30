package com.weweb.um.service

import com.ufoscout.coreutils.auth.Auth
import com.weweb.um.dto.CreateUserDto

interface UserService {

    fun login(username: String, password: String): Auth
    fun createUser(dto: CreateUserDto): Auth
}
