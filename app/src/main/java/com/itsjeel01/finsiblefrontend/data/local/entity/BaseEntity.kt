package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.ui.constants.Duration

abstract class BaseEntity {

    open var id: Long = 0
        set(value) {
            field = value
            cachedTime = System.currentTimeMillis()
        }

    var cachedTime: Long = 0
    var cacheTtlMinutes: Long? = Duration.DEFAULT_TTL_MINUTES

    fun isStale(): Boolean {
        if (cachedTime == 0L) return true

        val expiryTime = if (cacheTtlMinutes != null) {
            cachedTime + (cacheTtlMinutes!! * Duration.MINUTE_AS_SECONDS * Duration.MS_1000)
        } else {
            Long.MAX_VALUE
        }
        return System.currentTimeMillis() > expiryTime
    }

    fun updateCacheTime(ttlMinutes: Long? = null) {
        this.cachedTime = System.currentTimeMillis()
        this.cacheTtlMinutes = ttlMinutes ?: this.cacheTtlMinutes
    }
}