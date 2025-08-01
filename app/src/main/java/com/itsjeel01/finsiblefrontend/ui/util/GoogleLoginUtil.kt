package com.itsjeel01.finsiblefrontend.ui.util

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
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

            authViewModel.launchGoogleLogin(credentialsManager, request, context, clientId)
        }

        fun logout(
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
