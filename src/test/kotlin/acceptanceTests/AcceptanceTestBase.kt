package com.acceptanceTests

import com.module
import io.ktor.client.HttpClient
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
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class AcceptanceTestBase {
    protected abstract val partialUrl: String

    protected suspend fun ApplicationTestBuilder.createEnvironmentWithDefaults() {
        createEnvironment().also { setupDefaults(client) }
    }

    protected fun ApplicationTestBuilder.createEnvironment() {
        application {
            module()
        }
        client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    protected abstract suspend fun setupDefaults(client: HttpClient)

    protected suspend inline fun <reified T> HttpClient.create(
        entity : T
    ) : HttpResponse {
        return post(partialUrl) {
            contentType(ContentType.Application.Json)
            setBody(entity)
        }
    }

    protected suspend inline fun <reified T> HttpClient.createAndGetId(
        entity : T
    ) : UUID {
        val response = create(entity)
        ensureValidCreatedResponse(response)
        return UUID.fromString(response.bodyAsText())
    }

    protected suspend fun ensureValidCreatedResponse(response: HttpResponse) {
        assertEquals(HttpStatusCode.Created, response.status)
        assertNotNull(response.body())
    }

    protected suspend inline fun <reified T> HttpClient.get(
        id : UUID
    ) : T {
        val response = get("$partialUrl/$id")
        assertEquals(HttpStatusCode.OK, response.status)
        return response.body() as T
    }

    protected suspend inline fun <reified T> HttpClient.update(
        id : UUID,
        entity : T
    ) {
        val response = put("$partialUrl/$id") {
            contentType(ContentType.Application.Json)
            setBody(entity)
        }
        ensureValidUpdatedResponse(response)
    }

    protected fun ensureValidUpdatedResponse(response: HttpResponse) {
        assertEquals(HttpStatusCode.OK, response.status)
    }

    protected suspend inline fun HttpClient.delete(
        id : UUID
    ) : HttpResponse {
        return delete("$partialUrl/$id")
    }
}