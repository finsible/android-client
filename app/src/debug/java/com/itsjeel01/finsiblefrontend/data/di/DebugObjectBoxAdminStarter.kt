package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import io.objectbox.BoxStore
import io.objectbox.android.Admin
import javax.inject.Inject

class DebugObjectBoxAdminStarter @Inject constructor() : ObjectBoxAdminStarter {
	override fun start(store: BoxStore, context: Context) {
		val started = Admin(store).start(context)
		Logger.Database.d("ObjectBox Admin started: $started")
	}
}

