package com.acceptanceTests.projects

import com.helpers.builders.ProjectModelBuilder
import com.routes.PROJECTS_URL
import com.helpers.extensions.create
import io.ktor.client.statement.HttpResponse
import io.ktor.server.testing.ApplicationTestBuilder

suspend fun ApplicationTestBuilder.requestCreateDefaultProject() : HttpResponse {
    return client.create(
        PROJECTS_URL,
        ProjectModelBuilder().createDefault()
    )
}