package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import io.objectbox.BoxStore

interface ObjectBoxAdminStarter {
	fun start(store: BoxStore, context: Context)
}

