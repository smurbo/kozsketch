package com.repositories.projects

import com.data.schemas.Project
import com.data.schemas.Projects
import com.data.DatabaseConnection
import com.models.ProjectModel
import com.repositories.RepositoryImpl
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

class ProjectRepository(
    db : DatabaseConnection,
) : RepositoryImpl<Project, ProjectModel>(
    db,
    Projects,
    Project.Companion) {
    override fun insertModel(
        it: InsertStatement<Number>,
        model: ProjectModel
    ) {
        it[Projects.name] = model.name
        it[Projects.rating] = model.rating
    }

    override fun updateModel(
        it: UpdateStatement,
        model: ProjectModel
    ) {
        it[Projects.name] = model.name
        it[Projects.rating] = model.rating
    }
}