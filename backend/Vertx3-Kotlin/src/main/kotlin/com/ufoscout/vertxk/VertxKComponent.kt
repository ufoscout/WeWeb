package com.ufoscout.vertxk

import com.ufoscout.vertxk.ext.K

interface VertxKComponent: K {

    suspend fun start()

}