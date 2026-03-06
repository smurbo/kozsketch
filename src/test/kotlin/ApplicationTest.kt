package com

import com.infrastructure.Project
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", body())
        }
    }

    @Test
    fun createProject_ShouldCreateProject() = testApplication {
        application {
            module()
        }

        var receivedId = String

        client.post("/projects") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.ContentDisposition, "charset=utf-8")
            setBody(Project("test", "not sketch"))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertNotNull(body());
            receivedId = body()
        }
//
//        client.get("/projects/$receivedId").apply {
//            assertEquals(HttpStatusCode.OK, status)
//            assertNotNull(body());
//        }
    }
}
