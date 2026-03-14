package com.mappings.di

import com.data.schemas.Project
import com.mappings.ObjectMapper
import com.mappings.projects.ProjectMapper
import com.models.ProjectModel
import org.koin.dsl.module

val mappingModule = module {
    single<ObjectMapper<Project, ProjectModel>> { ProjectMapper() }
}