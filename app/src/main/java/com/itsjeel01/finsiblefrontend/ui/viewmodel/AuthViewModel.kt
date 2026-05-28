package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.repository.AuthRepository
import com.itsjeel01.finsiblefrontend.data.sync.PostAuthInitializer
import com.itsjeel01.finsiblefrontend.ui.model.state.AuthState
import com.itsjeel01.finsiblefrontend.ui.util.GoogleAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepo: AuthRepository,
    private val googleAuthManager: GoogleAuthManager,
    private val postAuthInitializer: PostAuthInitializer,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            if (authRepo.isAuthenticated()) {
                _authState.value = AuthState.Positive
            } else {
                _authState.value = AuthState.Negative()
            }
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
            is HttpException -> context.getString(R.string.auth_error_server)
            is GetCredentialCancellationException -> context.getString(R.string.auth_error_cancelled)
            is java.net.UnknownHostException -> context.getString(R.string.auth_error_no_internet)
            is java.net.SocketTimeoutException -> context.getString(R.string.auth_error_timeout)
            else -> context.getString(R.string.auth_error_unexpected)
        }

        _authState.value = AuthState.Negative(message, isFailed = true)
    }

    fun logout() {
        viewModelScope.launch {
            authRepo.logout()
            _authState.value = AuthState.Negative()
        }
    }
}