package com.data

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

class Project(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Project>(Projects)
    var name by Projects.name
    var rating by Projects.rating
}

object Projects : UUIDTable() {
    val name = varchar("name", length = 50)
    val rating = varchar("rating", 50)
}