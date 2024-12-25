package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewTransactionFormViewModel : ViewModel() {
    private val _transactionTypeState = MutableStateFlow(TransactionType.EXPENSE)
    val transactionTypeState: StateFlow<TransactionType> = _transactionTypeState

    private val _transactionDateState = MutableStateFlow(System.currentTimeMillis())
    val transactionDateState: StateFlow<Long> = _transactionDateState

    fun setTransactionType(transactionType: TransactionType) {
        _transactionTypeState.value = transactionType
    }

    fun setTransactionDate(transactionDate: Long) {
        _transactionDateState.value = transactionDate
    }
}
