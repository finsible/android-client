package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import retrofit2.HttpException

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

    /** Default HTTP exception handling for sync operations. */
    fun handleHttpException(e: HttpException): SyncException {
        return when (e.code()) {
            401 -> SyncException.unauthorized()
            404 -> SyncException.notFound()
            409 -> SyncException.conflict("Server has conflicting changes")
            else -> SyncException.serverError(e.code(), e.message())
        }
    }
}