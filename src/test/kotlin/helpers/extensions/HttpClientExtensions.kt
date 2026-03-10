package com.helpers.extensions

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.util.UUID

suspend inline fun <reified T> HttpClient.create(
    partialUrl: String,
    body: T
) : HttpResponse {
    return post(partialUrl) {
        contentType(ContentType.Application.Json)
        setBody(body)
    }
}

suspend inline fun <reified T> HttpClient.getById(
    partialUrl: String,
    id: UUID
) : T {
    return get("$partialUrl/$id").body<T>()
}

suspend inline fun <reified T> HttpClient.update(
    partialUrl: String,
    id: UUID,
    body: T
) : HttpResponse {
    return put("$partialUrl/$id") {
        contentType(ContentType.Application.Json)
        setBody(body)
    }
}

suspend fun HttpClient.delete(
    partialUrl: String,
    id: UUID
) : HttpResponse {
    return delete("$partialUrl/$id")
}