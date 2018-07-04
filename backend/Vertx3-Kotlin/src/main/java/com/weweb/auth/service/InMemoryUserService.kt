package com.weweb.auth.service

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.validation.ValidatorService
import com.ufoscout.vertk.kodein.auth.BadCredentialsException
import com.weweb.auth.dto.CreateLoginDto
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryUserService(private val crypt: PasswordEncoder,
                          private val validatorService: ValidatorService ) : UserService {

    private val users = ConcurrentHashMap<String, Auth>()
    private val passwords = ConcurrentHashMap<String, String>()

    init {
        users["user"] = Auth(0L, "user", arrayOf(Roles.USER))
        passwords["user"] = crypt.encode("user")
        users["admin"] = Auth(1L,"admin", arrayOf(Roles.ADMIN))
        passwords["admin"] = crypt.encode("admin")
    }

    override fun login(username: String, password: String): Auth {
        val user = users[username]

        if (user == null || !crypt.matches(password, passwords[username]!!)) {
            throw BadCredentialsException("")
        }
        return user
    }

    override fun createUser(dto: CreateLoginDto): Auth {
        println("Create user ${dto.email}. Is present? ${users.containsKey(dto.email)}")
        validatorService.validator<CreateLoginDto>()
                .add("username", "notUnique", {!users.containsKey(dto.email)})
                .add("confirmPassword", "notSame", {it.password.equals(it.passwordConfirm)})
                .build()
                .validateThrowException(dto)

        val newUser = Auth(Random().nextInt(100000000).toLong(), dto.email, arrayOf(Roles.USER))
        users[newUser.username] = newUser
        passwords[newUser.username] = crypt.encode(dto.password)
        return newUser
    }

}
