package com.acceptanceTests.projects

import com.acceptanceTests.AcceptanceTestBase
import com.helpers.builders.ProjectBuilder
import com.helpers.constants.*
import com.infrastructure.Project
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectsAcceptanceTests : AcceptanceTestBase() {
    override val partialUrl: String
        get() = PROJECTS_URL

    private lateinit var projectId : UUID
    private val defaultProject = ProjectBuilder().createDefault()

    override suspend fun setupDefaults(client: HttpClient) {
        createDefaultProject(client)
    }

    private suspend fun createDefaultProject(client : HttpClient) {
        projectId = client.createAndGetId(defaultProject)
    }

    @Test
    fun `If we create a Project, then get it, we should get the created Project`() = testApplication {
        createEnvironmentWithDefaults()

        val project = client.get<Project>(projectId)

        assertEquals(DEFAULT_PROJECT_NAME, project.name)
        assertEquals(DEFAULT_PROJECT_RATING, project.rating)
    }

    @Test
    fun `If we create a Project, then update it, we should get the updated Project`() = testApplication {
        createEnvironmentWithDefaults()

        val project = client.get<Project>(projectId)

        assertEquals(DEFAULT_PROJECT_NAME, project.name)
        assertEquals(DEFAULT_PROJECT_RATING, project.rating)

        client.update(
            id = projectId,
            entity = ProjectBuilder().createWithValidProperties(
                name = UPDATED_PROJECT_NAME,
                rating = UPDATED_PROJECT_RATING,
            )
        )

        val updatedProject = client.get<Project>(projectId)
        assertEquals(UPDATED_PROJECT_NAME, updatedProject.name)
        assertEquals(UPDATED_PROJECT_RATING, updatedProject.rating)
    }

    @Test
    fun `If we create a Project, then delete it, we should get a response with 404 Not Found when we try to get it`() = testApplication {
        createEnvironmentWithDefaults()

        val deleteResponse = client.delete(projectId)
        assertEquals(HttpStatusCode.OK, deleteResponse.status)

        val getResponse = client.get("$partialUrl/$projectId")
        assertEquals(HttpStatusCode.NotFound, getResponse.status)
    }
}