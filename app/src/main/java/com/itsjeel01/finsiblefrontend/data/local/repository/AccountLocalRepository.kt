package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.toAmountCentis
import com.itsjeel01.finsiblefrontend.data.local.entity.toAmountString
import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountUpdateRequest
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.kotlin.flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
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
        balance = entity.balanceCentis.toAmountString(),
        currencyCode = entity.currencyCode,
        icon = entity.icon,
        accountGroupId = entity.accountGroup.targetId,
        isActive = entity.isActive
    )

    override fun toUpdateRequest(entity: AccountEntity) = AccountUpdateRequest(
        name = entity.name,
        description = entity.description,
        balance = entity.balanceCentis.toAmountString(),
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

    suspend fun createAccount(
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
                balanceCentis = balance.toAmountCentis(),
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
        balance?.let { entity.balanceCentis = it.toAmountCentis() }
        currencyCode?.let { entity.currencyCode = it }
        icon?.let { entity.icon = it }
        accountGroupId?.let { entity.accountGroup.targetId = it }
        isActive?.let { entity.isActive = it }

        return queueUpdateEntity(entity)
    }

    fun deleteAccount(id: Long): Boolean = queueDeleteEntity(id)

    /** Reactively emits the top [limit] active accounts sorted purely by usage frequency. */
    fun getFrequentAccountsFlow(limit: Long): Flow<List<AccountEntity>> = callbackFlow {
        val query = box.query()
            .equal(AccountEntity_.isActive, true)
            .orderDesc(AccountEntity_.usageCount)
            .build()

        val subscription = query.subscribe().observer {
            trySend(query.find(0, limit))
        }

        awaitClose {
            subscription.cancel()
            query.close()
        }
    }.conflate()

    /**
     * Reactively emits the top [limit] active accounts sorted by recency.
     * Accounts never used (null lastUsedAt) are excluded — ObjectBox places nulls first on orderDesc,
     * which would incorrectly rank unused accounts as "most recent".
     */
    fun getRecentAccountsFlow(limit: Long): Flow<List<AccountEntity>> = callbackFlow {
        val query = box.query()
            .equal(AccountEntity_.isActive, true)
            .notNull(AccountEntity_.lastUsedAt)
            .orderDesc(AccountEntity_.lastUsedAt)
            .build()

        val subscription = query.subscribe().observer {
            trySend(query.find(0, limit))
        }

        awaitClose {
            subscription.cancel()
            query.close()
        }
    }.conflate()

    /**
     * Reactive top-K by usage frequency, driven by a Flow<Int> for K so the limit can change reactively.
     * Only returns active accounts. Local ObjectBox only — no remote fetch.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTopK(kFlow: Flow<Int>): Flow<List<AccountEntity>> {
        val baseFlow = box.query()
            .equal(AccountEntity_.isActive, true)
            .orderDesc(AccountEntity_.usageCount)
            .build()
            .flow()

        return combine(baseFlow, kFlow) { entities, k -> entities.take(k) }
    }

    /**
     * Increments usage count and updates lastUsedAt for the given account.
     *
     * Uses `BoxStore.callInTx` which blocks the calling thread. Callers must ensure this
     * runs on a background dispatcher (e.g. `Dispatchers.IO`) to avoid main-thread jank.
     */
    fun updateAccountUsage(id: Long): AccountEntity? {
        return box.store.callInTx {
            incrementUsage(id)
        }
    }

    internal fun incrementUsage(id: Long): AccountEntity? {
        val entity = box.get(id) ?: return null

        entity.usageCount += 1
        entity.lastUsedAt = System.currentTimeMillis()

        box.put(entity)
        Logger.Database.d("Updated account usage: id=$id, count=${entity.usageCount}")
        return entity
    }
}