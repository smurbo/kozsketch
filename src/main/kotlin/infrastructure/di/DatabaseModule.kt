package com.infrastructure.di

import com.infrastructure.DatabaseConnection
import com.infrastructure.DatabaseConnectionImpl
import io.ktor.server.application.ApplicationEnvironment
import org.koin.dsl.module

fun databaseModule(env : ApplicationEnvironment) = module {
    factory { DatabaseConnectionImpl(env) as DatabaseConnection }
}