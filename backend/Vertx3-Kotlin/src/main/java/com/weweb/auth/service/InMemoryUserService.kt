package com.weweb.auth.service

import com.weweb.auth.exception.BadCredentialsException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryUserService(private val crypt: PasswordEncoder) : UserService {

    private val users = ConcurrentHashMap<String, User>()
    private val passwords = ConcurrentHashMap<String, String>()

    init {
        users["user"] = User("user", 2L)
        passwords["user"] = crypt.encode("user")
        users["admin"] = User("admin", 3L)
        passwords["admin"] = crypt.encode("admin")
    }

    override fun login(username: String, password: String): User {
        val user = users[username]

        if (user == null || !crypt.matches(password, passwords[username]!!)) {
            throw BadCredentialsException("")
        }

        return user

    }

}
