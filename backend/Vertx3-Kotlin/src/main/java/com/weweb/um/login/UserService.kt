package com.weweb.um.login

import com.ufoscout.vertxk.kodein.auth.User

interface UserService {

    fun login(username: String, password: String): User
}
