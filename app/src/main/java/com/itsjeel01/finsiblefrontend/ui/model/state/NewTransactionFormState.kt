package com.itsjeel01.finsiblefrontend.ui.model.state

import androidx.compose.runtime.Immutable
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType

enum class NewTransactionDateSelection {
    TODAY,
    YESTERDAY,
    CUSTOM
}

enum class NewTransactionSheetMode {
    CURRENCY,
    DATE_PICKER,
    CATEGORY_EXPLORER,
    ACCOUNT_FROM_SELECTOR,
    ACCOUNT_TO_SELECTOR
}

enum class NewTransactionValidationError {
    AMOUNT,
    DATE,
    CATEGORY,
    FROM_ACCOUNT,
    TO_ACCOUNT,
    TRANSFER_ACCOUNTS_MUST_DIFFER
}

/** Freshness status for exchange rates used in conversion UI. */
enum class ExchangeRateFreshness {
    AVAILABLE,
    STALE,
    UNAVAILABLE
}

/** Immutable UI state for the new transaction single-screen form. */
@Immutable
data class NewTransactionFormState(
    val amountString: String = "",
    val dateMillis: Long? = null,
    val dateSelection: NewTransactionDateSelection = NewTransactionDateSelection.TODAY,
    val currencyCode: String = "",
    val isRecurring: Boolean = false,
    val recurringFrequency: TransactionRecurringFrequency = TransactionRecurringFrequency.DAILY,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val categoryId: Long? = null,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val description: String = "",
    val sheetMode: NewTransactionSheetMode? = null,
    val validationErrors: Set<NewTransactionValidationError> = emptySet(),
    val isSaving: Boolean = false,
    /** Optional computed display string for converted amount in the user's preferred currency. */
    val convertedAmountDisplay: String? = null,
    /** Freshness indicator for the exchange rate used to compute [convertedAmountDisplay]. */
    val exchangeRateFreshness: ExchangeRateFreshness = ExchangeRateFreshness.AVAILABLE,
    /** Mirrors current connectivity so the conversion UI can reflect offline/stale behavior. */
    val isOnline: Boolean = true
) {
    companion object {
        /** Default state seeded with the current local time as the transaction date. */
        val DEFAULT get() = NewTransactionFormState(dateMillis = System.currentTimeMillis())
    }
}
