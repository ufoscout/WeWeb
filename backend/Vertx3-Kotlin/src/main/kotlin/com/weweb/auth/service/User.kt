package com.weweb.auth.service

class User (val username: String,
    val encodedPassword: String,
    val roles: List<String>)