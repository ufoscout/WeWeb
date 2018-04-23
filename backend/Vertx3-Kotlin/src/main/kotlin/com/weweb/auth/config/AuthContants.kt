package com.weweb.auth.config

import com.weweb.core.config.CoreConstants

interface AuthContants {

    companion object {
        val BASE_AUTH_API = CoreConstants.BASE_API + "/auth"

        val JWT_TOKEN_HEADER = "Authorization"
        val JWT_TOKEN_HEADER_SUFFIX = "Bearer "
    }

}
