package com.itsjeel01.finsiblefrontend.ui.util

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class GoogleLoginUtil {
    companion object {
        fun login(
            context: Context,
            coroutineScope: CoroutineScope,
            authViewModel: AuthViewModel,
        ) {
            val credentialsManager = CredentialManager.create(context)
            val clientId = BuildConfig.SERVER_CLIENT_ID

            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val hashedNonce = MessageDigest.getInstance("SHA-256").digest(bytes)
                .fold("") { str, it -> str + "%02x".format(it) }

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(clientId)
                .setNonce(hashedNonce)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            coroutineScope.launch {
                try {
                    val result =
                        credentialsManager.getCredential(request = request, context = context)
                    val credential = result.credential

                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken

                    authViewModel.authenticate(clientId = clientId, idToken = googleIdToken)
                } catch (e: Exception) {
                    Log.e(Strings.GOOGLE_LOGIN_UTIL, e.toString())
                }
            }
        }

        fun logout(
            context: Context,
            coroutineScope: CoroutineScope,
            authViewModel: AuthViewModel,
        ) {
            coroutineScope.launch {
                try {
                    authViewModel.logout()
                } catch (e: Exception) {
                    Log.e(Strings.GOOGLE_LOGIN_UTIL, e.toString())
                }
            }
        }
    }
}
