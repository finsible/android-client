package com.itsjeel01.finsiblefrontend.ui.util

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthManager @Inject constructor() {

    suspend fun fetchGoogleIdToken(context: Context, clientId: String): Result<String> {
        return try {
            Logger.Auth.d("Fetching Google ID token for clientId: $clientId")
            val credentialsManager = CredentialManager.create(context)
            val request = createCredentialRequest(clientId)

            val result = credentialsManager.getCredential(context, request)
            val credential = result.credential
            val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken

            Logger.Auth.i("Google ID token fetched successfully")
            Result.success(idToken)
        } catch (e: Exception) {
            Logger.Auth.e("Error fetching Google ID token", e)
            Result.failure(e)
        }
    }

    private fun createCredentialRequest(clientId: String): GetCredentialRequest {
        val rawNonce = UUID.randomUUID().toString()
        val hashedNonce = MessageDigest.getInstance("SHA-256")
            .digest(rawNonce.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(clientId)
            .setNonce(hashedNonce)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }
}