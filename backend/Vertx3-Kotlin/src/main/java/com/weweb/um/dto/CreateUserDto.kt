package com.weweb.um.dto

data class CreateUserDto(
        val email: String,
        val password: String,
        val passwordConfirm: String
) {}