package com.itsjeel01.finsiblefrontend.ui.model.event

import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionDateSelection
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionSheetMode

sealed interface NewTransactionUiEvent {
    data object ResetClicked : NewTransactionUiEvent
    data class TransactionTypeChanged(val type: TransactionType) : NewTransactionUiEvent
    data class AmountChanged(val value: String) : NewTransactionUiEvent
    data class CurrencySelected(val currencyCode: String) : NewTransactionUiEvent
    data class DateQuickSelectionChanged(val selection: NewTransactionDateSelection) : NewTransactionUiEvent
    data class CustomDateSelected(val dateMillis: Long) : NewTransactionUiEvent
    data object ClearCustomDate : NewTransactionUiEvent
    data class RecurringChanged(val isRecurring: Boolean) : NewTransactionUiEvent
    data class RecurringFrequencyChanged(val frequency: TransactionRecurringFrequency) : NewTransactionUiEvent
    data class CategorySelected(val categoryId: Long) : NewTransactionUiEvent
    data class FromAccountSelected(val accountId: Long) : NewTransactionUiEvent
    data class ToAccountSelected(val accountId: Long) : NewTransactionUiEvent
    data object SwapAccounts : NewTransactionUiEvent
    data class NotesChanged(val value: String) : NewTransactionUiEvent
    data class SheetModeChanged(val mode: NewTransactionSheetMode?) : NewTransactionUiEvent
}
