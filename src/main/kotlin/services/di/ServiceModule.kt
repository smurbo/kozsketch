package com.services.di

import com.models.ProjectModel
import com.services.Service
import com.services.projects.ProjectService
import org.koin.dsl.module

val serviceModule = module {
    single { ProjectService(repository = get()) }
    single<Service<ProjectModel>> { get<ProjectService>() }
}