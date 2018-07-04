package com.weweb.auth.service

import com.ufoscout.coreutils.auth.Auth
import com.weweb.auth.dto.CreateLoginDto

interface UserService {

    fun login(username: String, password: String): Auth
    fun createUser(dto: CreateLoginDto): Auth
}
