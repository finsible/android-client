package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.data.local.StatusConverter
import com.itsjeel01.finsiblefrontend.data.model.Account
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.relation.ToOne

@Entity
data class AccountEntity(
    @Id(assignable = true) override var id: Long = 0,
    var name: String = "",

    var description: String = "",

    /** Account balance in centis (×100). E.g., 123456L = 1234.56. */
    @Index var balanceCentis: Long = 0,

    var currencyCode: String = "",

    var icon: String = "",

    var isActive: Boolean = false,

    var isSystemDefault: Boolean = false,

    @Convert(converter = StatusConverter::class, dbType = Int::class)
    override var syncStatus: Status = Status.COMPLETED,

    override var lastSyncAttempt: Long? = null,

    override var syncError: String? = null,
) : BaseEntity(), SyncableEntity {
    lateinit var accountGroup: ToOne<AccountGroupEntity>
}

fun AccountEntity.toDTO(): Account {
    return Account(
        id = id,
        name = name,
        description = description,
        accountGroupId = accountGroup.target?.id,
        balance = balanceCentis.toAmountString(),
        currencyCode = currencyCode,
        icon = icon,
        isActive = isActive,
        isSystemDefault = isSystemDefault,
    )
}