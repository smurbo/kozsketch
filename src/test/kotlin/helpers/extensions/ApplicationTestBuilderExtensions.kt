package com.helpers.extensions

import com.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

fun ApplicationTestBuilder.createEnvironment() {
    application {
        module()
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