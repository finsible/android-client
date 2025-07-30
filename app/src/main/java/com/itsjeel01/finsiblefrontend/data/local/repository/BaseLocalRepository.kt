package com.itsjeel01.finsiblefrontend.data.local.repository

interface BaseLocalRepository<Model, Entity> {
    fun add(entity: Entity)

    fun remove(entity: Entity)

    fun addAll(models: List<Model>, additionalInfo: Any? = null, ttlMinutes: Long? = null)

    fun getAll(): List<Entity>

    fun isStale(): Boolean

    fun syncToServer(entity: Entity)
}