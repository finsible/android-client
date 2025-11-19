package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import io.objectbox.Box
import javax.inject.Inject

class AccountLocalRepository @Inject constructor(
    override val box: Box<AccountEntity>,
) : BaseLocalRepository<Account, AccountEntity> {

    override fun addAll(
        data: List<Account>,
        additionalInfo: Any?,
        ttlMinutes: Long?
    ) {
        for (account in data) {
            val entity = account.toEntity().apply {
                updateCacheTime(ttlMinutes)
                accountGroup.targetId = account.accountGroupId
            }
            add(entity)
        }
    }

    override fun syncToServer(entity: AccountEntity) {
        TODO("Not yet implemented")
    }
}