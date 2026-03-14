package com.services

import com.data.Project
import com.models.ProjectModel
import com.repositories.Repository
import java.util.UUID

interface Service<TModel> {
    suspend fun create(model: TModel) : UUID
    suspend fun getById(id: UUID) : TModel?
    suspend fun update(id: UUID, model: TModel)
    suspend fun delete(id: UUID)
}

class ProjectService(
    val repository: Repository<Project, ProjectModel>
) : Service<ProjectModel> {
    override suspend fun create(model: ProjectModel): UUID {
        return repository.create(model)
    }

    override suspend fun getById(id: UUID): ProjectModel? {
        val project = repository.getById(id)

        if (project != null) {
            return ProjectModel(
                project.id.value,
                project.name,
                project.rating
            )
        }
        else return null
    }

    override suspend fun update(id: UUID, model: ProjectModel) {
        repository.update(id, model)
    }

    override suspend fun delete(id: UUID) {
        repository.delete(id)
    }
}