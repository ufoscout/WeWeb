package com.weweb.auth.context

/**
 * Context containing the profile of the caller of a protected end point
 *
 * @author Francesco Cina'
 */
class UserContext @JvmOverloads constructor(val username: String = "",
                                            val roles: Array<String> = arrayOf())
