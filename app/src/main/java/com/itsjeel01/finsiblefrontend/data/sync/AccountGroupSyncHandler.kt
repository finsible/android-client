package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupUpdateRequest
import com.itsjeel01.finsiblefrontend.data.repository.AccountGroupRepository
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/** Handles AccountGroup sync operations. */
@Singleton
class AccountGroupSyncHandler @Inject constructor(
    private val repository: AccountGroupRepository,
    private val localRepository: AccountGroupLocalRepository,
    private val json: Json
) : EntitySyncHandler {

    override val entityType: EntityType = EntityType.ACCOUNT_GROUP

    override suspend fun processCreate(operation: PendingOperationEntity) {
        Logger.Sync.d("Processing CREATE: localId=${operation.localEntityId}")

        val request = json.decodeFromString<AccountGroupCreateRequest>(operation.payload)

        try {
            val response = repository.createAccountGroup(request)

            if (response.success) {
                val serverAccountGroup = response.data
                val entity = serverAccountGroup.toEntity(Status.COMPLETED)

                localRepository.remapId(
                    oldId = operation.localEntityId,
                    newId = serverAccountGroup.id,
                    updatedEntity = entity
                )

                Logger.Sync.i("CREATE success: local=${operation.localEntityId} → server=${serverAccountGroup.id}")
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

        val request = json.decodeFromString<AccountGroupUpdateRequest>(operation.payload)

        try {
            val response = repository.updateAccountGroup(operation.entityId, request)

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
            val response = repository.deleteAccountGroup(operation.entityId)

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

