package com.weweb.auth.dto

import com.ufoscout.coreutils.auth.Auth

data class LoginResponseDto (
        val token: String,
        val auth: Auth) {}
