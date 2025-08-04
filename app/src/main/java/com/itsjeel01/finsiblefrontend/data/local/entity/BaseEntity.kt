package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.ui.util.Duration

abstract class BaseEntity {

    open var id: Long = 0
        set(value) {
            field = value
            cachedTime = System.currentTimeMillis()
        }

    private var cachedTime: Long = 0
    private var cacheTtlMinutes: Long? = Duration.MIN_DEFAULT_TTL

    fun isStale(): Boolean {
        if (cachedTime == 0L) return true

        val expiryTime = if (cacheTtlMinutes != null) {
            cachedTime + (cacheTtlMinutes!! * Duration.SEC_60 * Duration.MSEC_1000)
        } else {
            Long.MAX_VALUE
        }
        return System.currentTimeMillis() > expiryTime
    }

    fun updateCacheTime(ttlMinutes: Long? = null) {
        cachedTime = System.currentTimeMillis()
        cacheTtlMinutes = ttlMinutes ?: this.cacheTtlMinutes
    }
}