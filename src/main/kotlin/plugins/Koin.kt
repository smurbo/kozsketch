package com.plugins

import com.mappings.di.mappingModule
import com.data.di.databaseModule
import com.repositories.di.repositoryModule
import com.services.di.serviceModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            databaseModule(environment),
            mappingModule,
            repositoryModule,
            serviceModule)
    }
}