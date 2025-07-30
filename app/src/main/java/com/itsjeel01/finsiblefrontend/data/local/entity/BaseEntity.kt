package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.Constants.Companion.MINUTES

abstract class BaseEntity {

    open var id: Long = 0
        set(value) {
            field = value
            cachedTime = System.currentTimeMillis()
        }

    private var cachedTime: Long = 0
    private var cacheTtlMinutes: Long? = 1440L * MINUTES

    fun isStale(): Boolean {
        if (cachedTime == 0L) return true

        val expiryTime = cachedTime + ((cacheTtlMinutes?.times(60) ?: Long.MAX_VALUE) * 1000)
        return System.currentTimeMillis() > expiryTime
    }

    fun updateCacheTime(ttlMinutes: Long? = null) {
        cachedTime = System.currentTimeMillis()
        cacheTtlMinutes = ttlMinutes ?: this.cacheTtlMinutes
    }
}