package com.weweb.um.dto

import com.ufoscout.coreutils.auth.Auth

data class LoginResponseDto (
        val token: String,
        val auth: Auth) {}
