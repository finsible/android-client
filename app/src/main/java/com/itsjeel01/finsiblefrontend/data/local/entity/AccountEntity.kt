package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.data.model.Account
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class AccountEntity(
    @Id(assignable = true) override var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var balance: Double = 0.0,
    var currencyCode: String = "",
    var icon: String = "",
    var isActive: Boolean = false,
    var isSystemDefault: Boolean = false,
) : BaseEntity() {
    lateinit var accountGroup: ToOne<AccountGroupEntity>
}

fun AccountEntity.toDTO(): Account {
    return Account(
        id = id,
        name = name,
        description = description,
        accountGroupId = accountGroup.target.id,
        balance = balance,
        currencyCode = currencyCode,
        icon = icon,
        isActive = isActive,
        isSystemDefault = isSystemDefault,
    )
}