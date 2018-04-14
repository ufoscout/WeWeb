package com.weweb.auth.service

import com.weweb.auth.exception.BadCredentialsException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryUserService(private val crypt: PasswordEncoder) : UserService {

    private val users = ConcurrentHashMap<String, User>()

    init {
        users["user"] = User("user", crypt.encode("user"), Arrays.asList("USER"))
        users["admin"] = User("admin", crypt.encode("admin"), Arrays.asList("USER", "ADMIN"))
    }

    override fun login(username: String, password: String): User {
        val user = users[username]

        if (user == null || !crypt.matches(password, user.encodedPassword)) {
            throw BadCredentialsException("")
        }

        return user

    }

}
