package com.itsjeel01.finsiblefrontend.ui.model.state

import androidx.compose.runtime.Immutable
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.convertUTCToLocal

/** Immutable UI state for the new transaction multi-step form. */
@Immutable
data class NewTransactionFormState(
    val amountString: String = "",
    val dateMillis: Long? = null,
    val isRecurring: Boolean = false,
    val recurringFrequency: TransactionRecurringFrequency = TransactionRecurringFrequency.DAILY,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val categoryId: Long? = null,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val description: String = "",
) {
    companion object {
        /** Default state seeded with the current local time as the transaction date. */
        val DEFAULT get() = NewTransactionFormState(dateMillis = System.currentTimeMillis().convertUTCToLocal())
    }
}

