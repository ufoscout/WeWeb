package com.weweb.auth.service

import com.weweb.auth.context.AuthContext

interface AuthContextService {

    fun getAuth(): AuthContext

}