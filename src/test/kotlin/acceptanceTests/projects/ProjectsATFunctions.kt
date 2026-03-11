package com.acceptanceTests.projects

import com.helpers.builders.ProjectBuilder
import com.helpers.constants.PROJECTS_URL
import com.helpers.extensions.create
import io.ktor.client.statement.HttpResponse
import io.ktor.server.testing.ApplicationTestBuilder

suspend fun ApplicationTestBuilder.requestCreateDefaultProject() : HttpResponse {
    return client.create(
        PROJECTS_URL,
        ProjectBuilder().createDefault()
    )
}