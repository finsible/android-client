package com.itsjeel01.finsiblefrontend.ui.model

import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity

/** Sealed class for representing items in the accounts list. */
sealed class AccountListItem {
    /** Header item for account group name. */
    data class Header(val groupName: String) : AccountListItem()

    /** Account item. */
    data class Account(val account: AccountEntity) : AccountListItem()
}
