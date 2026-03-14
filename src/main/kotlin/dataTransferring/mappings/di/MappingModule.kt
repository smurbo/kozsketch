package com.dataTransferring.mappings.di

import com.data.Project
import com.dataTransferring.mappings.ObjectMapper
import com.dataTransferring.mappings.projects.ProjectMapper
import com.models.ProjectModel
import org.koin.dsl.module

val mappingModule = module {
    single<ObjectMapper<Project, ProjectModel>> { ProjectMapper() }
}