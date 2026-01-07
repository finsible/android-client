package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountUpdateRequest
import com.itsjeel01.finsiblefrontend.data.repository.AccountRepository
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/** Handles Account sync operations. */
@Singleton
class AccountSyncHandler @Inject constructor(
    private val repository: AccountRepository,
    private val localRepository: AccountLocalRepository,
    private val json: Json
) : EntitySyncHandler {

    override val entityType: EntityType = EntityType.ACCOUNT

    override suspend fun processCreate(operation: PendingOperationEntity) {
        Logger.Sync.d("Processing CREATE: localId=${operation.localEntityId}")

        val request = json.decodeFromString<AccountCreateRequest>(operation.payload)

        try {
            val response = repository.createAccount(request)

            if (response.success) {
                val serverAccount = response.data
                val entity = serverAccount.toEntity(Status.COMPLETED)

                // Preserve accountGroupId relationship
                request.accountGroupId?.let {
                    entity.accountGroup.targetId = it
                }

                localRepository.remapId(
                    oldId = operation.localEntityId,
                    newId = serverAccount.id,
                    updatedEntity = entity
                )

                Logger.Sync.i("CREATE success: local=${operation.localEntityId} → server=${serverAccount.id}")
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

        val request = json.decodeFromString<AccountUpdateRequest>(operation.payload)

        try {
            val response = repository.updateAccount(operation.entityId, request)

            if (response.success) {
                val entity = response.data.toEntity(Status.COMPLETED)

                // Preserve accountGroupId relationship
                response.data.accountGroupId?.let {
                    entity.accountGroup.targetId = it
                }

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
            val response = repository.deleteAccount(operation.entityId)

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

