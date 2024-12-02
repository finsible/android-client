package com.itsjeel01.finsiblefrontend.utils

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.itsjeel01.finsiblefrontend.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

fun signInWithGoogle(context: Context, coroutineScope: CoroutineScope) {
    val credentialsManager = CredentialManager.create(context)

    val rawNonce = UUID.randomUUID().toString()
    val bytes = rawNonce.toByteArray()
    val hashedNonce = MessageDigest.getInstance("SHA-256").digest(bytes)
        .fold("") { str, it -> str + "%02x".format(it) }

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.SERVER_CLIENT_ID) // web client ID
        .setNonce(hashedNonce)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    coroutineScope.launch {
        try {
            val result = credentialsManager.getCredential(request = request, context = context)
            val credential = result.credential

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

            Log.i("SignInWithGoogle", googleIdToken)
        } catch (e: Exception) {
            Log.e("SignInWithGoogle", e.message.toString())
        }
    }
}