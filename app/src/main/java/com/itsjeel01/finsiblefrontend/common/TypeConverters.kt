package com.itsjeel01.finsiblefrontend.common

import io.objectbox.converter.PropertyConverter

class TransactionTypeConverter : PropertyConverter<TransactionType, Int> {
    override fun convertToDatabaseValue(entityProperty: TransactionType): Int {
        return entityProperty.ordinal
    }

    override fun convertToEntityProperty(databaseValue: Int): TransactionType {
        return TransactionType.entries[databaseValue]
    }
}
