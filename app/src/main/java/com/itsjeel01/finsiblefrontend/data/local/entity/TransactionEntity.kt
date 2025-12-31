package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.StatusConverter
import com.itsjeel01.finsiblefrontend.data.local.TransactionTypeConverter
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import java.util.Calendar

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
    var description: String? = null,
    var currency: String = "INR",

    @Index var fromAccountId: Long? = null,
    @Index var toAccountId: Long? = null,

    @Convert(converter = StatusConverter::class, dbType = Int::class)
    var syncStatus: Status = Status.COMPLETED,
    var lastSyncAttempt: Long? = null,
    var syncError: String? = null,

    // Split expense fields (future scope)
    var spaceId: Long? = null,
    var userShare: String? = null,
    var isSplit: Boolean = false,
    var paidByUserId: Long? = null,
    var paidByUserName: String? = null,
) : BaseEntity() {

    val periodKey: String
        get() {
            val cal = Calendar.getInstance().apply { timeInMillis = transactionDate }
            return "${cal.get(Calendar.YEAR)}-${
                (cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
            }"
        }

    val isLocalOnly: Boolean
        get() = id < 0
}

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
    toAccountId = toAccountId,
    spaceId = spaceId,
    userShare = userShare,
    isSplit = isSplit,
    paidByUserId = paidByUserId,
    paidByUserName = paidByUserName
)