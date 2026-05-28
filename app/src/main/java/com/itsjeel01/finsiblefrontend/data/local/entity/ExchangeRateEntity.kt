package com.itsjeel01.finsiblefrontend.data.local.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Unique

/** Cache-only entity — fetched from server via ExchangeRateApiService, never synced bidirectionally. No PendingOperationEntity is created. */
@Entity
data class ExchangeRateEntity(
    @Id(assignable = true)
    override var id: Long = 0,

    @Unique
    var pairCode: String = "",

    @Index
    var baseCurrencyCode: String = "",
    var targetCurrencyCode: String = "",
    var rate: Double = 0.0,
    var lastSyncedAt: Long = 0L
) : BaseEntity()