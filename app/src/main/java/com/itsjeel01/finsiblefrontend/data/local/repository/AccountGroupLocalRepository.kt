package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupUpdateRequest
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.kotlin.flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountGroupLocalRepository @Inject constructor(
    override val box: Box<AccountGroupEntity>,
    pendingOperationBox: Box<PendingOperationEntity>,
    localIdGenerator: LocalIdGenerator
) : SyncableLocalRepository<AccountGroup, AccountGroupEntity>(
    box,
    pendingOperationBox,
    localIdGenerator
) {

    override val entityType: EntityType = EntityType.ACCOUNT_GROUP
    override fun idProperty(): Property<AccountGroupEntity> = AccountGroupEntity_.id
    override fun syncStatusProperty(): Property<AccountGroupEntity> = AccountGroupEntity_.syncStatus

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAccountGroupsFlow(): Flow<List<AccountGroupEntity>> {
        return box.query().build().flow()
    }

    override fun toCreateRequest(entity: AccountGroupEntity) = AccountGroupCreateRequest(
        name = entity.name,
        description = entity.description,
        icon = entity.icon,
        color = entity.color
    )

    override fun toUpdateRequest(entity: AccountGroupEntity) = AccountGroupUpdateRequest(
        name = entity.name,
        description = entity.description,
        icon = entity.icon,
        color = entity.color
    )

    override fun addAll(
        data: List<AccountGroup>,
        additionalInfo: Any?
    ) {
        super.addAll(data, additionalInfo)

        val entities = data.map { accountGroup ->
            accountGroup.toEntity().apply {
                syncStatus = Status.COMPLETED
            }
        }

        box.put(entities)
        Logger.Database.d("Added ${entities.size} account groups to local DB")
    }

    fun createAccountGroup(
        name: String,
        description: String,
        icon: String,
        color: String
    ): AccountGroupEntity {
        return queueCreateEntity { localId ->
            AccountGroupEntity(
                id = localId,
                name = name,
                description = description,
                icon = icon,
                color = color,
                isSystemDefault = false,
                syncStatus = Status.PENDING
            )
        }
    }

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

        return queueUpdateEntity(entity)
    }

    fun deleteAccountGroup(id: Long): Boolean = queueDeleteEntity(id)
}