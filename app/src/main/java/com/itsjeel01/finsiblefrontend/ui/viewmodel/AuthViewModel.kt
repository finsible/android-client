package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.repository.AuthRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
import com.itsjeel01.finsiblefrontend.ui.data.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    // --- State ---

    private val _authState =
        MutableStateFlow<AuthState>(
            AuthState.Negative(
                Strings.AUTH_STATE_NEGATIVE_MESSAGE,
                isFailed = false
            )
        )
    val authState: StateFlow<AuthState> = _authState

    // --- Actions ---

    init {
        if (preferenceManager.isLoggedIn()) {
            _authState.value = AuthState.Positive
        } else {
            _authState.value =
                AuthState.Negative(Strings.AUTH_STATE_NEGATIVE_MESSAGE)
        }
    }

    private fun authenticate(clientId: String, idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val response = authRepository.authenticate(clientId, idToken)

                if (response.success) {
                    fetchCategories()

                    preferenceManager.saveAuthData(response.data)
                    _authState.value = AuthState.Positive
                } else {
                    _authState.value = AuthState.Negative(response.message, isFailed = true)
                    Log.e(Strings.AUTH_API, response.message)
                }
            } catch (e: Exception) {
                Log.e(Strings.AUTH_API, e.toString())

                if (e is HttpException) {
                    _authState.value = AuthState.Negative(
                        Strings.AUTH_SERVER_ERROR_MESSAGE,
                        isFailed = true
                    )
                } else {
                    _authState.value = AuthState.Negative(
                        Strings.AUTH_FAILED_GENERIC_MESSAGE,
                        isFailed = true
                    )
                }
            }

        }
    }

    fun launchGoogleLogin(
        credentialsManager: CredentialManager,
        request: GetCredentialRequest,
        context: Context,
        clientId: String
    ) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                val result = credentialsManager.getCredential(request = request, context = context)
                val credential = result.credential
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken

                authenticate(clientId = clientId, idToken = googleIdToken)
            } catch (e: Exception) {
                Log.e(Strings.GOOGLE_LOGIN_UTIL, e.toString())

                if (e is GetCredentialCancellationException) {
                    _authState.value =
                        AuthState.Negative(Strings.AUTH_STATE_NEGATIVE_MESSAGE, isFailed = true)
                } else {
                    _authState.value =
                        AuthState.Negative(Strings.AUTH_FAILED_GENERIC_MESSAGE, isFailed = true)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferenceManager.clearAuthData()
            _authState.value = AuthState.Negative(Strings.AUTH_STATE_NEGATIVE_MESSAGE)
        }
    }

    // --- Private Methods ---

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                categoryRepository.getCategories(TransactionType.INCOME.name)
                categoryRepository.getCategories(TransactionType.EXPENSE.name)
            } catch (e: Exception) {
                Log.e(Strings.CATEGORY, e.message.toString())
            }
        }
    }
}
