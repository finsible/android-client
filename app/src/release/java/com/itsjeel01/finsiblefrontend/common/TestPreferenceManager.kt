package com.itsjeel01.finsiblefrontend.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Stub implementation of DebugPreferenceManager for release builds. */
@Singleton
class TestPreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    fun shouldSkipDebugScreen(): Boolean = true
}

