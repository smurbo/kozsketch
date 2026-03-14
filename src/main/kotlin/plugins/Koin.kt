package com.plugins

import com.infrastructure.di.databaseModule
import com.repositories.di.repositoryModule
import com.services.di.serviceModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.IgnoreTrailingSlash
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(IgnoreTrailingSlash)

    install(Koin) {
        slf4jLogger()
        modules(
            databaseModule(environment),
            repositoryModule,
            serviceModule)
    }
}