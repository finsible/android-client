package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountUpdateRequest
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.kotlin.flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountLocalRepository @Inject constructor(
    override val box: Box<AccountEntity>,
    pendingOperationBox: Box<PendingOperationEntity>,
    localIdGenerator: LocalIdGenerator
) : SyncableLocalRepository<Account, AccountEntity>(
    box,
    pendingOperationBox,
    localIdGenerator
) {

    override val entityType: EntityType = EntityType.ACCOUNT
    override fun idProperty(): Property<AccountEntity> = AccountEntity_.id
    override fun syncStatusProperty(): Property<AccountEntity> = AccountEntity_.syncStatus

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAccountsFlow(): Flow<List<AccountEntity>> {
        return box.query().build().flow()
    }

    override fun toCreateRequest(entity: AccountEntity) = AccountCreateRequest(
        name = entity.name,
        description = entity.description,
        balance = entity.balance.toString(),
        currencyCode = entity.currencyCode,
        icon = entity.icon,
        accountGroupId = entity.accountGroup.targetId,
        isActive = entity.isActive
    )

    override fun toUpdateRequest(entity: AccountEntity) = AccountUpdateRequest(
        name = entity.name,
        description = entity.description,
        balance = entity.balance.toString(),
        currencyCode = entity.currencyCode,
        icon = entity.icon,
        accountGroupId = entity.accountGroup.targetId,
        isActive = entity.isActive
    )

    override fun addAll(
        data: List<Account>,
        additionalInfo: Any?
    ) {
        super.addAll(data, additionalInfo)

        val entities = data.map { account ->
            account.toEntity().apply {
                syncStatus = Status.COMPLETED
                account.accountGroupId?.let { groupId ->
                    accountGroup.targetId = groupId
                }
            }
        }

        box.put(entities)
        Logger.Database.d("Added ${entities.size} accounts to local DB")
    }

    fun getAccountsForGroup(groupId: Long): List<AccountEntity> {
        return box.all
            .filter { it.accountGroup.target?.id == groupId }
            .also { Logger.Database.d("Fetched ${it.size} accounts for group $groupId") }
    }

    fun getActiveAccounts(): List<AccountEntity> {
        return box.query()
            .equal(AccountEntity_.isActive, true)
            .build()
            .find()
            .also { Logger.Database.d("Fetched ${it.size} active accounts") }
    }

    fun createAccount(
        name: String,
        description: String,
        balance: String,
        currencyCode: String,
        icon: String,
        accountGroupId: Long?,
        isActive: Boolean = true
    ): AccountEntity {
        return queueCreateEntity { localId ->
            AccountEntity(
                id = localId,
                name = name,
                description = description,
                balance = android.icu.math.BigDecimal(balance),
                currencyCode = currencyCode,
                icon = icon,
                isActive = isActive,
                isSystemDefault = false,
                syncStatus = Status.PENDING
            ).apply {
                accountGroupId?.let { accountGroup.targetId = it }
            }
        }
    }

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

        name?.let { entity.name = it }
        description?.let { entity.description = it }
        balance?.let { entity.balance = android.icu.math.BigDecimal(it) }
        currencyCode?.let { entity.currencyCode = it }
        icon?.let { entity.icon = it }
        accountGroupId?.let { entity.accountGroup.targetId = it }
        isActive?.let { entity.isActive = it }

        return queueUpdateEntity(entity)
    }

    fun deleteAccount(id: Long): Boolean = queueDeleteEntity(id)
}