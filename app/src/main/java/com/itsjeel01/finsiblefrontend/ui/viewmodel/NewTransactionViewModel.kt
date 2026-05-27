package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.toAmountCentis
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.ExchangeRateLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.data.repository.AccountRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import com.itsjeel01.finsiblefrontend.data.repository.ExchangeRateRepository
import com.itsjeel01.finsiblefrontend.data.sync.DataFetcher
import com.itsjeel01.finsiblefrontend.data.sync.IntegrityChecker
import com.itsjeel01.finsiblefrontend.data.sync.NetworkMonitor
import com.itsjeel01.finsiblefrontend.ui.mapper.toUiModel
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionDateSelection
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionValidationError
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class NewTransactionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryLocalRepository: CategoryLocalRepository,
    private val accountLocalRepository: AccountLocalRepository,
    private val transactionLocalRepository: TransactionLocalRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    val currencyRepository: CurrencyRepository,
    private val dataFetcher: DataFetcher,
    private val integrityChecker: IntegrityChecker,
    val currencyFormatter: CurrencyFormatter,
    val preferenceManager: PreferenceManager,
    private val exchangeRateLocalRepository: ExchangeRateLocalRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    companion object {
        private const val MAX_INTEGER_DIGITS = 14
        private const val MAX_DECIMAL_DIGITS = 2
        private const val NOTES_MAX_LENGTH = 256
        private const val SUBSCRIPTION_TIMEOUT = 5000L

        /** Consider rates older than this as stale (24 hours). */
        private const val RATE_STALE_THRESHOLD_MS = 86_400_000L

        /** Prevents rapid repeat refreshes while user types. */
        private const val RATE_REFRESH_COOLDOWN_MS = 30_000L
    }

    private val inFlightRateRefreshBases = mutableSetOf<String>()
    private val lastRateRefreshAttemptAt = mutableMapOf<String, Long>()
    private var onlineCollectorJob: Job? = null

    /** Consolidated form state for the new transaction flow. */
    private val _state = MutableStateFlow(NewTransactionFormState.DEFAULT)
    val state: StateFlow<NewTransactionFormState> = _state.asStateFlow()

    /** Currencies for the picker — pre-resolved via repository without exposing it to UI. */
    val availableCurrencies: StateFlow<List<Currency>> = state.map { it.currencyCode }
        .distinctUntilChanged()
        .map { currencyRepository.getAll(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), currencyRepository.getAll(""))

    private val _topKLimit = MutableStateFlow(5)
    val topKLimit: StateFlow<Int> = _topKLimit.asStateFlow()

    private inline fun <T> Iterable<T>.sortedContextually(
        crossinline getUsageCount: (T) -> Long,
        crossinline getLastUsedAt: (T) -> Long,
        crossinline getName: (T) -> String
    ): List<T> {
        val list = this.toList()
        if (list.isEmpty()) return emptyList()

        val hasUsage = list.any { getUsageCount(it) > 0 }
        return if (hasUsage) {
            list.sortedWith(
                compareByDescending<T> { getUsageCount(it) }
                    .thenByDescending { getLastUsedAt(it) }
                    .thenBy { getName(it).lowercase() }
            )
        } else {
            list.sortedBy { getName(it).lowercase() }
        }
    }

    /** Emits the grouped map for Bottom Sheets */
    val categoriesMap: StateFlow<Map<CategoryUIModel, List<CategoryUIModel>>> =
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

    /** Leaf + parent categories — parents sorted after leaf categories. */
    val topKCategories: StateFlow<List<CategoryUIModel>> = categoriesMap
        .map { rawMap ->
            val allCategories = rawMap.entries.flatMap { (parent, children) ->
                children + parent
            }
            allCategories
                .distinctBy { it.id }
                .sortedWith(
                    compareBy<CategoryUIModel> { it.isParent }
                        .thenByDescending { it.usageCount }
                        .thenByDescending { it.lastUsedAt ?: 0L }
                        .thenBy { it.name.lowercase() }
                )
        }.flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
            initialValue = emptyList()
        )

    /** Emits ALL active From accounts, sorted contextually */
    val topKFromAccounts: StateFlow<List<AccountUIModel>> = accountLocalRepository.getAccountsFlow()
        .map { entities ->
            val activeAccounts = entities.filter { it.isActive }
            activeAccounts.sortedContextually(
                getUsageCount = { it.usageCount },
                getLastUsedAt = { it.lastUsedAt ?: 0L },
                getName = { it.name }
            ).map { it.toUiModel(currencyFormatter) }
        }.flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
            initialValue = emptyList()
        )

    /** Emits ALL valid To accounts (excluding selected From account), sorted contextually */
    val topKToAccounts: StateFlow<List<AccountUIModel>> = combine(
        accountLocalRepository.getAccountsFlow(),
        state.map { it.fromAccountId }.distinctUntilChanged()
    ) { entities, fromId ->
        val validAccounts = entities.filter { it.isActive && it.id != fromId }
        validAccounts.sortedContextually(
            getUsageCount = { it.usageCount },
            getLastUsedAt = { it.lastUsedAt ?: 0L },
            getName = { it.name }
        ).map { it.toUiModel(currencyFormatter) }
    }.flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
            initialValue = emptyList()
        )

    init {
        networkMonitor.initialize()
        ensureDataFetched()
        hydrateDefaultCurrencyCode()
        observeConversion()
    }

    private fun observeConversion() {
        onlineCollectorJob?.cancel()
        onlineCollectorJob = viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                _state.update { it.copy(isOnline = online) }
            }
        }

        viewModelScope.launch {
            // Combine form state and preferred currency code to compute conversion display
            combine(
                state,
                preferenceManager.defaultCurrencyCodeFlow,
                networkMonitor.isOnline
            ) { formState, preferredCurrencyCode, isOnline ->
                Triple(formState, preferredCurrencyCode, isOnline)
            }.collect { (formState, preferredCurrencyCode, isOnline) ->
                updateConversionState(
                    formState = formState,
                    preferredCurrencyCode = preferredCurrencyCode,
                    isOnline = isOnline,
                    allowRefresh = true
                )
            }
        }
    }

    private fun updateConversionState(
        formState: NewTransactionFormState,
        preferredCurrencyCode: String,
        isOnline: Boolean,
        allowRefresh: Boolean
    ) {
        val amount = try {
            if (formState.amountString.isNotBlank()) {
                BigDecimal(formState.amountString)
            } else null
        } catch (_: NumberFormatException) {
            null
        }

        val currentCurrency = formState.currencyCode.trim().uppercase()
        val preferredCode = preferredCurrencyCode.trim().uppercase()

        if (amount == null || amount <= BigDecimal.ZERO) {
            _state.update {
                it.copy(
                    convertedAmountDisplay = null,
                    exchangeRateFreshness = com.itsjeel01.finsiblefrontend.ui.model.state.ExchangeRateFreshness.AVAILABLE
                )
            }
            return
        }

        if (currentCurrency.isBlank() || preferredCode.isBlank()) {
            _state.update {
                it.copy(
                    convertedAmountDisplay = null,
                    exchangeRateFreshness = com.itsjeel01.finsiblefrontend.ui.model.state.ExchangeRateFreshness.UNAVAILABLE
                )
            }
            return
        }

        if (currentCurrency == preferredCode) {
            _state.update {
                it.copy(
                    convertedAmountDisplay = null,
                    exchangeRateFreshness = com.itsjeel01.finsiblefrontend.ui.model.state.ExchangeRateFreshness.AVAILABLE
                )
            }
            return
        }

        val rateEntity = exchangeRateLocalRepository.getRateEntity(currentCurrency, preferredCode)
        if (rateEntity == null) {
            if (allowRefresh && isOnline) {
                maybeRefreshRates(baseCurrencyCode = currentCurrency)
            }
            _state.update {
                it.copy(
                    convertedAmountDisplay = null,
                    exchangeRateFreshness = com.itsjeel01.finsiblefrontend.ui.model.state.ExchangeRateFreshness.UNAVAILABLE
                )
            }
            return
        }

        // Perform exact financial math using BigDecimal.
        val rate = BigDecimal(rateEntity.rate.toString())
        val convertedAmount = amount.multiply(rate)

        // Convert to centis
        val cents = convertedAmount.multiply(BigDecimal(100))
            .setScale(0, RoundingMode.HALF_EVEN)
            .toLong()

        val display = currencyFormatter.format(
            centis = cents, currencyCode = preferredCode, options = CurrencyFormatter.CurrencyFormatOptions(
                includeSpaceAfterCurrencySymbol = true,
                includeSign = false
            )
        )

        val freshness = if (System.currentTimeMillis() - rateEntity.lastSyncedAt > RATE_STALE_THRESHOLD_MS) {
            com.itsjeel01.finsiblefrontend.ui.model.state.ExchangeRateFreshness.STALE
        } else {
            com.itsjeel01.finsiblefrontend.ui.model.state.ExchangeRateFreshness.AVAILABLE
        }

        if (allowRefresh && freshness == com.itsjeel01.finsiblefrontend.ui.model.state.ExchangeRateFreshness.STALE && isOnline) {
            maybeRefreshRates(baseCurrencyCode = currentCurrency)
        }

        _state.update { it.copy(convertedAmountDisplay = display, exchangeRateFreshness = freshness) }
    }

    private fun maybeRefreshRates(baseCurrencyCode: String) {
        val base = baseCurrencyCode.trim().uppercase()
        if (base.isBlank()) return

        val now = System.currentTimeMillis()
        val lastAttempt = lastRateRefreshAttemptAt[base] ?: 0L
        if (now - lastAttempt < RATE_REFRESH_COOLDOWN_MS) return
        if (!inFlightRateRefreshBases.add(base)) return

        lastRateRefreshAttemptAt[base] = now

        viewModelScope.launch {
            try {
                Logger.UI.d("Refreshing exchange rates (base=$base)")
                val success = exchangeRateRepository.refreshRatesAndCache(base)
                Logger.UI.d("Exchange rates refresh completed (base=$base, success=$success)")

                if (success) {
                    // Force a conversion re-evaluation so the UI updates even if the user stops typing.
                    val preferredCurrencyCode = preferenceManager.getDefaultCurrencyCode()
                    updateConversionState(
                        formState = state.value,
                        preferredCurrencyCode = preferredCurrencyCode,
                        isOnline = networkMonitor.isOnline.value,
                        allowRefresh = false
                    )
                }
            } finally {
                inFlightRateRefreshBases.remove(base)
            }
        }
    }

    private fun hydrateDefaultCurrencyCode() {
        viewModelScope.launch {
            val currencyCode = preferenceManager.getDefaultCurrencyCode()
            _state.update { it.copy(currencyCode = currencyCode) }
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

    fun onEvent(event: NewTransactionUiEvent) {
        when (event) {
            NewTransactionUiEvent.ResetClicked -> reset()
            is NewTransactionUiEvent.TransactionTypeChanged -> setTransactionType(event.type)
            is NewTransactionUiEvent.AmountChanged -> setTransactionAmountString(validateAmount(event.value))
            is NewTransactionUiEvent.CurrencySelected -> setCurrencyCode(event.currencyCode)
            is NewTransactionUiEvent.DateQuickSelectionChanged -> setQuickDate(event.selection)
            is NewTransactionUiEvent.CustomDateSelected -> {
                _state.update {
                    it.copy(
                        dateMillis = event.dateMillis,
                        dateSelection = NewTransactionDateSelection.CUSTOM,
                        validationErrors = it.validationErrors - NewTransactionValidationError.DATE
                    )
                }
            }
            // Handle the new clear event by reverting to Today
            NewTransactionUiEvent.ClearCustomDate -> setQuickDate(NewTransactionDateSelection.TODAY)

            is NewTransactionUiEvent.RecurringChanged -> setIsRecurring(event.isRecurring)
            is NewTransactionUiEvent.RecurringFrequencyChanged -> setRecurringFrequency(event.frequency)
            is NewTransactionUiEvent.CategorySelected -> setTransactionCategoryId(event.categoryId)
            is NewTransactionUiEvent.FromAccountSelected -> setTransactionFromAccountId(event.accountId)
            is NewTransactionUiEvent.ToAccountSelected -> setTransactionToAccountId(event.accountId)
            NewTransactionUiEvent.SwapAccounts -> {
                _state.update {
                    val updated = it.copy(
                        fromAccountId = it.toAccountId,
                        toAccountId = it.fromAccountId,
                        validationErrors = it.validationErrors - NewTransactionValidationError.TRANSFER_ACCOUNTS_MUST_DIFFER
                    )
                    syncTransferDifferenceError(updated)
                }
            }

            is NewTransactionUiEvent.NotesChanged -> setTransactionDescription(event.value)
            is NewTransactionUiEvent.SheetModeChanged -> _state.update { it.copy(sheetMode = event.mode) }
        }
    }

    private fun setQuickDate(selection: NewTransactionDateSelection) {
        val now = System.currentTimeMillis()
        val millis = when (selection) {
            NewTransactionDateSelection.TODAY -> now
            NewTransactionDateSelection.YESTERDAY -> now - 86_400_000L
            NewTransactionDateSelection.CUSTOM -> _state.value.dateMillis ?: now
        }

        _state.update {
            it.copy(
                dateSelection = selection,
                dateMillis = millis,
                validationErrors = it.validationErrors - NewTransactionValidationError.DATE
            )
        }
    }

    private fun validateForSave(snapshot: NewTransactionFormState): Set<NewTransactionValidationError> {
        val errors = mutableSetOf<NewTransactionValidationError>()

        val parsedAmount = snapshot.amountString.toDoubleOrNull()
        if (snapshot.amountString.isBlank() || parsedAmount == null || parsedAmount <= 0.0) {
            errors += NewTransactionValidationError.AMOUNT
        }

        if (snapshot.dateMillis == null) {
            errors += NewTransactionValidationError.DATE
        }

        if (snapshot.categoryId == null) {
            errors += NewTransactionValidationError.CATEGORY
        }

        when (snapshot.transactionType) {
            TransactionType.EXPENSE -> {
                if (snapshot.fromAccountId == null) {
                    errors += NewTransactionValidationError.FROM_ACCOUNT
                }
            }

            TransactionType.INCOME -> {
                if (snapshot.toAccountId == null) {
                    errors += NewTransactionValidationError.TO_ACCOUNT
                }
            }

            TransactionType.TRANSFER -> {
                if (snapshot.fromAccountId == null) {
                    errors += NewTransactionValidationError.FROM_ACCOUNT
                }
                if (snapshot.toAccountId == null) {
                    errors += NewTransactionValidationError.TO_ACCOUNT
                }
                if (snapshot.fromAccountId != null && snapshot.fromAccountId == snapshot.toAccountId) {
                    errors += NewTransactionValidationError.TRANSFER_ACCOUNTS_MUST_DIFFER
                }
            }
        }

        return errors
    }

    fun setTransactionAmountString(amountStr: String) {
        _state.update {
            it.copy(
                amountString = amountStr,
                validationErrors = it.validationErrors - NewTransactionValidationError.AMOUNT
            )
        }
    }

    private fun setCurrencyCode(currencyCode: String) {
        _state.update { it.copy(currencyCode = currencyCode) }
    }

    fun setIsRecurring(recurring: Boolean) {
        _state.update { it.copy(isRecurring = recurring) }
    }

    fun setRecurringFrequency(frequency: TransactionRecurringFrequency) {
        _state.update { it.copy(recurringFrequency = frequency) }
    }

    fun setTransactionType(type: TransactionType) {
        _state.update { s ->
            if (s.transactionType == type) {
                s
            } else {
                s.copy(
                    transactionType = type,
                    categoryId = null,
                    fromAccountId = if (type == TransactionType.INCOME) null else s.fromAccountId,
                    toAccountId = if (type == TransactionType.EXPENSE) null else s.toAccountId,
                    validationErrors = emptySet()
                )
            }
        }
    }

    fun setTransactionCategoryId(id: Long) {
        _state.update {
            it.copy(
                categoryId = id,
                validationErrors = it.validationErrors - NewTransactionValidationError.CATEGORY
            )
        }
    }

    fun setTransactionFromAccountId(id: Long) {
        _state.update {
            val updated = it.copy(
                fromAccountId = id,
                validationErrors = it.validationErrors - NewTransactionValidationError.FROM_ACCOUNT
            )
            syncTransferDifferenceError(updated)
        }
    }

    fun setTransactionToAccountId(id: Long) {
        _state.update {
            val updated = it.copy(
                toAccountId = id,
                validationErrors = it.validationErrors - NewTransactionValidationError.TO_ACCOUNT
            )
            syncTransferDifferenceError(updated)
        }
    }

    fun setTransactionDescription(description: String) {
        _state.update { it.copy(description = description.take(NOTES_MAX_LENGTH)) }
    }

    fun reset() {
        val currentCurrencyCode = _state.value.currencyCode
        _state.value = NewTransactionFormState.DEFAULT.copy(currencyCode = currentCurrencyCode)
    }

    fun submit(addMore: Boolean = false, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val snapshot = _state.value
        val validationErrors = validateForSave(snapshot)
        if (validationErrors.isNotEmpty()) {
            _state.update { it.copy(validationErrors = validationErrors) }
            return
        }

        _state.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            try {
                val categoryName = getCategoryFromCache(snapshot.categoryId)?.name ?: ""
                transactionLocalRepository.createTransaction(
                    type = snapshot.transactionType,
                    totalAmount = snapshot.amountString.toAmountCentis(),
                    transactionDate = snapshot.dateMillis ?: System.currentTimeMillis(),
                    categoryId = snapshot.categoryId ?: 0L,
                    categoryName = categoryName,
                    fromAccountId = snapshot.fromAccountId ?: 0L,
                    toAccountId = snapshot.toAccountId,
                    description = snapshot.description.takeIf { it.isNotBlank() },
                    currencyCode = snapshot.currencyCode
                )

                if (addMore) {
                    _state.value = NewTransactionFormState.DEFAULT.copy(
                        dateMillis = snapshot.dateMillis,
                        dateSelection = snapshot.dateSelection,
                        currencyCode = snapshot.currencyCode
                    )
                } else {
                    reset()
                }

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: context.getString(R.string.failed_to_create_transaction))
            } finally {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun getCategoryFromCache(id: Long?): CategoryUIModel? {
        if (id == null) return null
        val currentMap = categoriesMap.value
        for (subList in currentMap.values) {
            val found = subList.find { it.id == id }
            if (found != null) return found
        }
        return currentMap.keys.find { it.id == id }
    }

    private fun syncTransferDifferenceError(state: NewTransactionFormState): NewTransactionFormState {
        if (state.transactionType != TransactionType.TRANSFER) {
            return state.copy(
                validationErrors = state.validationErrors - NewTransactionValidationError.TRANSFER_ACCOUNTS_MUST_DIFFER
            )
        }

        val hasSameFromAndTo = state.fromAccountId != null && state.fromAccountId == state.toAccountId
        val nextErrors = if (hasSameFromAndTo) {
            state.validationErrors + NewTransactionValidationError.TRANSFER_ACCOUNTS_MUST_DIFFER
        } else {
            state.validationErrors - NewTransactionValidationError.TRANSFER_ACCOUNTS_MUST_DIFFER
        }

        return state.copy(validationErrors = nextErrors)
    }

}
