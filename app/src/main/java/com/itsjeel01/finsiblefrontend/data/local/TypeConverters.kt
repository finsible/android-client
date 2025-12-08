package com.itsjeel01.finsiblefrontend.data.local

import android.icu.math.BigDecimal
import com.itsjeel01.finsiblefrontend.common.TransactionType
import io.objectbox.converter.PropertyConverter

class TransactionTypeConverter : PropertyConverter<TransactionType, Int> {
    override fun convertToDatabaseValue(entityProperty: TransactionType): Int {
        return entityProperty.ordinal
    }

    override fun convertToEntityProperty(databaseValue: Int): TransactionType {
        return TransactionType.entries[databaseValue]
    }
}

class BigDecimalConverter : PropertyConverter<BigDecimal, String> {
    override fun convertToDatabaseValue(entityProperty: BigDecimal): String {
        return entityProperty.toString()
    }

    override fun convertToEntityProperty(databaseValue: String): BigDecimal {
        return BigDecimal(databaseValue)
    }
}
