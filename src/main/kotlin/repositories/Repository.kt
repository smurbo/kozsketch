package com.repositories

import com.data.Project
import com.data.Projects
import com.infrastructure.DatabaseConnection
import com.models.ProjectModel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

interface Repository<TEntity, TModel> {
    suspend fun create(model: TModel) : UUID
    suspend fun getById(id: UUID) : TEntity?
    suspend fun update(id: UUID, model: TModel)
    suspend fun delete(id: UUID)
}

abstract class RepositoryImpl<TEntity, TModel>(
    protected val db : DatabaseConnection
) : Repository<TEntity, TModel> {
    init {
        transaction(db.database) {
            SchemaUtils.create(Projects)
        }
    }

    //create

    //really dangerous lmao dw about it
    override suspend fun getById(id: UUID): TEntity? = dbQuery {
        Project.findById(id) as TEntity?
    }

    override suspend fun delete(id: UUID) {
        dbQuery {
            Projects.deleteWhere { Projects.id.eq(Projects.id) }
        }
    }

    protected suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO, db.database) { block() }
}

class ProjectRepository(db : DatabaseConnection) : RepositoryImpl<Project, ProjectModel>(db) {
    override suspend fun create(model: ProjectModel): UUID = dbQuery {
        Projects.insert {
            it[name] = model.name
            it[rating] = model.rating
        }[Projects.id].value
    }

    override suspend fun update(id: UUID, model: ProjectModel) {
        dbQuery {
            Projects.update({ Projects.id eq id }) {
                it[name] = model.name
                it[rating] = model.rating
            }
        }
    }
}