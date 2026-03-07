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
    fun shouldCreateProject_ThenShouldGetProject() = testApplication {
        createEnvironment()

        val client = createClient() {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/projects") {
            contentType(ContentType.Application.Json)
            setBody(Project("TestProject", "NotSketch"))
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertNotNull(response.body())

        val receivedId = UUID.fromString(response.bodyAsText())

        val getResponse = client.get("/projects/$receivedId")
        val project : Project = getResponse.body()

        assertEquals(HttpStatusCode.OK, getResponse.status)
        assertEquals("TestProject", project.name)
        assertEquals("NotSketch", project.rating)
    }
}
