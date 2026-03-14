package com.repositories.di

import com.data.Project
import com.models.ProjectModel
import com.repositories.ProjectRepository
import com.repositories.Repository
import org.koin.dsl.module

val repositoryModule = module {
    single { ProjectRepository(db = get()) }
    single<Repository<Project, ProjectModel>> { get<ProjectRepository>() }
}