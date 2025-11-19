package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.data.repository.AuthRepository
import com.itsjeel01.finsiblefrontend.data.sync.PostAuthInitializer
import com.itsjeel01.finsiblefrontend.ui.model.AuthState
import com.itsjeel01.finsiblefrontend.ui.util.GoogleAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val googleAuthManager: GoogleAuthManager,
    private val postAuthInitializer: PostAuthInitializer,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Negative())
    val authState: StateFlow<AuthState> = _authState

    init {
        if (authRepo.isAuthenticated()) {
            _authState.value = AuthState.Positive
        }
    }

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val clientId = BuildConfig.SERVER_CLIENT_ID

            googleAuthManager.fetchGoogleIdToken(context, clientId)
                .onSuccess { idToken ->
                    authenticateWithBackend(clientId, idToken)
                }
                .onFailure { exception ->
                    handleAuthError(exception)
                }
        }
    }

    private suspend fun authenticateWithBackend(clientId: String, idToken: String) {
        authRepo.authenticate(clientId, idToken)
            .onSuccess { _ ->
                _authState.value = AuthState.Positive
                postAuthInitializer.initialize()
            }
            .onFailure { exception ->
                handleAuthError(exception)
            }
    }

    private fun handleAuthError(exception: Throwable) {
        val message = when (exception) {
            is HttpException -> "It's not you, it's us. Please try again later."
            is GetCredentialCancellationException -> "Sign-in cancelled, please log in."
            is java.net.UnknownHostException -> "No internet connection. Please check your network."
            is java.net.SocketTimeoutException -> "The request timed out. Please try again."
            else -> "An unexpected error occurred. Please try again."
        }

        _authState.value = AuthState.Negative(message, isFailed = true)
    }

    fun logout() {
        viewModelScope.launch {
            authRepo.logout()
            _authState.value = AuthState.Negative()
        }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}