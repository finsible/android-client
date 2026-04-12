package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import io.objectbox.BoxStore
import javax.inject.Inject

class ReleaseObjectBoxAdminStarter @Inject constructor() : ObjectBoxAdminStarter {
    override fun start(store: BoxStore, context: Context) {
        Logger.Database.d("ObjectBox admin is disabled for release builds")
    }
}

