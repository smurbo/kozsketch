package com.acceptanceTests.projects

import com.helpers.builders.ProjectBuilder
import com.helpers.constants.DEFAULT_PROJECT_NAME
import com.helpers.constants.DEFAULT_PROJECT_RATING
import com.helpers.constants.UPDATED_PROJECT_NAME
import com.helpers.constants.UPDATED_PROJECT_RATING
import com.infrastructure.Project
import com.module
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

class ProjectsAT {
    fun ApplicationTestBuilder.createEnvironment() {
        application {
            module()
        }
        client = createClient() {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Test
    fun shouldCreateProject_ProjectShouldBeCreated() = testApplication {
        createEnvironment()

        val postResponse = client.postProject()
        assertPostResponse(postResponse)

        assertEquals(HttpStatusCode.Companion.Created, postResponse.status)
        assertNotNull(postResponse.body())

        val receivedId = UUID.fromString(postResponse.bodyAsText())

        val project = client.getProject(receivedId)

        assertEquals(DEFAULT_PROJECT_NAME, project.name)
        assertEquals(DEFAULT_PROJECT_RATING, project.rating)
    }

    private suspend inline fun HttpClient.postProject(
        project : Project? = createDefaultProject()
    ) : HttpResponse {
        return post("/projects") {
            contentType(ContentType.Application.Json)
            setBody(project ?: createDefaultProject())
        }
    }

    fun createDefaultProject() : Project {
        return ProjectBuilder().createDefault()
    }

    private suspend fun assertPostResponse(response : HttpResponse) {
        assertEquals(HttpStatusCode.Companion.Created, response.status)
        assertNotNull(response.body())
    }

    private suspend inline fun HttpClient.getProject(
        id : UUID
    ) : Project {
        val response = get("/projects/$id")
        assertEquals(HttpStatusCode.Companion.OK, response.status)
        return response.body()
    }

    @Test
    fun createProject_ThenUpdateProject_ProjectShouldBeUpdated() = testApplication {
        createEnvironment()

        val postResponse = client.postProject()
        assertPostResponse(postResponse)

        val receivedId = UUID.fromString(postResponse.bodyAsText())

        val project = client.getProject(receivedId)
        assertEquals(DEFAULT_PROJECT_NAME, project.name)
        assertEquals(DEFAULT_PROJECT_RATING, project.rating)

        val putResponse = client.updateProject(
            id = receivedId,
            project = ProjectBuilder().createWithValidProperties(
                name = UPDATED_PROJECT_NAME,
                rating = UPDATED_PROJECT_RATING,
            )
        )
        assertEquals(HttpStatusCode.Companion.OK, putResponse.status)

        val updatedProject = client.getProject(receivedId)
        assertEquals(UPDATED_PROJECT_NAME, updatedProject.name)
        assertEquals(UPDATED_PROJECT_RATING, updatedProject.rating)
    }

    private suspend inline fun HttpClient.updateProject(
        id : UUID,
        project : Project
    ) : HttpResponse {
        return put("/projects/$id") {
            contentType(ContentType.Application.Json)
            setBody(project)
        }
    }

    @Test
    fun createProject_ThenDeleteProject_ShouldNotGetProject() = testApplication {
        createEnvironment()

        val postResponse = client.postProject()
        assertPostResponse(postResponse)
        val receivedId = UUID.fromString(postResponse.bodyAsText())

        val deleteResponse = client.deleteProject(receivedId)
        assertEquals(HttpStatusCode.Companion.OK, deleteResponse.status)

        val getResponse = client.get("/projects/$receivedId")
        assertEquals(HttpStatusCode.Companion.NotFound, getResponse.status)
    }

    private suspend inline fun HttpClient.deleteProject(
        id : UUID
    ) : HttpResponse {
        return delete("/projects/$id")
    }
}