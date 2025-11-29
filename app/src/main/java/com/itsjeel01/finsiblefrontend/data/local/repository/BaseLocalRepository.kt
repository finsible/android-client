package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.BaseEntity
import io.objectbox.Box

interface BaseLocalRepository<DTO, Entity : BaseEntity> {
    val box: Box<Entity>

    fun get(id: Long): Entity {
        Logger.Database.d("Fetching ${entityName()} entity with id=$id")
        return box.get(id)
    }

    fun add(entity: Entity) {
        Logger.Database.d("Adding ${entityName()} entity id=${entity.id}")
        box.put(entity)
    }

    fun remove(entity: Entity) {
        Logger.Database.d("Removing ${entityName()} entity id=${entity.id}")
        box.remove(entity.id)
    }

    fun addAll(data: List<DTO>, additionalInfo: Any? = null, ttlMinutes: Long? = null) {
        Logger.Database.d("Adding ${data.size} ${entityName()} entities")
    }

    fun getAll(): List<Entity> {
        val entities = box.all
        Logger.Database.d("Fetched ${entities.size} ${entityName()} entities")
        return entities
    }

    fun isStale(): Boolean {
        val entities = box.all
        val empty = entities.isEmpty()
        val hasStale = entities.any { it.isStale() }

        Logger.Database.d("Staleness check for ${entityName()} entity: empty=$empty, hasStale=$hasStale")
        return empty || hasStale
    }

    fun syncToServer(entity: Entity)

    fun entityName(): String = box.entityInfo.entityName
}