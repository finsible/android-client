package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.Constants.Companion.DEFAULT_TTL_MINUTES
import com.itsjeel01.finsiblefrontend.common.Constants.Companion.MILLISECONDS_IN_SECOND
import com.itsjeel01.finsiblefrontend.common.Constants.Companion.SECONDS_IN_MINUTE

abstract class BaseEntity {

    open var id: Long = 0
        set(value) {
            field = value
            cachedTime = System.currentTimeMillis()
        }

    private var cachedTime: Long = 0
    private var cacheTtlMinutes: Long? = DEFAULT_TTL_MINUTES

    fun isStale(): Boolean {
        if (cachedTime == 0L) return true

        val expiryTime = cachedTime + ((cacheTtlMinutes?.times(SECONDS_IN_MINUTE)
            ?: Long.MAX_VALUE) * MILLISECONDS_IN_SECOND)
        return System.currentTimeMillis() > expiryTime
    }

    fun updateCacheTime(ttlMinutes: Long? = null) {
        cachedTime = System.currentTimeMillis()
        cacheTtlMinutes = ttlMinutes ?: this.cacheTtlMinutes
    }
}