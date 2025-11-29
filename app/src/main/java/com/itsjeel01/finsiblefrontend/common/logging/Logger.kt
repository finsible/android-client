package com.itsjeel01.finsiblefrontend.common.logging

import timber.log.Timber

/** Centralized logging interface for domain-specific logging. Timber automatically captures caller source location. */
object Logger {

    /** Network operations logger. */
    object Network {
        fun d(message: String) = Timber.d("[${LogDomain.NETWORK.name}] $message")
        fun i(message: String) = Timber.i("[${LogDomain.NETWORK.name}] $message")
        fun w(message: String, throwable: Throwable? = null) = Timber.w(throwable, "[${LogDomain.NETWORK.name}] $message")
        fun e(message: String, throwable: Throwable? = null) = Timber.e(throwable, "[${LogDomain.NETWORK.name}] $message")
    }

    /** Database operations logger. */
    object Database {
        fun d(message: String) = Timber.d("[${LogDomain.DATABASE.name}] $message")
        fun i(message: String) = Timber.i("[${LogDomain.DATABASE.name}] $message")
        fun w(message: String, throwable: Throwable? = null) = Timber.w(throwable, "[${LogDomain.DATABASE.name}] $message")
        fun e(message: String, throwable: Throwable? = null) = Timber.e(throwable, "[${LogDomain.DATABASE.name}] $message")
    }

    /** Authentication operations logger. */
    object Auth {
        fun d(message: String) = Timber.d("[${LogDomain.AUTH.name}] $message")
        fun i(message: String) = Timber.i("[${LogDomain.AUTH.name}] $message")
        fun w(message: String, throwable: Throwable? = null) = Timber.w(throwable, "[${LogDomain.AUTH.name}] $message")
        fun e(message: String, throwable: Throwable? = null) = Timber.e(throwable, "[${LogDomain.AUTH.name}] $message")
    }

    /** Sync operations logger. */
    object Sync {
        fun d(message: String) = Timber.d("[${LogDomain.SYNC.name}] $message")
        fun i(message: String) = Timber.i("[${LogDomain.SYNC.name}] $message")
        fun w(message: String, throwable: Throwable? = null) = Timber.w(throwable, "[${LogDomain.SYNC.name}] $message")
        fun e(message: String, throwable: Throwable? = null) = Timber.e(throwable, "[${LogDomain.SYNC.name}] $message")
    }

    /** Cache operations logger. */
    object Cache {
        fun d(message: String) = Timber.d("[${LogDomain.CACHE.name}] $message")
        fun i(message: String) = Timber.i("[${LogDomain.CACHE.name}] $message")
        fun w(message: String, throwable: Throwable? = null) = Timber.w(throwable, "[${LogDomain.CACHE.name}] $message")
        fun e(message: String, throwable: Throwable? = null) = Timber.e(throwable, "[${LogDomain.CACHE.name}] $message")
    }

    /** UI operations logger. */
    object UI {
        fun d(message: String) = Timber.d("[${LogDomain.UI.name}] $message")
        fun i(message: String) = Timber.i("[${LogDomain.UI.name}] $message")
        fun w(message: String, throwable: Throwable? = null) = Timber.w(throwable, "[${LogDomain.UI.name}] $message")
        fun e(message: String, throwable: Throwable? = null) = Timber.e(throwable, "[${LogDomain.UI.name}] $message")
    }

    /** General app operations logger. */
    object App {
        fun d(message: String) = Timber.d("[${LogDomain.APP.name}] $message")
        fun i(message: String) = Timber.i("[${LogDomain.APP.name}] $message")
        fun w(message: String, throwable: Throwable? = null) = Timber.w(throwable, "[${LogDomain.APP.name}] $message")
        fun e(message: String, throwable: Throwable? = null) = Timber.e(throwable, "[${LogDomain.APP.name}] $message")
    }
}

