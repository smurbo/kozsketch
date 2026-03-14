package com.services

import com.repositories.Repository
import org.jetbrains.exposed.dao.UUIDEntity
import java.util.UUID

interface Service<TModel> {
    suspend fun create(model: TModel) : UUID
    suspend fun getById(id: UUID) : TModel?
    suspend fun update(id: UUID, model: TModel)
    suspend fun delete(id: UUID)
}

abstract class ServiceImpl<TModel>(
    val repository: Repository<UUIDEntity, TModel>
) : Service<TModel> {
    override suspend fun create(model: TModel): UUID {
        return repository.create(model)
    }

    override suspend fun getById(id: UUID): TModel? {
        return repository.getById(id)
    }

    override suspend fun update(id: UUID, model: TModel) {
        repository.update(id, model)
    }

    override suspend fun delete(id: UUID) {
        repository.delete(id)
    }
}