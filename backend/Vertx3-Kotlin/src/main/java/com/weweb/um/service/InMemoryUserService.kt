package com.weweb.um.service

import com.ufoscout.coreutils.validation.ValidatorService
import com.ufoscout.vertk.kodein.auth.BadCredentialsException
import com.ufoscout.vertk.kodein.auth.User
import com.weweb.um.dto.CreateUserDto
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryUserService(private val crypt: PasswordEncoder,
                          private val validatorService: ValidatorService ) : UserService {

    private val users = ConcurrentHashMap<String, User>()
    private val passwords = ConcurrentHashMap<String, String>()

    init {
        users["user"] = User(0L, "user", 2L)
        passwords["user"] = crypt.encode("user")
        users["admin"] = User(1L,"admin", 1L)
        passwords["admin"] = crypt.encode("admin")
    }

    override fun login(username: String, password: String): User {
        val user = users[username]

        if (user == null || !crypt.matches(password, passwords[username]!!)) {
            throw BadCredentialsException("")
        }
        return user
    }

    override fun createUser(dto: CreateUserDto): User {
        println("Create user ${dto.email}. Is present? ${users.containsKey(dto.email)}")
        validatorService.validator<CreateUserDto>()
                .add("username", "notUnique", {!users.containsKey(dto.email)})
                .add("confirmPassword", "notSame", {it.password.equals(it.passwordConfirm)})
                .build()
                .validateThrowException(dto)

        val newUser = User(id = Random().nextInt(100000000).toLong(), username =  dto.email, roles = 2L)
        users[newUser.username] = newUser
        passwords[newUser.username] = crypt.encode(dto.password)
        return newUser
    }

}
