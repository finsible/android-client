package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.convertUTCToLocal
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.repository.AccountRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
import com.itsjeel01.finsiblefrontend.data.sync.DataFetcher
import com.itsjeel01.finsiblefrontend.data.sync.IntegrityChecker
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import com.itsjeel01.finsiblefrontend.ui.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTransactionViewModel @Inject constructor(
    private val categoryLocalRepository: CategoryLocalRepository,
    private val accountLocalRepository: AccountLocalRepository,
    private val transactionLocalRepository: TransactionLocalRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val dataFetcher: DataFetcher,
    private val integrityChecker: IntegrityChecker,
) : ViewModel() {

    companion object {
        private const val MAX_INTEGER_DIGITS = 15
        private const val MAX_DECIMAL_DIGITS = 4
        private const val SUBSCRIPTION_TIMEOUT = 5000L
    }

    private val _uiState = MutableStateFlow(NewTransactionUiState())
    val uiState: StateFlow<NewTransactionUiState> = _uiState

    /** Data for categories based on transaction type. */
    val categories: StateFlow<Map<CategoryEntity, List<CategoryEntity>>> =
        _uiState.map { it.type }
            .distinctUntilChanged()
            .map { type ->
                categoryLocalRepository.getCategories(type)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
                initialValue = hashMapOf()
            )

    /** Pre-calculates the valid "To" accounts to prevent main-thread filtering. */
    val availableToAccounts: StateFlow<List<AccountEntity>> = _uiState.map { state ->
        if (state.type == TransactionType.TRANSFER && state.fromAccountId != null) {
            state.accounts.filter { it.id != state.fromAccountId }
        } else {
            state.accounts
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
        initialValue = emptyList()
    )

    init {
        loadAccounts()
        ensureDataFetched()
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            _uiState.update { it.copy(accounts = accountLocalRepository.getAll()) }
        }
    }

    /** Auto-fetch categories and accounts if never synced. */
    private fun ensureDataFetched() {
        viewModelScope.launch {
            dataFetcher.ensureDataFetched(
                fetcher = { categoryRepository.getCategories(TransactionType.EXPENSE.name) },
                verifyIntegrity = { integrityChecker.verifyCategoriesIntegrity() }
            )
            dataFetcher.ensureDataFetched(
                fetcher = { categoryRepository.getCategories(TransactionType.INCOME.name) },
                verifyIntegrity = { integrityChecker.verifyCategoriesIntegrity() }
            )
            dataFetcher.ensureDataFetched(
                fetcher = { categoryRepository.getCategories(TransactionType.TRANSFER.name) },
                verifyIntegrity = { integrityChecker.verifyCategoriesIntegrity() }
            )
            dataFetcher.ensureDataFetched(
                fetcher = { accountRepository.getAccounts() },
                verifyIntegrity = { integrityChecker.verifyAccountsIntegrity() }
            )
        }
    }

    /** Validate amount input and return sanitized value or current value if invalid. */
    fun validateAmount(input: String): String {
        if (input.isEmpty()) return ""

        if (input.length > MAX_INTEGER_DIGITS + MAX_DECIMAL_DIGITS + 2)
            return _uiState.value.amountString

        if (input.count { it == '.' } > 1)
            return _uiState.value.amountString

        val filtered = input.filter { it.isDigit() || it == '.' }

        val parts = filtered.split('.')
        val integerPart = parts[0]
        val decimalPart = parts.getOrNull(1) ?: ""

        return when {
            integerPart.length > MAX_INTEGER_DIGITS -> _uiState.value.amountString
            decimalPart.length > MAX_DECIMAL_DIGITS -> _uiState.value.amountString
            integerPart.length > 1 && integerPart.startsWith("0") && !filtered.startsWith("0.") -> _uiState.value.amountString
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
        Route.Home.NewTransaction.Amount -> _uiState.map { validateAmountStep(it.amountString) }
        Route.Home.NewTransaction.Date -> _uiState.map { validateDateStep(it.date) }
        Route.Home.NewTransaction.Category -> _uiState.map { validateCategoryStep(it.categoryId) }
        Route.Home.NewTransaction.TransactionAccounts -> _uiState.map { state ->
            validateAccountStep(state.type, state.fromAccountId, state.toAccountId)
        }

        Route.Home.NewTransaction.Description -> flowOf(true)
        else -> throw UnsupportedOperationException("Unrecognized transaction step: $step. Please add handling for this step in isStepValid().")
    }

    fun setTransactionAmountString(amountStr: String) {
        _uiState.update { it.copy(amountString = amountStr) }
    }

    fun setTransactionDate(date: Long) {
        _uiState.update { it.copy(date = date) }
    }

    fun setIsRecurring(recurring: Boolean) {
        _uiState.update { it.copy(isRecurring = recurring) }
    }

    fun setRecurringFrequency(frequency: TransactionRecurringFrequency) {
        _uiState.update { it.copy(recurringFrequency = frequency) }
    }

    fun setTransactionType(type: TransactionType) {
        _uiState.update { current ->
            if (current.type != type) current.copy(type = type, categoryId = null)
            else current
        }
    }

    fun setTransactionCategoryId(id: Long) {
        _uiState.update { it.copy(categoryId = id) }
    }

    fun setTransactionFromAccountId(id: Long) {
        _uiState.update { it.copy(fromAccountId = id) }
    }

    fun setTransactionToAccountId(id: Long) {
        _uiState.update { it.copy(toAccountId = id) }
    }

    fun setTransactionDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    /**
     * OPTIMIZATION: Uses cached data in memory instead of blocking DB calls.
     */
    fun toTxString(): String = buildString {
        val state = _uiState.value
        appendLine("Transaction Details:")
        appendLine("Type: ${state.type}")
        appendLine("Amount: ${state.amountString}")
        appendLine("Date: ${DateUtils.readableDate(state.date ?: System.currentTimeMillis())}")
        appendLine("Is Recurring: ${state.isRecurring}")
        appendLine("Recurring Frequency: ${state.recurringFrequency}")
        appendLine("Category: ${getCategoryFromCache(state.categoryId)?.name}")
        appendLine("From Account: ${getAccountFromCache(state.fromAccountId)?.name}")
        appendLine("To Account: ${getAccountFromCache(state.toAccountId)?.name}")
        append("Description: ${state.description}")
    }

    fun reset() {
        _uiState.update { current -> NewTransactionUiState(accounts = current.accounts) }
    }

    fun submit(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val categoryName = getCategoryFromCache(state.categoryId)?.name ?: ""

                transactionLocalRepository.createTransaction(
                    type = state.type,
                    totalAmount = state.amountString,
                    transactionDate = state.date ?: System.currentTimeMillis(),
                    categoryId = state.categoryId ?: 0L,
                    categoryName = categoryName,
                    fromAccountId = state.fromAccountId ?: 0L,
                    toAccountId = state.toAccountId,
                    description = state.description.takeIf { it.isNotBlank() }
                )
                reset()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to create transaction")
            }
        }
    }

    private fun getCategoryFromCache(id: Long?): CategoryEntity? {
        if (id == null) return null
        val currentMap = categories.value
        // Search through the values (lists of subcategories)
        for (subList in currentMap.values) {
            val found = subList.find { it.id == id }
            if (found != null) return found
        }
        // Also check keys (parent categories) if they can be selected
        return currentMap.keys.find { it.id == id }
    }

    private fun getAccountFromCache(id: Long?): AccountEntity? {
        if (id == null) return null
        return _uiState.value.accounts.find { it.id == id }
    }
}

/** UI state for the new transaction form. */
@Immutable
data class NewTransactionUiState(
    val amountString: String = "",
    val date: Long? = System.currentTimeMillis().convertUTCToLocal(),
    val isRecurring: Boolean = false,
    val recurringFrequency: TransactionRecurringFrequency = TransactionRecurringFrequency.DAILY,
    val type: TransactionType = TransactionType.EXPENSE,
    val categoryId: Long? = null,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val description: String = "",
    val accounts: List<AccountEntity> = emptyList()
)