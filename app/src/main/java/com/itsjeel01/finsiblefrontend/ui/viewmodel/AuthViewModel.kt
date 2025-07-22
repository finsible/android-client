package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.AuthState
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoriesData
import com.itsjeel01.finsiblefrontend.data.repository.AuthRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
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
    private val categoryLocalRepository: CategoryLocalRepository,
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
                Log.e(Strings.GOOGLE_LOGIN_UTIL, e.message.toString())
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
                val incomeCategoriesResponse =
                    categoryRepository.getCategories(TransactionType.INCOME.name)
                cacheCategory(TransactionType.INCOME, incomeCategoriesResponse)

                val expenseCategoriesResponse =
                    categoryRepository.getCategories(TransactionType.EXPENSE.name)
                cacheCategory(TransactionType.EXPENSE, expenseCategoriesResponse)
            } catch (e: Exception) {
                Log.e(Strings.CATEGORY, e.message.toString())
            }
        }
    }

    private fun cacheCategory(
        type: TransactionType,
        response: BaseResponse<CategoriesData>
    ) {
        viewModelScope.launch {
            if (response.success) {
                for (category in response.data.categories) {
                    categoryLocalRepository.addCategory(
                        CategoryEntity(
                            id = category.id,
                            type = type,
                            name = category.name,
                            color = category.color
                        )
                    )
                }
            }
        }
    }
}
