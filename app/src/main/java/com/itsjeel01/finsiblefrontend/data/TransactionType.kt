package com.itsjeel01.finsiblefrontend.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.CustomColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER,
}

@Composable
fun getTransactionColor(type: TransactionType): Color {
    return when (type) {
        TransactionType.INCOME -> getCustomColor(key = CustomColorKey.Income)
        TransactionType.EXPENSE -> getCustomColor(key = CustomColorKey.Expense)
        TransactionType.TRANSFER -> getCustomColor(key = CustomColorKey.Transfer)
    }
}