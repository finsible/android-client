package com.itsjeel01.finsiblefrontend.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

enum class TransactionType(val displayText: String, val icon: Int) {
    INCOME("Income", R.drawable.ic_arrow_down),
    EXPENSE("Expense", R.drawable.ic_arrow_up),
    TRANSFER("Transfer", R.drawable.ic_transfer);

    @Composable
    fun getColor(): Color {
        return when (this) {
            INCOME -> FinsibleTheme.colors.income
            EXPENSE -> FinsibleTheme.colors.expense
            TRANSFER -> FinsibleTheme.colors.transfer
        }
    }

    companion object {
        fun toOrderedList(): List<TransactionType> {
            return listOf(INCOME, EXPENSE, TRANSFER)
        }
    }
}

enum class TransactionRecurringFrequency(val displayText: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly");

    companion object {
        fun toOrderedList(): List<TransactionRecurringFrequency> {
            return listOf(DAILY, WEEKLY, MONTHLY, YEARLY)
        }
    }
}

@Immutable
sealed class SyncState {
    @Immutable
    data object Idle : SyncState()

    @Immutable
    data class Syncing(val remaining: Int) : SyncState()

    @Immutable
    data class Error(val message: String) : SyncState()
}

enum class EntityType {
    TRANSACTION,
    ACCOUNT,
    ACCOUNT_GROUP,
    CATEGORY,
}

enum class OperationType {
    CREATE,
    UPDATE,
    DELETE,
}

enum class Status {
    PENDING,
    SYNCING,
    FAILED,
    COMPLETED
}

enum class Currency {
    INR;

    fun getSymbol(): String {
        return when (this) {
            INR -> "₹"
        }
    }
}