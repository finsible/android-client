package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.convertUTCToLocal
import com.itsjeel01.finsiblefrontend.common.toReadableDate
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NewTransactionViewModel @Inject constructor(
    private val categoryLocalRepository: CategoryLocalRepository,
    private val accountLocalRepository: AccountLocalRepository
) : ViewModel() {

    companion object {
        private const val TAG = "NewTransactionViewModel"
        private const val TOTAL_STEPS = 5
        private const val MAX_INTEGER_DIGITS = 15
        private const val MAX_DECIMAL_DIGITS = 4
        private const val SUBSCRIPTION_TIMEOUT = 5000L
    }

    /** Transaction form state. */
    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.stateFlow()

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
    val accounts: List<AccountEntity> by lazy {
        accountLocalRepository.getAll()
    }

    /** Combined validation state for current step. */
    private data class ValidationState(
        val step: Int,
        val amountString: String,
        val date: Long?,
        val categoryId: Long?,
        val type: TransactionType,
        val fromAccountId: Long?,
        val toAccountId: Long?
    )

    /** Partial validation holder for the first 5 flows. */
    private data class PartialValidationState(
        val step: Int,
        val amountString: String,
        val date: Long?,
        val categoryId: Long?,
        val type: TransactionType
    )

    /** Computed state for step validation and navigation control. */
    private val firstFiveCombined = combine(
        currentStep,
        transactionAmountString,
        transactionDate,
        transactionCategoryId,
        transactionType
    ) { step, amount, date, categoryId, type ->
        PartialValidationState(step, amount, date, categoryId, type)
    }

    val canContinue: StateFlow<Boolean> = combine(
        firstFiveCombined,
        transactionFromAccountId,
        transactionToAccountId
    ) { partial, fromId, toId ->
        ValidationState(partial.step, partial.amountString, partial.date, partial.categoryId, partial.type, fromId, toId)
    }.map { state ->
        validateCurrentStep(state)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
        initialValue = false
    )

    val canGoBack: StateFlow<Boolean> = currentStep.map { it > 0 }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
        initialValue = false
    )

    val showBackButton: StateFlow<Boolean> = canGoBack

    /** Validate current step based on collected state. */
    private fun validateCurrentStep(state: ValidationState): Boolean = when (state.step) {
        0 -> validateAmountStep(state.amountString)
        1 -> validateDateStep(state.date)
        2 -> validateCategoryStep(state.categoryId)
        3 -> validateAccountStep(state.type, state.fromAccountId, state.toAccountId)
        4 -> true // Confirmation step
        else -> false
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

    /** Navigation methods. */
    fun nextStep() {
        if (_currentStep.value < TOTAL_STEPS - 1 && canContinue.value) {
            _currentStep.value += 1
        }
    }

    fun previousStep() {
        if (_currentStep.value > 0) {
            _currentStep.value -= 1
        }
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
        _currentStep.value = 0
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
        if (BuildConfig.DEBUG) Log.d(TAG, this.toTxString())
        this.reset()
    }

    fun totalSteps(): Int = TOTAL_STEPS

    private fun getCategory(id: Long?): CategoryEntity? = id?.let { categoryLocalRepository.get(it) }

    private fun getAccount(id: Long?): AccountEntity? = id?.let { accountLocalRepository.get(it) }

    private fun <T> MutableStateFlow<T>.stateFlow(): StateFlow<T> = this
}