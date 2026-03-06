package com

import com.infrastructure.configureDatabases
import com.webApi.configureHTTP
import com.webApi.configureMonitoring
import com.webApi.configureRouting
import com.webApi.configureSerialization
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureHTTP()
    configureRouting()

    install(ContentNegotiation) {
        json()
    }
}
