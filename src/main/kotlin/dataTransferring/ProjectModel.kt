package com.models

import com.dataTransferring.serializers.NullableUUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProjectModel(
    @Serializable(with = NullableUUIDSerializer::class)
    val id: UUID? = null,
    val name: String,
    val rating: String
)