package com.ufoscout.vertxk

import com.ufoscout.vertxk.ext.Kx

interface VertxKComponent: Kx {

    suspend fun start()

}