package com.itsjeel01.finsiblefrontend.common.datastore

import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class EncryptedPreferenceSerializer(
    private val aead: Aead,
    private val json: Json,
) : Serializer<UserPreferences> {

    override val defaultValue: UserPreferences = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        return withContext(Dispatchers.IO) {
            try {
                // Read the encrypted bytes from disk and decrypt them
                val encryptedBytes = input.readBytes()
                if (encryptedBytes.isEmpty()) return@withContext defaultValue

                val decryptedBytes = aead.decrypt(encryptedBytes, null)
                val decodedJson = decryptedBytes.decodeToString()

                json.decodeFromString(UserPreferences.serializer(), decodedJson)
            } catch (e: SerializationException) {
                e.printStackTrace()
                defaultValue
            } catch (e: Exception) {
                e.printStackTrace()
                defaultValue // If Tink fails to decrypt (e.g., keyset tampered with), fallback to default
            }
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            // Serialize the data class to JSON, encrypt it, and write to disk
            val jsonString = json.encodeToString(UserPreferences.serializer(), t)
            val encryptedBytes = aead.encrypt(jsonString.encodeToByteArray(), null)
            output.write(encryptedBytes)
        }
    }
}