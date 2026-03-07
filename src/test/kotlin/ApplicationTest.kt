package com

import com.infrastructure.Project
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ApplicationTest {

    fun ApplicationTestBuilder.createEnvironment() {
        application {
            module()
        }
    }

    @Test
    fun shouldCreateProject_ProjectShouldBeCreated() = testApplication {
        createEnvironment()

        val client = createClient() {
            install(ContentNegotiation) {
                json()
            }
        }

        val postResponse = client.post("/projects") {
            contentType(ContentType.Application.Json)
            setBody(Project("TestProject", "NotSketch"))
        }

        assertEquals(HttpStatusCode.Created, postResponse.status)
        assertNotNull(postResponse.body())

        val receivedId = UUID.fromString(postResponse.bodyAsText())

        val getResponse = client.get("/projects/$receivedId")
        val project : Project = getResponse.body()

        assertEquals(HttpStatusCode.OK, getResponse.status)
        assertEquals("TestProject", project.name)
        assertEquals("NotSketch", project.rating)
    }

    @Test
    fun createProject_ThenUpdateProject_ProjectShouldBeUpdated() = testApplication {
        createEnvironment()

        val client = createClient() {
            install(ContentNegotiation) {
                json()
            }
        }

        val postResponse = client.post("/projects") {
            contentType(ContentType.Application.Json)
            setBody(Project("TestProject", "NotSketch"))
        }

        assertEquals(HttpStatusCode.Created, postResponse.status)
        assertNotNull(postResponse.body())

        val receivedId = UUID.fromString(postResponse.bodyAsText())

        val putResponse = client.put("/projects/$receivedId") {
            contentType(ContentType.Application.Json)
            setBody(Project("NewNameForTestProject", "ActuallySketch"))
        }

        assertEquals(HttpStatusCode.OK, putResponse.status)

        val getResponse = client.get("/projects/$receivedId")
        val project : Project = getResponse.body()

        assertEquals(HttpStatusCode.OK, getResponse.status)
        assertEquals("NewNameForTestProject", project.name)
        assertEquals("ActuallySketch", project.rating)
    }

    @Test
    fun createProject_ThenDeleteProject_ShouldNotGetProject() = testApplication {
        createEnvironment()

        val client = createClient() {
            install(ContentNegotiation) {
                json()
            }
        }

        val postResponse = client.post("/projects") {
            contentType(ContentType.Application.Json)
            setBody(Project("TestProject", "NotSketch"))
        }

        assertEquals(HttpStatusCode.Created, postResponse.status)
        assertNotNull(postResponse.body())

        val receivedId = UUID.fromString(postResponse.bodyAsText())

        val deleteResponse = client.delete("/projects/$receivedId")

        assertEquals(HttpStatusCode.OK, deleteResponse.status)

        val getResponse = client.get("/projects/$receivedId")

        assertEquals(HttpStatusCode.NotFound, getResponse.status)
    }
}
