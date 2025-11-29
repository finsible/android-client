package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.MyObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.BoxStore
import io.objectbox.android.Admin
import io.objectbox.exception.DbException
import io.objectbox.exception.DbSchemaException
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object ObjectBoxModule {
    private lateinit var store: BoxStore

    @Volatile
    private var databaseWasCleared = false

    @Synchronized
    fun init(context: Context) {
        if (::store.isInitialized) {
            Logger.Database.w("ObjectBox already initialized, skipping")
            return
        }

        Logger.Database.i("Initializing ObjectBox database (schema version: ${BuildConfig.DATABASE_SCHEMA_VERSION})")

        try {
            store = MyObjectBox.builder()
                .androidContext(context)
                .build()

            Logger.Database.i("ObjectBox database initialized successfully")
        } catch (e: DbSchemaException) {
            Logger.Database.w("Schema conflict detected during initialization", e)
            attemptDatabaseRecovery(context, e)
        } catch (e: DbException) {
            Logger.Database.w("Database exception during initialization", e)
            attemptDatabaseRecovery(context, e)
        }

        if (BuildConfig.DEBUG) startAdmin(context)
    }

    /** Attempts to recover the database by clearing files and rebuilding. */
    private fun attemptDatabaseRecovery(context: Context, originalException: Exception) {
        try {
            clearDatabaseFiles(context)

            // Retry initialization after clearing
            store = MyObjectBox.builder()
                .androidContext(context)
                .build()

            databaseWasCleared = true
            Logger.Database.i("Database cleared and rebuilt successfully")
        } catch (e: Exception) {
            Logger.Database.e("Failed to recover from database error", e)
            throw RuntimeException("ObjectBox initialization failed after recovery attempt", originalException)
        }
    }

    /** Clears ObjectBox database files from the file system. */
    private fun clearDatabaseFiles(context: Context) {
        Logger.Database.d("Clearing ObjectBox database files")

        try {
            val objectBoxDir = File(context.filesDir, "objectbox")
            if (objectBoxDir.exists()) {
                val deleted = objectBoxDir.deleteRecursively()
                Logger.Database.d("ObjectBox directory deletion result: $deleted")
            }
        } catch (e: Exception) {
            Logger.Database.w("Error during database file cleanup", e)
            // Don't rethrow - let the rebuild attempt proceed
        }
    }

    /** Returns true if the database was cleared during initialization due to schema conflicts. */
    @Synchronized
    fun wasDatabaseCleared(): Boolean = databaseWasCleared

    /** Resets the cleared flag (useful for testing or after successful re-sync). */
    @Synchronized
    fun resetClearedFlag() {
        databaseWasCleared = false
        Logger.Database.d("Database cleared flag reset")
    }

    fun startAdmin(context: Context) {
        val started = Admin(store).start(context)
        Logger.Database.d("ObjectBox Admin started: $started")
    }

    @Provides
    fun boxStore(): BoxStore {
        return store
    }
}