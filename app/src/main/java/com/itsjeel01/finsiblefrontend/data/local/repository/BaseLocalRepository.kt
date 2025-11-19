package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.data.local.entity.BaseEntity
import io.objectbox.Box

interface BaseLocalRepository<DTO, Entity : BaseEntity> {
    val box: Box<Entity>

    fun get(id: Long): Entity {
        return box.get(id)
    }

    fun add(entity: Entity) {
        box.put(entity)
    }

    fun remove(entity: Entity) {
        box.remove(entity.id)
    }

    fun addAll(data: List<DTO>, additionalInfo: Any? = null, ttlMinutes: Long? = null)

    fun getAll(): List<Entity> {
        return box.all
    }

    fun isStale(): Boolean {
        return box.isEmpty || box.all.any { it.isStale() }
    }

    fun syncToServer(entity: Entity)
}