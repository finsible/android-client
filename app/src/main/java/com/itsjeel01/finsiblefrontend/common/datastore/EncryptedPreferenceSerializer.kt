package com.itsjeel01.finsiblefrontend.common.datastore

import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class EncryptedPreferenceSerializer(
    private val aead: Aead,
    private val json: Json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
) : Serializer<UserPreferences> {

    override val defaultValue: UserPreferences = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        val encryptedBytes = try {
            input.readBytes()
        } catch (e: Exception) {
            Logger.App.e("Failed to read preference file from disk", e)
            return defaultValue
        }

        if (encryptedBytes.isEmpty()) {
            Logger.App.i("Empty preference file, using defaults (first launch)")
            return defaultValue
        }

        val decryptedBytes = try {
            aead.decrypt(encryptedBytes, null)
        } catch (e: Exception) {
            Logger.App.e("Preference decryption failed — possible data corruption or keyset mismatch", e)
            return defaultValue
        }

        val decodedJson = decryptedBytes.decodeToString()

        return try {
            json.decodeFromString(UserPreferences.serializer(), decodedJson)
        } catch (e: SerializationException) {
            Logger.App.w("Failed to deserialize preferences (schema change?), using defaults", e)
            defaultValue
        } catch (e: Exception) {
            Logger.App.e("Unexpected error during preferences deserialization", e)
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        val jsonString = json.encodeToString(UserPreferences.serializer(), t)
        val encryptedBytes = aead.encrypt(jsonString.encodeToByteArray(), null)
        
        withContext(Dispatchers.IO) {
            output.write(encryptedBytes)
            output.flush()
        }
    }
}