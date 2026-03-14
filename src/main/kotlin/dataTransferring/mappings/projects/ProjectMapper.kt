package com.dataTransferring.mappings.projects

import com.data.Project
import com.dataTransferring.mappings.ObjectMapper
import com.models.ProjectModel

class ProjectMapper : ObjectMapper<Project, ProjectModel> {
    override fun map(source: Project): ProjectModel {
        return ProjectModel(
            id = source.id.value,
            name = source.name,
            rating = source.rating
        )
    }
}