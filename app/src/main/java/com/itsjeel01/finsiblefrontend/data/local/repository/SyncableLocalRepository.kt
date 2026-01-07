package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.StatusConverter
import com.itsjeel01.finsiblefrontend.data.local.entity.BaseEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncableEntity
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.kotlin.equal
import io.objectbox.query.QueryBuilder
import kotlinx.serialization.json.Json

abstract class SyncableLocalRepository<DTO, Entity>(
    override val box: Box<Entity>,
    protected val pendingOperationBox: Box<PendingOperationEntity>,
    protected val localIdGenerator: LocalIdGenerator
) : BaseLocalRepository<DTO, Entity>
        where Entity : BaseEntity, Entity : SyncableEntity {

    protected abstract val entityType: EntityType

    fun getPendingEntities(): List<Entity> {
        return box.query()
            .equal(syncStatusProperty(), StatusConverter().convertToDatabaseValue(Status.PENDING)!!)
            .build()
            .find()
    }

    fun getFailedEntities(): List<Entity> {
        return box.query()
            .equal(syncStatusProperty(), StatusConverter().convertToDatabaseValue(Status.FAILED)!!)
            .build()
            .find()
    }

    fun getLocalOnlyEntities(): List<Entity> {
        return box.query()
            .less(idProperty(), 0)
            .build()
            .find()
    }

    fun updateSyncStatus(id: Long, status: Status, error: String? = null) {
        val entity = box.get(id)

        entity?.let {
            it.syncStatus = status
            it.syncError = error
            it.lastSyncAttempt = if (status == Status.SYNCING || status == Status.FAILED) {
                System.currentTimeMillis()
            } else {
                it.lastSyncAttempt
            }
            box.put(it)
            Logger.Database.d("Updated sync status for ${entityType.name} $id: $status")
        }
    }

    fun remapId(oldId: Long, newId: Long, updatedEntity: Entity) {
        box.remove(oldId)

        updatedEntity.syncStatus = Status.COMPLETED
        updatedEntity.syncError = null
        box.put(updatedEntity)

        Logger.Database.i("Remapped ${entityType.name} ID: $oldId → $newId")
    }

    protected fun queueCreateOperation(localEntityId: Long, payload: String) {
        queueOperation(
            operationType = OperationType.CREATE,
            localEntityId = localEntityId,
            payload = payload
        )
    }

    /** Queue CREATE operation with automatic JSON serialization. */
    protected inline fun <reified T> queueCreate(localEntityId: Long, request: T) {
        queueCreateOperation(localEntityId, Json.encodeToString(request))
    }

    protected fun queueUpdateOperation(entityId: Long, payload: String) {
        queueOperation(
            operationType = OperationType.UPDATE,
            entityId = entityId,
            payload = payload
        )
    }

    /** Queue UPDATE operation with automatic JSON serialization. */
    protected inline fun <reified T> queueUpdate(entityId: Long, request: T) {
        queueUpdateOperation(entityId, Json.encodeToString(request))
    }

    protected fun queueDeleteOperation(entityId: Long) {
        queueOperation(
            operationType = OperationType.DELETE,
            entityId = entityId
        )
    }

    private fun queueOperation(
        operationType: OperationType,
        entityId: Long = 0,
        localEntityId: Long = 0,
        payload: String = ""
    ) {
        val operation = PendingOperationEntity(
            entityType = entityType,
            operationType = operationType,
            entityId = entityId,
            localEntityId = localEntityId,
            payload = payload,
            status = Status.PENDING,
            createdAt = System.currentTimeMillis()
        )
        pendingOperationBox.put(operation)
        Logger.Database.d("Queued $operationType for ${entityType.name}")
    }


    fun upsert(entity: Entity) {
        box.put(entity)
        Logger.Database.d("Upserted ${entityType.name} ${entity.id}")
    }

    fun removeById(id: Long) {
        box.remove(id)
        Logger.Database.d("Removed ${entityType.name} $id")
    }

    fun clearAll() {
        box.removeAll()
        Logger.Database.i("Cleared all ${entityType.name} data")
    }

    protected fun replaceMatching(
        queryBuilder: QueryBuilder<Entity>,
        newEntities: List<Entity>
    ) {
        // Get all matching entities
        val matchingEntities = queryBuilder.build().find()

        // Remove only non-pending entities
        matchingEntities.forEach { entity ->
            if (entity.syncStatus != Status.PENDING) {
                box.remove(entity.id)
            }
        }

        box.put(newEntities)

        Logger.Database.i("Replaced ${newEntities.size} ${entityType.name} entities")
    }

    /**
     * Delete an entity with sync-aware logic:
     * - Local-only (negative ID): remove immediately + clear pending CREATE ops
     * - Server-synced (positive ID): queue DELETE operation
     */
    protected fun deleteSyncAware(id: Long): Boolean {
        val entity = box.get(id) ?: return false

        // Local-only: remove immediately
        if (id < 0) {
            box.remove(id)
            // Clear any pending CREATE operation for this local ID
            val pendingOps = pendingOperationBox.all.filter { it.localEntityId == id }
            pendingOps.forEach { pendingOperationBox.remove(it) }
            Logger.Database.i("Deleted local-only ${entityType.name}: id=$id")
            return true
        }

        // Server-synced: queue delete operation
        queueDeleteOperation(id)

        // Optionally mark as pending-delete
        entity.syncStatus = Status.PENDING
        box.put(entity)

        Logger.Database.i("Queued ${entityType.name} deletion: id=$id")
        return true
    }

    /** Property ID for the entity's ID field (for ObjectBox queries). */
    protected abstract fun idProperty(): io.objectbox.Property<Entity>

    /** Property ID for the entity's syncStatus field (for ObjectBox queries). */
    protected abstract fun syncStatusProperty(): io.objectbox.Property<Entity>
}

