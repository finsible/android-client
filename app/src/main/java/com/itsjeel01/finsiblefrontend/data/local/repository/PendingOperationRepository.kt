package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.EntityTypeConverter
import com.itsjeel01.finsiblefrontend.data.local.StatusConverter
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity_
import io.objectbox.Box
import io.objectbox.kotlin.equal
import javax.inject.Inject

class PendingOperationRepository @Inject constructor(
    private val box: Box<PendingOperationEntity>
) {
    fun add(operation: PendingOperationEntity) {
        operation.createdAt = System.currentTimeMillis()
        box.put(operation)
        Logger.Database.d("Added pending operation: ${operation.operationType} ${operation.entityType}")
    }

    fun getPending(): List<PendingOperationEntity> {
        return box.query()
            .equal(PendingOperationEntity_.status, StatusConverter().convertToDatabaseValue(Status.PENDING)!!)
            .order(PendingOperationEntity_.createdAt)
            .build()
            .find()
    }

    fun getFailed(): List<PendingOperationEntity> {
        return box.query()
            .equal(
                PendingOperationEntity_.status,
                StatusConverter().convertToDatabaseValue(Status.FAILED)!!
            )
            .build()
            .find()
    }

    fun getPendingCount(): Long {
        return box.query()
            .equal(PendingOperationEntity_.status, StatusConverter().convertToDatabaseValue(Status.PENDING)!!)
            .build()
            .count()
    }

    fun getForEntity(entityType: EntityType, entityId: Long): PendingOperationEntity? {
        return box.query()
            .equal(PendingOperationEntity_.entityType, EntityTypeConverter().convertToDatabaseValue(entityType)!!)
            .equal(PendingOperationEntity_.entityId, entityId)
            .equal(PendingOperationEntity_.status, StatusConverter().convertToDatabaseValue(Status.PENDING)!!)
            .build()
            .findFirst()
    }

    fun removeByLocalEntityId(localEntityId: Long) {
        box.query()
            .equal(PendingOperationEntity_.localEntityId, localEntityId)
            .build()
            .remove()
        Logger.Database.d("Removed pending operation for local entity $localEntityId")
    }

    fun update(operation: PendingOperationEntity) {
        box.put(operation)
    }

    fun removeCompleted() {
        box.query()
            .equal(PendingOperationEntity_.status, StatusConverter().convertToDatabaseValue(Status.COMPLETED)!!)
            .build()
            .remove()
    }
}