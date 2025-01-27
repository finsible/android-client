package com.itsjeel01.finsiblefrontend.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.CustomColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import io.objectbox.converter.PropertyConverter

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER,
}

class TransactionTypeConverter : PropertyConverter<TransactionType, Int> {
    override fun convertToDatabaseValue(entityProperty: TransactionType): Int {
        return entityProperty.ordinal // Convert enum to its ordinal value
    }

    override fun convertToEntityProperty(databaseValue: Int): TransactionType {
        return TransactionType.entries[databaseValue] // Convert ordinal back to enum
    }
}

@Composable
fun getTransactionColor(type: TransactionType): Color {
    return when (type) {
        TransactionType.INCOME -> getCustomColor(key = CustomColorKey.Income)
        TransactionType.EXPENSE -> getCustomColor(key = CustomColorKey.Expense)
        TransactionType.TRANSFER -> getCustomColor(key = CustomColorKey.Transfer)
    }
}