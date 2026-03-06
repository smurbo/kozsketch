package com.infrastructure

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

@Serializable
data class Project(
    val name: String,
    val rating: String)

class ProjectService(database: Database) {
    object Projects : Table() {
        val id = uuid("id").autoGenerate()
        val name = varchar("name", length = 50)
        val rating = varchar("rating", 50)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Projects)
        }
    }

    suspend fun create(project: Project): UUID = dbQuery {
        Projects.insert {

            it[name] = project.name
            it[rating] = project.rating
        }[Projects.id]
    }

    suspend fun read(id: UUID): Project? = dbQuery {
        dbQuery {
            Projects.selectAll()
                .where { Projects.id eq id }
                .map { Project(
                    it[Projects.name],
                    it[Projects.rating]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: UUID, project: Project) {
        dbQuery {
            Projects.update({ Projects.id eq id }) {
                it[name] = project.name
                it[rating] = project.rating
            }
        }
    }

    suspend fun delete(id: UUID) {
        dbQuery {
            Projects.deleteWhere { Projects.id.eq(id) }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

