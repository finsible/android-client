package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.CurrencyConverter
import com.itsjeel01.finsiblefrontend.data.local.StatusConverter
import com.itsjeel01.finsiblefrontend.data.local.TransactionTypeConverter
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
data class TransactionEntity(
    @Id(assignable = true)
    override var id: Long = 0,

    @Convert(converter = TransactionTypeConverter::class, dbType = Int::class)
    var type: TransactionType = TransactionType.EXPENSE,

    /** Indexed amount in centis (×100) for 2 decimal precision. E.g., 123.45 → 12345. */
    @Index var totalAmount: Long = 0,

    /** Pre-computed lowercase searchable text (description + categoryName) for efficient text search. */
    var searchableText: String = "",

    @Index var transactionDate: Long = 0,

    var categoryId: Long = 0,
    var categoryName: String = "",
    var categoryIcon: String = "",
    var description: String? = null,

    @Convert(converter = CurrencyConverter::class, dbType = String::class)
    var currency: Currency = Currency.INR,

    @Index var fromAccountId: Long? = null,
    var fromAccountName: String? = null,
    @Index var toAccountId: Long? = null,
    var toAccountName: String? = null,

    @Convert(converter = StatusConverter::class, dbType = Int::class)
    override var syncStatus: Status = Status.COMPLETED,
    override var lastSyncAttempt: Long? = null,
    override var syncError: String? = null,

    // Split expense fields (future scope)
    var spaceId: Long? = null,
    var userShare: String? = null,
    var isSplit: Boolean = false,
    var paidByUserId: Long? = null,
    var paidByUserName: String? = null,
) : BaseEntity(), SyncableEntity

/** Multiplier for centis conversion (2 decimal places). E.g., 123.45 → 12345. */
const val AMOUNT_MULTIPLIER = 100L

/** Convert centis (Long) to decimal String with exactly 2dp. E.g., 12345 → "123.45", -12345 → "-123.45". */
fun Long.toAmountString(): String {
    val sign = if (this < 0) "-" else ""
    val abs = if (this < 0) -this else this
    val wholePart = abs / AMOUNT_MULTIPLIER
    val decimalPart = (abs % AMOUNT_MULTIPLIER).toString().padStart(2, '0')
    return "$sign$wholePart.$decimalPart"
}

/** Convert decimal String to centis (Long). E.g., "123.45" → 12345, "-123.45" → -12345. */
fun String.toAmountCentis(): Long {
    val trimmed = this.trim()
    val negative = trimmed.startsWith("-")
    val unsigned = if (negative) trimmed.substring(1) else trimmed
    val parts = unsigned.split(".")
    val wholePart = parts[0].toLongOrNull() ?: 0L
    val decimalPart = if (parts.size > 1) {
        parts[1].take(2).padEnd(2, '0').toLongOrNull() ?: 0L
    } else 0L
    val centis = wholePart * AMOUNT_MULTIPLIER + decimalPart
    return if (negative) -centis else centis
}

fun TransactionEntity.toDTO(): Transaction = Transaction(
    id = id,
    type = type.name,
    totalAmount = totalAmount.toAmountString(),
    transactionDate = transactionDate.toString(),
    categoryId = categoryId,
    categoryName = categoryName,
    description = description,
    currency = currency,
    fromAccountId = fromAccountId,
    fromAccountName = fromAccountName,
    toAccountId = toAccountId,
    toAccountName = toAccountName,
    spaceId = spaceId,
    userShare = userShare,
    isSplit = isSplit,
    paidByUserId = paidByUserId,
    paidByUserName = paidByUserName
)