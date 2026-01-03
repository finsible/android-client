package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.api.TransactionApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionUpdateRequest
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/** Handles Transaction sync operations. Pattern for other entity handlers. */
@Singleton
class TransactionSyncHandler @Inject constructor(
    private val apiService: TransactionApiService,
    private val localRepository: TransactionLocalRepository,
    private val json: Json
) : EntitySyncHandler {

    override val entityType: EntityType = EntityType.TRANSACTION

    override suspend fun processCreate(operation: PendingOperationEntity) {
        Logger.Sync.d("Processing CREATE: localId=${operation.localEntityId}")

        val request = json.decodeFromString<TransactionCreateRequest>(operation.payload)

        try {
            val response = apiService.createTransaction(request)

            if (response.success) {
                val serverTx = response.data
                val entity = serverTx.toEntity(Status.COMPLETED)

                localRepository.remapId(
                    oldId = operation.localEntityId,
                    newId = serverTx.id,
                    updatedEntity = entity
                )

                Logger.Sync.i("CREATE success: local=${operation.localEntityId} → server=${serverTx.id}")
            } else {
                throw SyncException.serverError(0, response.message)
            }
        } catch (e: IOException) {
            throw SyncException.networkError(e)
        } catch (e: HttpException) {
            throw handleHttpException(e)
        }
    }

    override suspend fun processUpdate(operation: PendingOperationEntity) {
        Logger.Sync.d("Processing UPDATE: id=${operation.entityId}")

        val request = json.decodeFromString<TransactionUpdateRequest>(operation.payload)

        try {
            val response = apiService.updateTransaction(operation.entityId, request)

            if (response.success) {
                val entity = response.data.toEntity(Status.COMPLETED)
                localRepository.upsert(entity)

                Logger.Sync.i("UPDATE success: id=${operation.entityId}")
            } else {
                throw SyncException.serverError(0, response.message)
            }
        } catch (e: IOException) {
            throw SyncException.networkError(e)
        } catch (e: HttpException) {
            throw handleHttpException(e)
        }
    }

    override suspend fun processDelete(operation: PendingOperationEntity) {
        Logger.Sync.d("Processing DELETE: id=${operation.entityId}")

        try {
            val response = apiService.deleteTransaction(operation.entityId)

            if (response.success) {
                localRepository.removeById(operation.entityId)
                Logger.Sync.i("DELETE success: id=${operation.entityId}")
            } else {
                throw SyncException.serverError(0, response.message)
            }
        } catch (e: IOException) {
            throw SyncException.networkError(e)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> {
                    // Already deleted on server - remove locally and succeed
                    localRepository.removeById(operation.entityId)
                    Logger.Sync.w("DELETE 404: already deleted on server, removing local")
                }

                else -> throw handleHttpException(e)
            }
        }
    }

    private fun handleHttpException(e: HttpException): SyncException {
        return when (e.code()) {
            401 -> SyncException.unauthorized()
            404 -> SyncException.notFound()
            409 -> SyncException.conflict("Server has conflicting changes")
            else -> SyncException.serverError(e.code(), e.message())
        }
    }
}