package com.itsjeel01.finsiblefrontend.feature.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.core.network.model.base.BaseResponse
import com.itsjeel01.finsiblefrontend.core.network.model.response.CategoryData
import com.itsjeel01.finsiblefrontend.core.storage.PreferenceManager
import com.itsjeel01.finsiblefrontend.core.storage.objectbox.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.core.storage.objectbox.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.AuthState
import com.itsjeel01.finsiblefrontend.data.model.TransactionType
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

    private val _authState =
        MutableStateFlow<AuthState>(AuthState.Negative("You are not logged in", isFailed = false))
    val authState: StateFlow<AuthState> = _authState

    init {
        if (preferenceManager.isLoggedIn()) {
            _authState.value = AuthState.Positive
        } else {
            _authState.value = AuthState.Negative("You are not logged in", isFailed = false)
        }
    }

    fun authenticate(clientId: String, token: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.authenticate(clientId, token)
                if (response.success) {
                    preferenceManager.saveAuthData(response.data) // Save user info to app prefs
                    _authState.value = AuthState.Positive // Update Auth state
                    fetchAndStoreCategories() // Fetch and store categories
                } else {
                    _authState.value = AuthState.Negative(response.message, isFailed = true)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Negative(e.message ?: "Unknown error occurred", isFailed = true)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferenceManager.clearAuthData()
            _authState.value = AuthState.Negative("You are not logged in", isFailed = false)
        }
    }

    private fun fetchAndStoreCategories() {
        viewModelScope.launch {
            try {
                val incomeCategoriesResponse =
                    categoryRepository.getCategories(TransactionType.INCOME.name)
                storeCategories(TransactionType.INCOME, incomeCategoriesResponse)

                val expenseCategoriesResponse =
                    categoryRepository.getCategories(TransactionType.EXPENSE.name)
                storeCategories(TransactionType.EXPENSE, expenseCategoriesResponse)
            } catch (e: Exception) {
                Log.e("CategoryFetch", e.message.toString())
            }
        }
    }

    private fun storeCategories(type: TransactionType, response: BaseResponse<CategoryData>) {
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