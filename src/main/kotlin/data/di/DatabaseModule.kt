package com.data.di

import com.data.DatabaseConnection
import com.data.DatabaseConnectionImpl
import io.ktor.server.application.ApplicationEnvironment
import org.koin.dsl.module

fun databaseModule(env : ApplicationEnvironment) = module {
    factory { DatabaseConnectionImpl(env) as DatabaseConnection }
}