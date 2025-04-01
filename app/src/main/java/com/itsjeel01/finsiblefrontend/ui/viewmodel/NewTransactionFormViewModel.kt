package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.data.TransactionType
import com.itsjeel01.finsiblefrontend.data.objectbox.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.objectbox.repositories.CategoryLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NewTransactionFormViewModel @Inject constructor(
    private val categoryLocalRepository: CategoryLocalRepository,
) : ViewModel() {
    // Transaction type
    private val _transactionTypeState = MutableStateFlow(TransactionType.EXPENSE)
    val transactionTypeState: StateFlow<TransactionType> = _transactionTypeState

    // Transaction date
    private val _transactionDateState = MutableStateFlow(System.currentTimeMillis())
    val transactionDateState: StateFlow<Long> = _transactionDateState

    // Transaction amount
    private val _transactionAmountState = MutableStateFlow<Double?>(null)
    val transactionAmountState: StateFlow<Double?> = _transactionAmountState

    private val _currentCategoriesState =
        MutableStateFlow(
            categoryLocalRepository.getAllCategories()
                .filter { it.type == _transactionTypeState.value })
    val currentCategoriesState: StateFlow<List<CategoryEntity>> = _currentCategoriesState

    // Selected category
    private val _transactionCategoryState = MutableStateFlow<CategoryEntity>(currentCategoriesState.value.first())
    val transactionCategoryState: StateFlow<CategoryEntity> = _transactionCategoryState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _currentCategoriesState.value = categoryLocalRepository.getAllCategories()
            .filter { it.type == _transactionTypeState.value }
        _transactionCategoryState.value = _currentCategoriesState.value.first()
    }

    fun setTransactionType(transactionType: TransactionType) {
        _transactionTypeState.value = transactionType
        if (transactionType != TransactionType.TRANSFER) {
            loadCategories()
        }
    }

    fun setTransactionDate(transactionDate: Long) {
        _transactionDateState.value = transactionDate
    }

    fun setTransactionAmount(transactionAmount: Double?) {
        _transactionAmountState.value = transactionAmount
    }

    fun setTransactionCategory(category: CategoryEntity) {
        _transactionCategoryState.value = category
    }
}