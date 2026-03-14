package com.repositories

import com.dataTransferring.mappings.ObjectMapper
import com.infrastructure.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

interface Repository<out TEntity: UUIDEntity, TModel> {
    suspend fun create(model: TModel) : UUID
    suspend fun getById(id: UUID) : TModel?
    suspend fun update(id: UUID, model: TModel)
    suspend fun delete(id: UUID)
}

abstract class RepositoryImpl<TEntity: UUIDEntity, TModel>(
    protected val db : DatabaseConnection,
    protected val mainTable : UUIDTable,
    protected val entityClass : EntityClass<UUID, TEntity>
) : Repository<TEntity, TModel>, KoinComponent {
    init {
        transaction(db.database) {
            SchemaUtils.create(mainTable)
        }
    }

    private val mapper: ObjectMapper<TEntity, TModel> by inject()

    override suspend fun create(model: TModel): UUID = dbQuery {
        mainTable.insert {
            insertModel(it, model)
        }[mainTable.id].value
    }

    abstract fun insertModel(it: InsertStatement<Number>, model: TModel)

    override suspend fun getById(id: UUID): TModel? {
        val entity = dbQuery {
            entityClass.findById(id)
        }
        return entity?.let { mapper.map(it) }
    }

    override suspend fun update(
        id: UUID,
        model: TModel
    ) {
        dbQuery {
            mainTable.update({ mainTable.id eq id }) {
                updateModel(it, model)
            }
        }
    }

    abstract fun updateModel(
        it: UpdateStatement,
        model: TModel)

    override suspend fun delete(id: UUID) {
        dbQuery {
            mainTable.deleteWhere { mainTable.id.eq(mainTable.id) }
        }
    }

    protected suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO, db.database) { block() }
}