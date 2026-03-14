package com.services.di

import com.models.ProjectModel
import com.services.ProjectService
import com.services.Service
import org.koin.dsl.module

val serviceModule = module {
    single { ProjectService(repository = get()) }
    single<Service<ProjectModel>> { get<ProjectService>() }
}