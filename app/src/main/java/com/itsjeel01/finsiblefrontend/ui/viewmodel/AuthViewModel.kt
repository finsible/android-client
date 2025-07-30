package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun authenticate(clientId: String, idToken: String) {
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
                }
            } catch (e: Exception) {
                Log.e(Strings.GOOGLE_LOGIN_UTIL, e.toString())
                _authState.value =
                    AuthState.Negative(Strings.GENERIC_UNEXPECTED_ERROR_MESSAGE, isFailed = true)
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
