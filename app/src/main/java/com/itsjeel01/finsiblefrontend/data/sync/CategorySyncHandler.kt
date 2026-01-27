package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryUpdateRequest
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/** Handles Category sync operations. */
@Singleton
class CategorySyncHandler @Inject constructor(
    private val repository: CategoryRepository,
    private val localRepository: CategoryLocalRepository,
    private val json: Json
) : EntitySyncHandler {

    override val entityType: EntityType = EntityType.CATEGORY

    override suspend fun processCreate(operation: PendingOperationEntity) {
        Logger.Sync.d("Processing CREATE: localId=${operation.localEntityId}")

        val request = json.decodeFromString<CategoryCreateRequest>(operation.payload)

        try {
            val response = repository.createCategory(request)

            if (response.success) {
                val serverCategory = response.data
                val type = TransactionType.valueOf(request.type)
                val entity = serverCategory.toEntity(type, Status.COMPLETED)

                localRepository.remapId(
                    oldId = operation.localEntityId,
                    newId = serverCategory.id,
                    updatedEntity = entity
                )

                Logger.Sync.i("CREATE success: local=${operation.localEntityId} → server=${serverCategory.id}")
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

        val request = json.decodeFromString<CategoryUpdateRequest>(operation.payload)

        try {
            val response = repository.updateCategory(operation.entityId, request)

            if (response.success) {
                // Get existing entity to preserve type
                val existingEntity = localRepository.get(operation.entityId)
                val entity = response.data.toEntity(existingEntity.type, Status.COMPLETED)
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
            val response = repository.deleteCategory(operation.entityId)

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
}

