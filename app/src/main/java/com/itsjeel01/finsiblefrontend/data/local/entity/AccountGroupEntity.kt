package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class AccountGroupEntity(
    @Id(assignable = true) override var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var icon: String = "",
    var color: String = "neutral",
    var isSystemDefault: Boolean = true,
) : BaseEntity() {
    @Backlink(to = "accountGroup")
    lateinit var accounts: ToMany<AccountEntity>
}

fun AccountGroupEntity.toDTO(): AccountGroup {
    return AccountGroup(
        id = id,
        name = name,
        description = description,
        icon = icon,
        color = color,
        isSystemDefault = isSystemDefault
    )
}