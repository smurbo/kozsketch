package com.helpers.extensions

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

fun ApplicationTestBuilder.createEnvironment() {
    environment {
        config = ApplicationConfig("at.yaml")
    }
    client = createClient {
        install(ContentNegotiation) {
            json()
        }
    }
}

fun runAT(
    body: suspend ApplicationTestBuilder.() -> Unit
) {
    testApplication {
        createEnvironment()
        body()
    }
}