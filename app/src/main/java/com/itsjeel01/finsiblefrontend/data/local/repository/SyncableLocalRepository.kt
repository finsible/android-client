package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.BaseEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncableEntity
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import kotlinx.serialization.json.Json

abstract class SyncableLocalRepository<DTO, Entity>(
    override val box: Box<Entity>,
    protected val pendingOperationBox: Box<PendingOperationEntity>,
    protected val localIdGenerator: LocalIdGenerator
) : BaseLocalRepository<DTO, Entity>
        where Entity : BaseEntity, Entity : SyncableEntity {

    protected abstract val entityType: EntityType

    /** Convert entity to CREATE request for serialization. */
    protected abstract fun toCreateRequest(entity: Entity): Any

    /** Convert entity to UPDATE request for serialization. */
    protected abstract fun toUpdateRequest(entity: Entity): Any

    /**
     * Create entity locally and queue for sync. Returns entity with assigned local ID.
     *
     * @param entityFactory Function that creates entity with provided local ID
     * @return Created entity with local ID and Status.PENDING
     */
    fun queueCreateEntity(entityFactory: (localId: Long) -> Entity): Entity {
        val localId = localIdGenerator.nextLocalId()
        val entity = entityFactory(localId).apply {
            syncStatus = Status.PENDING
        }

        box.put(entity)

        val request = toCreateRequest(entity)
        queueOperation(
            operationType = OperationType.CREATE,
            localEntityId = localId,
            payload = Json.encodeToString(request)
        )

        Logger.Database.i("Created local ${entityType.name}: id=$localId")
        return entity
    }

    /**
     * Update entity locally and queue for sync (only for server-synced entities with positive ID).
     *
     * @param entity Entity to update (must have positive ID for sync queueing)
     * @return Updated entity or null if not found
     */
    fun queueUpdateEntity(entity: Entity): Entity {
        if (entity.id <= 0) {
            // Local-only: just update, don't queue
            box.put(entity.apply { syncStatus = Status.PENDING })
            Logger.Database.i("Updated local-only ${entityType.name}: id=${entity.id}")
            return entity
        }

        // Server-synced: queue update
        entity.syncStatus = Status.PENDING
        box.put(entity)

        val request = toUpdateRequest(entity)
        queueOperation(
            operationType = OperationType.UPDATE,
            entityId = entity.id,
            payload = Json.encodeToString(request)
        )

        Logger.Database.i("Updated ${entityType.name}: id=${entity.id}")
        return entity
    }

    /**
     * Delete entity with sync-aware logic.
     * - Local-only (negative ID): remove immediately + clear pending CREATE ops
     * - Server-synced (positive ID): queue DELETE operation
     *
     * @param id Entity ID to delete
     * @return true if deleted/queued, false if not found
     */
    fun queueDeleteEntity(id: Long): Boolean {
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
        queueOperation(
            operationType = OperationType.DELETE,
            entityId = id
        )

        // Optionally mark as pending-delete
        entity.syncStatus = Status.PENDING
        box.put(entity)

        Logger.Database.i("Queued ${entityType.name} deletion: id=$id")
        return true
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


    /** Property ID for the entity's ID field (for ObjectBox queries). */
    protected abstract fun idProperty(): io.objectbox.Property<Entity>

    /** Property ID for the entity's syncStatus field (for ObjectBox queries). */
    protected abstract fun syncStatusProperty(): io.objectbox.Property<Entity>
}

