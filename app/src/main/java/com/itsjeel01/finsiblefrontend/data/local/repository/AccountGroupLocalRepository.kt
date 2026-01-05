package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity
import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupUpdateRequest
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.Property
import javax.inject.Inject

class AccountGroupLocalRepository @Inject constructor(
    override val box: Box<AccountGroupEntity>,
    syncMetadataBox: Box<SyncMetadataEntity>,
    pendingOperationBox: Box<PendingOperationEntity>,
    localIdGenerator: LocalIdGenerator
) : SyncableLocalRepository<AccountGroup, AccountGroupEntity>(
    box,
    syncMetadataBox,
    pendingOperationBox,
    localIdGenerator
) {

    override val entityType: EntityType = EntityType.ACCOUNT_GROUP
    override fun idProperty(): Property<AccountGroupEntity> = AccountGroupEntity_.id
    override fun syncStatusProperty(): Property<AccountGroupEntity> = AccountGroupEntity_.syncStatus

    override fun addAll(
        data: List<AccountGroup>,
        additionalInfo: Any?,
        ttlMinutes: Long?
    ) {
        super.addAll(data, additionalInfo, ttlMinutes)

        val entities = data.map { accountGroup ->
            accountGroup.toEntity().apply {
                updateCacheTime(ttlMinutes)
                syncStatus = Status.COMPLETED
            }
        }

        box.put(entities)
        Logger.Database.d("Added ${entities.size} account groups to local DB")
    }

    override fun syncToServer(entity: AccountGroupEntity) {
        Logger.Database.w("syncToServer called directly - use SyncManager instead")
    }

    /** Create account group locally and queue for sync. Returns immediately with local entity. */
    fun createAccountGroup(
        name: String,
        description: String,
        icon: String,
        color: String
    ): AccountGroupEntity {
        val localId = localIdGenerator.nextLocalId()

        val entity = AccountGroupEntity(
            id = localId,
            name = name,
            description = description,
            icon = icon,
            color = color,
            isSystemDefault = false,
            syncStatus = Status.PENDING
        )

        box.put(entity)

        queueCreate(
            localEntityId = localId,
            request = AccountGroupCreateRequest(
                name = name,
                description = description,
                icon = icon,
                color = color
            )
        )

        Logger.Database.i("Created local account group: id=$localId, name=$name")
        return entity
    }

    /** Update account group locally and queue for sync (only for server-synced entities). */
    fun updateAccountGroup(
        id: Long,
        name: String? = null,
        description: String? = null,
        icon: String? = null,
        color: String? = null
    ): AccountGroupEntity? {
        val entity = box.get(id) ?: return null

        // Apply updates
        name?.let { entity.name = it }
        description?.let { entity.description = it }
        icon?.let { entity.icon = it }
        color?.let { entity.color = it }

        entity.syncStatus = Status.PENDING
        box.put(entity)

        // Only queue if server-synced (positive ID)
        if (id > 0) {
            queueUpdate(
                entityId = id,
                request = AccountGroupUpdateRequest(
                    name = name,
                    description = description,
                    icon = icon,
                    color = color
                )
            )
        }

        Logger.Database.i("Updated account group: id=$id")
        return entity
    }

    /** Delete account group locally and queue for sync (server-synced) or remove immediately (local-only). */
    fun deleteAccountGroup(id: Long): Boolean = deleteSyncAware(id)
}