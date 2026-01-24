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

    var totalAmount: String = "0.0",

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

fun TransactionEntity.toDTO(): Transaction = Transaction(
    id = id,
    type = type.name,
    totalAmount = totalAmount,
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