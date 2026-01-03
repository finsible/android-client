package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity

/** Generic interface for entity-specific sync operations. Implement for each syncable entity. */
interface EntitySyncHandler {
    /** Which entity type this handler processes. */
    val entityType: EntityType

    /** Process CREATE - call ApiService, then LocalRepository.remapId(). */
    suspend fun processCreate(operation: PendingOperationEntity)

    /** Process UPDATE - call ApiService, then LocalRepository.upsert(). */
    suspend fun processUpdate(operation: PendingOperationEntity)

    /** Process DELETE - call ApiService, then LocalRepository.remove(). */
    suspend fun processDelete(operation: PendingOperationEntity)
}