package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val categoryLocalRepository: CategoryLocalRepository,
) : ViewModel() {

    // --- State ---

    private val _transactionType = MutableStateFlow(TransactionType.EXPENSE)
    val transactionType: StateFlow<TransactionType> = _transactionType

    private val _transactionDate: MutableStateFlow<Long?> = MutableStateFlow(null)
    val transactionDate: StateFlow<Long?> = _transactionDate

    private val _transactionAmount = MutableStateFlow<Double?>(null)
    val transactionAmount: StateFlow<Double?> = _transactionAmount

    private val _categories =
        MutableStateFlow(
            categoryLocalRepository.getAllCategories()
                .filter { it.type == _transactionType.value })
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _transactionCategory =
        MutableStateFlow(categories.value.first())
    val transactionCategory: StateFlow<CategoryEntity> = _transactionCategory

    // --- Actions ---

    init {
        loadCategories()
    }

    fun setTransactionType(transactionType: TransactionType) {
        _transactionType.value = transactionType
        if (transactionType != TransactionType.TRANSFER) {
            loadCategories()
        }
    }

    fun setTransactionDate(transactionDate: Long?) {
        _transactionDate.value = transactionDate
    }

    fun setTransactionAmount(transactionAmount: Double?) {
        _transactionAmount.value = transactionAmount
    }

    fun setTransactionCategory(category: CategoryEntity) {
        _transactionCategory.value = category
    }

    // --- Private Methods ---

    private fun loadCategories() {
        _categories.value = categoryLocalRepository.getAllCategories()
            .filter { it.type == _transactionType.value }
        _transactionCategory.value = _categories.value.first()
    }
}
