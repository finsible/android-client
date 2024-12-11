package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.data.models.AuthState
import com.itsjeel01.finsiblefrontend.data.repositories.AuthRepository
import com.itsjeel01.finsiblefrontend.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager,
) : ViewModel() {

    private val _authState =
        MutableStateFlow<AuthState>(AuthState.Negative("You are not logged in"))
    val authState: StateFlow<AuthState> = _authState

    init {
        if (preferenceManager.isLoggedIn()) {
            _authState.value = AuthState.Positive
        } else {
            _authState.value = AuthState.Negative("You are not logged in")
        }
    }

    fun authenticate(clientId: String, token: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.authenticate(clientId, token)
                if (response.success) {
                    preferenceManager.saveAuthData(response)
                    _authState.value = AuthState.Positive
                } else {
                    _authState.value = AuthState.Negative(response.message.toString())
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Negative(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferenceManager.clearAuthData()
            _authState.value = AuthState.Negative("You are not logged in")
        }
    }
}