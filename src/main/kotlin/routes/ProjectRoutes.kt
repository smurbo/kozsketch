package com.routes

import com.models.ProjectModel
import com.services.projects.ProjectService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import java.util.UUID
import kotlin.getValue

fun Route.configureProjectRoutes() {
    val projectService by inject<ProjectService>()

    route(PROJECTS_URL) {
        post {
            val model = call.receive<ProjectModel>()
            val id = projectService.create(model)
            call.respond(HttpStatusCode.Created, id.toString())
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            val model = projectService.getById(UUID.fromString(id))

            if (model != null) call.respond(HttpStatusCode.OK, model)
            else call.respond(HttpStatusCode.NotFound)
        }

        put("/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            val model = call.receive<ProjectModel>()
            projectService.update(UUID.fromString(id), model)
            call.respond(HttpStatusCode.OK)
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            projectService.delete(UUID.fromString(id))
            call.respond(HttpStatusCode.OK)
        }
    }
}