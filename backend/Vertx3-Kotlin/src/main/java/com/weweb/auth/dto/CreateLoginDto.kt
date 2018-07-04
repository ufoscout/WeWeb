package com.weweb.auth.dto

data class CreateLoginDto(
        val email: String,
        val password: String,
        val passwordConfirm: String
) {}