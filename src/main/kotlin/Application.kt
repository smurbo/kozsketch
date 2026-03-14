package com

import com.plugins.configureHTTP
import com.plugins.configureKoin
import com.plugins.configureMonitoring
import com.plugins.configureRouting
import com.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
