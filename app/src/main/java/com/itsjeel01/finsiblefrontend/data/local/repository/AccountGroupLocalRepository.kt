package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import io.objectbox.Box
import javax.inject.Inject

class AccountGroupLocalRepository @Inject constructor(
    override val box: Box<AccountGroupEntity>,
) : BaseLocalRepository<AccountGroup, AccountGroupEntity> {

    override fun addAll(
        data: List<AccountGroup>,
        additionalInfo: Any?,
        ttlMinutes: Long?
    ) {
        for (accountGroup in data) {
            add(accountGroup.toEntity().apply {
                updateCacheTime(ttlMinutes)
            })
        }
    }

    override fun syncToServer(entity: AccountGroupEntity) {
        TODO("Not yet implemented")
    }
}