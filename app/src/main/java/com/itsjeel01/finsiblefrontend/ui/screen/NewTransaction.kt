package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.NewTransactionBottomSheetHost
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.NewTransactionInputSection
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.NewTransactionSingleScreenHost
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section.NewTransactionCtaSection
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel

@Composable
fun NewTransactionScreen(
    state: NewTransactionFormState,
    categoriesMap: Map<CategoryUIModel, List<CategoryUIModel>>,
    categories: List<CategoryUIModel>,
    fromAccounts: List<AccountUIModel>,
    toAccounts: List<AccountUIModel>,
    currencyFormatter: CurrencyFormatter,
    currencyRepository: CurrencyRepository,
    availableCurrencies: List<Currency>,
    onEvent: (NewTransactionUiEvent) -> Unit,
    onSave: () -> Unit,
    onSaveAndAddMore: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transactionAccentColor = state.transactionType.getColor()
    val transactionSurfaceColor = state.transactionType.getSurfaceColor()

    val allCategories = remember(categoriesMap) {
        categoriesMap.entries.flatMap { (parent, children) ->
            listOf(parent) + children
        }
    }

    NewTransactionSingleScreenHost(
        modifier = modifier,
        inputSection = { inputModifier ->
            NewTransactionInputSection(
                modifier = inputModifier,
                state = state,
                categories = categories,
                allCategories = allCategories,
                fromAccounts = fromAccounts,
                toAccounts = toAccounts,
                currencyFormatter = currencyFormatter,
                currencyRepository = currencyRepository,
                onEvent = onEvent,
            )
        },
        ctaSection = { ctaModifier ->
            NewTransactionCtaSection(
                modifier = ctaModifier,
                isSaving = state.isSaving,
                onSaveAndAddMore = onSaveAndAddMore,
                onSave = onSave
            )
        }
    )

    NewTransactionBottomSheetHost(
        state = state,
        categories = categoriesMap,
        fromAccounts = fromAccounts,
        toAccounts = toAccounts,
        onEvent = onEvent,
        accentColor = transactionAccentColor,
        surfaceColor = transactionSurfaceColor,
        availableCurrencies = availableCurrencies
    )
}
