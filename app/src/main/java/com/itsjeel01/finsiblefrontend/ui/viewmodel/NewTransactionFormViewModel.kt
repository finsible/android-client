package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow

class NewTransactionFormViewModel : ViewModel() {
    private val _transactionTypeState = MutableStateFlow(TransactionType.EXPENSE)
    val transactionTypeState: MutableStateFlow<TransactionType> = _transactionTypeState

    fun setTransactionType(transactionType: TransactionType) {
        _transactionTypeState.value = transactionType
    }
}
