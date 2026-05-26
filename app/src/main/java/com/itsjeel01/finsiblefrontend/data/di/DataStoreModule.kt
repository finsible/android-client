package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.migrations.SharedPreferencesMigration
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.itsjeel01.finsiblefrontend.common.datastore.EncryptedPreferenceSerializer
import com.itsjeel01.finsiblefrontend.common.datastore.UserPreferences
import kotlinx.serialization.json.Json
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val DATASTORE_FILE_NAME = "user_prefs.pb"
    private const val OLD_PREFS_FILE_NAME = "secret_shared_prefs"

    @Provides
    @Singleton
    fun provideTinkAead(@ApplicationContext context: Context): Aead {
        AeadConfig.register()

        // Initializes Google Tink, backed by the Android Keystore
        AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset", "secure_tink_prefs")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://tink_master_key")
            .build()
            .keysetHandle
            .let { return it.getPrimitive(Aead::class.java) }
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
        aead: Aead,
        json: Json,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = EncryptedPreferenceSerializer(aead, json),
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            migrations = listOf(
                SharedPreferencesMigration(
                    context = context,
                    sharedPreferencesName = OLD_PREFS_FILE_NAME,
                    keysToMigrate = setOf(
                        "jwt", "is_logged_in", "user_id", "email", "name",
                        "local_id_counter", "sync_enabled", "backup_enabled",
                        "wifi_only_sync", "currency"
                    )
                ) { sharedPrefs, currentData ->
                    // Map the legacy SharedPreferences data into the new DataClass
                    currentData.copy(
                        jwt = sharedPrefs.getString("jwt", currentData.jwt),
                        isLoggedIn = sharedPrefs.getBoolean("is_logged_in", currentData.isLoggedIn),
                        userId = sharedPrefs.getString("user_id", currentData.userId),
                        email = sharedPrefs.getString("email", currentData.email),
                        name = sharedPrefs.getString("name", currentData.name),
                        localIdCounter = sharedPrefs.getLong("local_id_counter", currentData.localIdCounter),
                        isSyncEnabled = sharedPrefs.getBoolean("sync_enabled", currentData.isSyncEnabled),
                        isBackupEnabled = sharedPrefs.getBoolean("backup_enabled", currentData.isBackupEnabled),
                        isWifiOnlySyncEnabled = sharedPrefs.getBoolean("wifi_only_sync", currentData.isWifiOnlySyncEnabled),
                        preferredCurrencyCode = sharedPrefs.getString("currency", currentData.preferredCurrencyCode)
                    )
                }
            )
        ) {
            context.dataStoreFile(DATASTORE_FILE_NAME)
        }
    }
}