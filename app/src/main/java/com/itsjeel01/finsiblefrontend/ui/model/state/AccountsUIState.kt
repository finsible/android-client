package com.itsjeel01.finsiblefrontend.ui.model.state

import androidx.compose.runtime.Immutable
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountGroupUIModel
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** UI state for accounts tab. Immutable for Compose optimization. */
@Immutable
data class AccountsUIState(
    val tileCards: ImmutableList<FinsibleTileCardData> = persistentListOf(),
    val listItems: ImmutableList<AccountListItem> = persistentListOf(),
    val accountGroups: ImmutableList<AccountGroupUIModel> = persistentListOf(),
    val selectedGroupId: Long? = null,
    val isLoading: Boolean = false
)

/** Sealed interface for account list items (headers and accounts). */
sealed interface AccountListItem {
    @Immutable
    data class Header(val groupName: String) : AccountListItem

    @Immutable
    data class Account(val uiModel: AccountUIModel) : AccountListItem
}
