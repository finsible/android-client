package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.toAmountCentis
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.repository.AccountRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
import com.itsjeel01.finsiblefrontend.data.sync.DataFetcher
import com.itsjeel01.finsiblefrontend.data.sync.IntegrityChecker
import com.itsjeel01.finsiblefrontend.ui.mapper.toUiModel
import com.itsjeel01.finsiblefrontend.ui.model.item.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.model.item.CategoryUIModel
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTransactionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryLocalRepository: CategoryLocalRepository,
    private val accountLocalRepository: AccountLocalRepository,
    private val transactionLocalRepository: TransactionLocalRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val dataFetcher: DataFetcher,
    private val integrityChecker: IntegrityChecker,
    private val currencyFormatter: CurrencyFormatter,
) : ViewModel() {

    companion object {
        private const val MAX_INTEGER_DIGITS = 14
        private const val MAX_DECIMAL_DIGITS = 2
        private const val SUBSCRIPTION_TIMEOUT = 5000L
    }

    /** Consolidated form state for the new transaction flow. */
    private val _state = MutableStateFlow(NewTransactionFormState.DEFAULT)
    val state: StateFlow<NewTransactionFormState> = _state.asStateFlow()

    /** Mapped category UI models grouped by parent, re-queried only when transaction type changes. */
    val categories: StateFlow<Map<CategoryUIModel, List<CategoryUIModel>>> =
        state.map { it.transactionType }
            .distinctUntilChanged()
            .map { type ->
                categoryLocalRepository.getCategories(type)
                    .mapKeys { (k, _) -> k.toUiModel() }
                    .mapValues { (_, v) -> v.map { it.toUiModel() } }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
                initialValue = emptyMap()
            )

    /** Mapped account UI models for all available accounts. */
    private val _accounts = MutableStateFlow<List<AccountUIModel>>(emptyList())
    val accounts: StateFlow<List<AccountUIModel>> = _accounts.asStateFlow()

    /** Pre-filters "To" accounts to prevent TRANSFER self-selection. */
    val availableToAccounts: StateFlow<List<AccountUIModel>> = combine(
        _accounts,
        state
    ) { allAccounts, s ->
        if (s.transactionType == TransactionType.TRANSFER && s.fromAccountId != null) {
            allAccounts.filter { it.id != s.fromAccountId }
        } else {
            allAccounts
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
            _accounts.value = accountLocalRepository.getAll().map { it.toUiModel(currencyFormatter) }
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
            return _state.value.amountString

        if (input.count { it == '.' } > 1)
            return _state.value.amountString

        val filtered = input.filter { it.isDigit() || it == '.' }

        val parts = filtered.split('.')
        val integerPart = parts[0]
        val decimalPart = parts.getOrNull(1) ?: ""

        return when {
            integerPart.length > MAX_INTEGER_DIGITS -> _state.value.amountString
            decimalPart.length > MAX_DECIMAL_DIGITS -> _state.value.amountString
            integerPart.length > 1 && integerPart.startsWith("0") && !filtered.startsWith("0.") -> _state.value.amountString
            else -> filtered
        }
    }

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
        Route.Home.NewTransaction.Amount -> state.map { validateAmountStep(it.amountString) }
        Route.Home.NewTransaction.Date -> state.map { validateDateStep(it.dateMillis) }
        Route.Home.NewTransaction.Category -> state.map { validateCategoryStep(it.categoryId) }
        Route.Home.NewTransaction.TransactionAccounts -> state.map { s ->
            validateAccountStep(s.transactionType, s.fromAccountId, s.toAccountId)
        }

        Route.Home.NewTransaction.Description -> flowOf(true)
        else -> throw UnsupportedOperationException("Unrecognized transaction step: $step.")
    }

    fun setTransactionAmountString(amountStr: String) {
        _state.update { it.copy(amountString = amountStr) }
    }

    fun setTransactionDate(date: Long) {
        _state.update { it.copy(dateMillis = date) }
    }

    fun setIsRecurring(recurring: Boolean) {
        _state.update { it.copy(isRecurring = recurring) }
    }

    fun setRecurringFrequency(frequency: TransactionRecurringFrequency) {
        _state.update { it.copy(recurringFrequency = frequency) }
    }

    fun setTransactionType(type: TransactionType) {
        _state.update { s -> if (s.transactionType != type) s.copy(transactionType = type, categoryId = null) else s }
    }

    fun setTransactionCategoryId(id: Long) {
        _state.update { it.copy(categoryId = id) }
    }

    fun setTransactionFromAccountId(id: Long) {
        _state.update { it.copy(fromAccountId = id) }
    }

    fun setTransactionToAccountId(id: Long) {
        _state.update { it.copy(toAccountId = id) }
    }

    fun setTransactionDescription(description: String) {
        _state.update { it.copy(description = description) }
    }

    fun reset() {
        _state.value = NewTransactionFormState.DEFAULT
    }

    fun submit(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val s = _state.value
                val categoryName = getCategoryFromCache(s.categoryId)?.name ?: ""
                transactionLocalRepository.createTransaction(
                    type = s.transactionType,
                    totalAmount = s.amountString.toAmountCentis(),
                    transactionDate = s.dateMillis ?: System.currentTimeMillis(),
                    categoryId = s.categoryId ?: 0L,
                    categoryName = categoryName,
                    fromAccountId = s.fromAccountId ?: 0L,
                    toAccountId = s.toAccountId,
                    description = s.description.takeIf { it.isNotBlank() }
                )
                reset()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: context.getString(R.string.failed_to_create_transaction))
            }
        }
    }

    private fun getCategoryFromCache(id: Long?): CategoryUIModel? {
        if (id == null) return null
        val currentMap = categories.value
        for (subList in currentMap.values) {
            val found = subList.find { it.id == id }
            if (found != null) return found
        }
        return currentMap.keys.find { it.id == id }
    }

    private fun getAccountFromCache(id: Long?): AccountUIModel? {
        if (id == null) return null
        return _accounts.value.find { it.id == id }
    }
}
