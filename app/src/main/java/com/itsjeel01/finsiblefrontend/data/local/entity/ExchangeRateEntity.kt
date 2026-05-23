package com.itsjeel01.finsiblefrontend.data.local.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class ExchangeRateEntity(
    @Id(assignable = true)
    override var id: Long = 0,

    @Unique
    var pairCode: String = "",

    var baseCurrencyCode: String = "",
    var targetCurrencyCode: String = "",
    var rate: Double = 0.0,
    var lastSyncedAt: Long = 0L
) : BaseEntity()