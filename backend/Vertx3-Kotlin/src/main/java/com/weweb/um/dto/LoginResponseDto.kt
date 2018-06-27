package com.weweb.um.dto

import com.ufoscout.vertk.kodein.auth.User

data class LoginResponseDto (
        val token: String,
        val auth: User) {}
