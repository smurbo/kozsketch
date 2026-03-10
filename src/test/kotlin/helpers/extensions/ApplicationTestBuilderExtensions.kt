package com.helpers.extensions

import com.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder

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