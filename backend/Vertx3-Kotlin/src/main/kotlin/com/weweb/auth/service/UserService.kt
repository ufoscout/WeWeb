package com.weweb.auth.service

interface UserService {

    fun login(username: String, password: String): User
}
