package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.convertUTCToLocal
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.common.toReadableDate
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTransactionViewModel @Inject constructor(
    private val categoryLocalRepository: CategoryLocalRepository,
    private val accountLocalRepository: AccountLocalRepository
) : ViewModel() {

    companion object {
        private const val MAX_INTEGER_DIGITS = 15
        private const val MAX_DECIMAL_DIGITS = 4
        private const val SUBSCRIPTION_TIMEOUT = 5000L
    }

    /** Transaction form state. */
    private val _transactionAmountString = MutableStateFlow("")
    val transactionAmountString: StateFlow<String> = _transactionAmountString.stateFlow()

    private val _transactionDate = MutableStateFlow<Long?>(System.currentTimeMillis().convertUTCToLocal())
    val transactionDate: StateFlow<Long?> = _transactionDate.stateFlow()

    private val _isRecurring = MutableStateFlow(false)
    val isRecurring: StateFlow<Boolean> = _isRecurring.stateFlow()

    private val _recurringFrequency = MutableStateFlow(TransactionRecurringFrequency.DAILY)
    val recurringFrequency: StateFlow<TransactionRecurringFrequency> = _recurringFrequency.stateFlow()

    private val _transactionType = MutableStateFlow(TransactionType.EXPENSE)
    val transactionType: StateFlow<TransactionType> = _transactionType.stateFlow()

    private val _transactionCategoryId = MutableStateFlow<Long?>(null)
    val transactionCategoryId: StateFlow<Long?> = _transactionCategoryId.stateFlow()

    private val _transactionFromAccountId = MutableStateFlow<Long?>(null)
    val transactionFromAccountId: StateFlow<Long?> = _transactionFromAccountId.stateFlow()

    private val _transactionToAccountId = MutableStateFlow<Long?>(null)
    val transactionToAccountId: StateFlow<Long?> = _transactionToAccountId.stateFlow()

    private val _transactionDescription = MutableStateFlow("")
    val transactionDescription: StateFlow<String> = _transactionDescription.stateFlow()

    /** Data for categories based on transaction type. */
    val categories: StateFlow<HashMap<CategoryEntity, List<CategoryEntity>>> =
        transactionType.map { type ->
            categoryLocalRepository.getCategories(type)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
            initialValue = hashMapOf()
        )

    /** Available accounts for transaction. */
    private val _accounts = MutableStateFlow<List<AccountEntity>>(emptyList())
    val accounts: StateFlow<List<AccountEntity>> = _accounts.stateFlow()

    init {
        loadAccounts()
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            _accounts.value = accountLocalRepository.getAll()
        }
    }

    /** Validate amount input and return sanitized value or current value if invalid. */
    fun validateAmount(input: String): String {
        if (input.isEmpty()) return ""

        val filtered = input.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } > 1) return _transactionAmountString.value

        val parts = filtered.split('.')
        val integerPart = parts[0]
        val decimalPart = parts.getOrNull(1) ?: ""

        return when {
            integerPart.length > MAX_INTEGER_DIGITS -> _transactionAmountString.value
            decimalPart.length > MAX_DECIMAL_DIGITS -> _transactionAmountString.value
            integerPart.length > 1 && integerPart.startsWith("0") && !filtered.startsWith("0.") -> _transactionAmountString.value
            else -> filtered
        }
    }

    /** Step-specific validation methods. */
    private fun validateAmountStep(amountString: String): Boolean =
        validateAmount(amountString).isNotEmpty() && amountString.toDoubleOrNull()?.let { it > 0 } == true

    private fun validateDateStep(date: Long?): Boolean = date != null

    private fun validateCategoryStep(categoryId: Long?): Boolean = categoryId != null

    private fun validateAccountStep(type: TransactionType, fromAccountId: Long?, toAccountId: Long?): Boolean =
        when (type) {
            TransactionType.INCOME -> toAccountId != null
            TransactionType.EXPENSE -> fromAccountId != null
            TransactionType.TRANSFER -> fromAccountId != null && toAccountId != null && fromAccountId != toAccountId
        }

    fun isStepValid(step: Any): Flow<Boolean> = when (step) {
        Route.Home.NewTransaction.Amount -> transactionAmountString.map { validateAmountStep(it) }
        Route.Home.NewTransaction.Date -> transactionDate.map { validateDateStep(it) }
        Route.Home.NewTransaction.Category -> transactionCategoryId.map { validateCategoryStep(it) }
        Route.Home.NewTransaction.Accounts -> combine(transactionType, transactionFromAccountId, transactionToAccountId) { type, from, to ->
            validateAccountStep(type, from, to)
        }

        Route.Home.NewTransaction.Description -> flowOf(true)
        else -> throw IllegalArgumentException("Unrecognized transaction step: $step. Please add handling for this step in isStepValid().")
    }

    /** State setters with optimized patterns. */
    fun setTransactionAmountString(amountStr: String) {
        _transactionAmountString.value = amountStr
    }

    fun setTransactionDate(date: Long) {
        _transactionDate.value = date
    }

    fun setIsRecurring(recurring: Boolean) {
        _isRecurring.value = recurring
    }

    fun setRecurringFrequency(frequency: TransactionRecurringFrequency) {
        _recurringFrequency.value = frequency
    }

    fun setTransactionType(type: TransactionType) {
        if (_transactionType.value != type) {
            _transactionType.value = type
            _transactionCategoryId.value = null
        }
    }

    fun setTransactionCategoryId(id: Long) {
        _transactionCategoryId.value = id
    }

    fun setTransactionFromAccountId(id: Long) {
        _transactionFromAccountId.value = id
    }

    fun setTransactionToAccountId(id: Long) {
        _transactionToAccountId.value = id
    }

    fun setTransactionDescription(description: String) {
        _transactionDescription.value = description
    }

    fun toTxString(): String = buildString {
        appendLine("Transaction Details:")
        appendLine("Type: ${transactionType.value}")
        appendLine("Amount: ${transactionAmountString.value}")
        appendLine("Date: ${transactionDate.value?.toReadableDate()}")
        appendLine("Is Recurring: ${isRecurring.value}")
        appendLine("Recurring Frequency: ${recurringFrequency.value}")
        appendLine("Category: ${getCategory(transactionCategoryId.value)?.name}")
        appendLine("From Account: ${getAccount(transactionFromAccountId.value)?.name}")
        appendLine("To Account: ${getAccount(transactionToAccountId.value)?.name}")
        append("Description: ${transactionDescription.value}")
    }

    fun reset() {
        _transactionAmountString.value = ""
        _transactionDate.value = System.currentTimeMillis().convertUTCToLocal()
        _isRecurring.value = false
        _recurringFrequency.value = TransactionRecurringFrequency.DAILY
        _transactionType.value = TransactionType.EXPENSE
        _transactionCategoryId.value = null
        _transactionFromAccountId.value = null
        _transactionToAccountId.value = null
        _transactionDescription.value = ""
    }

    fun submit(uiFeedBack: () -> Unit) {
        uiFeedBack()
        Logger.UI.d(this.toTxString())
        this.reset()
    }


    private fun getCategory(id: Long?): CategoryEntity? = id?.let { categoryLocalRepository.get(it) }

    private fun getAccount(id: Long?): AccountEntity? = id?.let { accountLocalRepository.get(it) }

    private fun <T> MutableStateFlow<T>.stateFlow(): StateFlow<T> = this
}