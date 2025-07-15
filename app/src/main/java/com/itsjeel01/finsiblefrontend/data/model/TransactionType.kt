package com.itsjeel01.finsiblefrontend.data.model

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