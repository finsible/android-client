package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** UI state for accounts tab. Immutable for Compose optimization. */
@Immutable
data class AccountsUiState(
    val accountCards: ImmutableList<FlippableCardData> = persistentListOf(),
    val listItems: ImmutableList<AccountListItem> = persistentListOf(),
    val accountGroups: ImmutableList<AccountGroupEntity> = persistentListOf(),
    val selectedGroupId: Long? = null,
    val isLoading: Boolean = false
)

/** Sealed interface for account list items (headers and accounts). */
sealed interface AccountListItem {
    @Immutable
    data class Header(val groupName: String) : AccountListItem

    @Immutable
    data class Account(val uiModel: AccountUiModel) : AccountListItem
}
