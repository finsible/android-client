package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity
import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountUpdateRequest
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.Property
import javax.inject.Inject

class AccountLocalRepository @Inject constructor(
    override val box: Box<AccountEntity>,
    syncMetadataBox: Box<SyncMetadataEntity>,
    pendingOperationBox: Box<PendingOperationEntity>,
    localIdGenerator: LocalIdGenerator
) : SyncableLocalRepository<Account, AccountEntity>(
    box,
    syncMetadataBox,
    pendingOperationBox,
    localIdGenerator
) {

    override val entityType: EntityType = EntityType.ACCOUNT
    override fun idProperty(): Property<AccountEntity> = AccountEntity_.id
    override fun syncStatusProperty(): Property<AccountEntity> = AccountEntity_.syncStatus

    override fun addAll(
        data: List<Account>,
        additionalInfo: Any?,
        ttlMinutes: Long?
    ) {
        super.addAll(data, additionalInfo, ttlMinutes)

        val entities = data.map { account ->
            account.toEntity().apply {
                updateCacheTime(ttlMinutes)
                syncStatus = Status.COMPLETED
                account.accountGroupId?.let { groupId ->
                    accountGroup.targetId = groupId
                }
            }
        }

        box.put(entities)
        Logger.Database.d("Added ${entities.size} accounts to local DB")
    }

    override fun syncToServer(entity: AccountEntity) {
        Logger.Database.w("syncToServer called directly - use SyncManager instead")
    }

    /** Get accounts for a specific account group. */
    fun getAccountsForGroup(groupId: Long): List<AccountEntity> {
        return box.all
            .filter { it.accountGroup.target?.id == groupId }
            .also { Logger.Database.d("Fetched ${it.size} accounts for group $groupId") }
    }

    /** Get all active accounts. */
    fun getActiveAccounts(): List<AccountEntity> {
        return box.query()
            .equal(AccountEntity_.isActive, true)
            .build()
            .find()
            .also { Logger.Database.d("Fetched ${it.size} active accounts") }
    }

    /** Create account locally and queue for sync. Returns immediately with local entity. */
    fun createAccount(
        name: String,
        description: String,
        balance: String,
        currencyCode: String,
        icon: String,
        accountGroupId: Long?,
        isActive: Boolean = true
    ): AccountEntity {
        val localId = localIdGenerator.nextLocalId()

        val entity = AccountEntity(
            id = localId,
            name = name,
            description = description,
            balance = android.icu.math.BigDecimal(balance),
            currencyCode = currencyCode,
            icon = icon,
            isActive = isActive,
            isSystemDefault = false,
            syncStatus = Status.PENDING
        )

        accountGroupId?.let {
            entity.accountGroup.targetId = it
        }

        box.put(entity)

        queueCreate(
            localEntityId = localId,
            request = AccountCreateRequest(
                name = name,
                description = description,
                balance = balance,
                currencyCode = currencyCode,
                icon = icon,
                accountGroupId = accountGroupId,
                isActive = isActive
            )
        )

        Logger.Database.i("Created local account: id=$localId, name=$name")
        return entity
    }

    /** Update account locally and queue for sync (only for server-synced entities). */
    fun updateAccount(
        id: Long,
        name: String? = null,
        description: String? = null,
        balance: String? = null,
        currencyCode: String? = null,
        icon: String? = null,
        accountGroupId: Long? = null,
        isActive: Boolean? = null
    ): AccountEntity? {
        val entity = box.get(id) ?: return null

        // Apply updates
        name?.let { entity.name = it }
        description?.let { entity.description = it }
        balance?.let { entity.balance = android.icu.math.BigDecimal(it) }
        currencyCode?.let { entity.currencyCode = it }
        icon?.let { entity.icon = it }
        accountGroupId?.let { entity.accountGroup.targetId = it }
        isActive?.let { entity.isActive = it }

        entity.syncStatus = Status.PENDING
        box.put(entity)

        // Only queue if server-synced (positive ID)
        if (id > 0) {
            queueUpdate(
                entityId = id,
                request = AccountUpdateRequest(
                    name = name,
                    description = description,
                    balance = balance,
                    currencyCode = currencyCode,
                    icon = icon,
                    accountGroupId = accountGroupId,
                    isActive = isActive
                )
            )
        }

        Logger.Database.i("Updated account: id=$id")
        return entity
    }

    /** Delete account locally and queue for sync (server-synced) or remove immediately (local-only). */
    fun deleteAccount(id: Long): Boolean = deleteSyncAware(id)
}
