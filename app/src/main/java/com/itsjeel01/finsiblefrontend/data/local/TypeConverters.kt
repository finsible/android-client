package com.itsjeel01.finsiblefrontend.data.local

import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import io.objectbox.converter.PropertyConverter
import kotlin.enums.EnumEntries

open class EnumPropertyConverter<E : Enum<E>>(private val entries: EnumEntries<E>) : PropertyConverter<E, Int> {
    override fun convertToDatabaseValue(entityProperty: E?): Int? = entityProperty?.ordinal

    override fun convertToEntityProperty(databaseValue: Int?): E? {
        return databaseValue
            ?.takeIf { it in 0 until entries.size }
            ?.let { entries[it] }
    }
}

class TransactionTypeConverter : EnumPropertyConverter<TransactionType>(TransactionType.entries)

class EntityTypeConverter : EnumPropertyConverter<EntityType>(EntityType.entries)

class OperationTypeConverter : EnumPropertyConverter<OperationType>(OperationType.entries)

class StatusConverter : EnumPropertyConverter<Status>(Status.entries)

class CurrencyConverter : PropertyConverter<Currency, String> {
    override fun convertToDatabaseValue(entityProperty: Currency?): String? = entityProperty?.name
    override fun convertToEntityProperty(databaseValue: String?): Currency? = databaseValue?.let { Currency.valueOf(it) }
}