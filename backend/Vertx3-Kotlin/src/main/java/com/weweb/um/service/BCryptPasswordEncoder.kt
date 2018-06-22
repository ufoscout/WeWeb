package com.weweb.um.service

import org.mindrot.jbcrypt.BCrypt

class BCryptPasswordEncoder: PasswordEncoder {

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return BCrypt.checkpw(rawPassword, encodedPassword)
    }

    override fun encode(rawPassword: String): String {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

}