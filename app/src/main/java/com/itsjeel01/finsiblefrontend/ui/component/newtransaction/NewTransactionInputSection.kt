package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section.NewTransactionHeaderSection
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section.NewTransactionAmountCard
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section.NewTransactionDateSection
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section.NewTransactionCategorySection
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section.NewTransactionAccountSection
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section.NewTransactionNotesSection
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionSheetMode
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionValidationError
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.coroutines.flow.distinctUntilChanged

// Tracks which inline row is currently expanded
enum class ExpandedSection { NONE, CATEGORY, FROM_ACCOUNT, TO_ACCOUNT }

@Composable
fun NewTransactionInputSection(
    state: NewTransactionFormState,
    categories: List<CategoryUIModel>,
    allCategories: List<CategoryUIModel>,
    fromAccounts: List<AccountUIModel>,
    toAccounts: List<AccountUIModel>,
    onEvent: (NewTransactionUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    onScrolledFromTopChanged: (Boolean) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val transactionAccentColor = state.transactionType.getColor()

    // Mutual Collapse State
    var expandedSection by rememberSaveable { mutableStateOf(ExpandedSection.NONE) }

    LaunchedEffect(listState, onScrolledFromTopChanged) {
        snapshotFlow {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
            .distinctUntilChanged()
            .collect { onScrolledFromTopChanged(it) }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMd),
        contentPadding = PaddingValues(bottom = FinsibleTheme.spacing.insetXl)
    ) {
        // Transaction type selector — scrolls away with content
        item(key = "transaction_type") {
            NewTransactionHeaderSection(
                state = state,
                onEvent = onEvent
            )
        }

        // Amount card — sticky below type selector, above form
        stickyHeader(key = "amount_sticky") {
            NewTransactionAmountCard(
                state = state,
                onEvent = onEvent,
                accentColor = transactionAccentColor,
            )
        }

        // Date Card
        item {
            NewTransactionSectionCard {
                NewTransactionDateSection(
                    state = state,
                    onEvent = onEvent,
                    accentColor = transactionAccentColor
                )
            }
        }

        // Category Card — only leaf categories shown; resolved from full list
        item {
            NewTransactionSectionCard {
                NewTransactionCategorySection(
                    categories = categories,
                    allCategories = allCategories,
                    selectedCategoryId = state.categoryId,
                    hasError = state.validationErrors.contains(NewTransactionValidationError.CATEGORY),
                    isExpanded = expandedSection == ExpandedSection.CATEGORY,
                    onExpandedChange = { expanded ->
                        expandedSection = if (expanded) ExpandedSection.CATEGORY else ExpandedSection.NONE
                    },
                    onOpenAllCategories = {
                        onEvent(NewTransactionUiEvent.SheetModeChanged(NewTransactionSheetMode.CATEGORY_EXPLORER))
                    },
                    accentColor = transactionAccentColor,
                    onSelectCategory = { onEvent(NewTransactionUiEvent.CategorySelected(it)) }
                )
            }
        }

        // Account Card
        item {
            NewTransactionSectionCard {
                NewTransactionAccountSection(
                    state = state,
                    fromAccounts = fromAccounts,
                    toAccounts = toAccounts,
                    isFromExpanded = expandedSection == ExpandedSection.FROM_ACCOUNT,
                    onFromExpandedChange = { expanded ->
                        expandedSection = if (expanded) ExpandedSection.FROM_ACCOUNT else ExpandedSection.NONE
                    },
                    isToExpanded = expandedSection == ExpandedSection.TO_ACCOUNT,
                    onToExpandedChange = { expanded ->
                        expandedSection = if (expanded) ExpandedSection.TO_ACCOUNT else ExpandedSection.NONE
                    },
                    onOpenFromAccounts = {
                        onEvent(NewTransactionUiEvent.SheetModeChanged(NewTransactionSheetMode.ACCOUNT_FROM_SELECTOR))
                    },
                    onOpenToAccounts = {
                        onEvent(NewTransactionUiEvent.SheetModeChanged(NewTransactionSheetMode.ACCOUNT_TO_SELECTOR))
                    },
                    accentColor = transactionAccentColor,
                    onEvent = onEvent
                )
            }
        }

        // Notes Card
        item {
            NewTransactionSectionCard {
                NewTransactionNotesSection(
                    state = state,
                    onEvent = onEvent,
                    accentColor = transactionAccentColor
                )
            }
        }
    }
}