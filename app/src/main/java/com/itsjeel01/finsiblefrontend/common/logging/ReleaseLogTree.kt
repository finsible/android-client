package com.itsjeel01.finsiblefrontend.common.logging

import android.util.Log
import timber.log.Timber

/** Production tree that only logs warnings and errors. */
class ReleaseLogTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.WARN) return

        // In production, only log warnings and errors
        // Future: Add breadcrumb logging for crash reporting here
        when (priority) {
            Log.WARN -> Log.w(tag, message, t)
            Log.ERROR -> Log.e(tag, message, t)
        }
    }
}

