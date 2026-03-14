package com.utils

import io.ktor.server.config.ApplicationConfig

fun ApplicationConfig.getBooleanFromConfigOrDefault(
    path: String,
    fallback: Boolean
) : Boolean {
    try {
        return property(path)
            .getString()
            .toBoolean()
    } catch (_: Exception) {
        return fallback
    }
}