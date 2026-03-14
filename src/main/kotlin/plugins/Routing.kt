package com.plugins

import com.routes.configureProjectRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        route("/api") {
            configureProjectRoutes()
        }
    }
}
