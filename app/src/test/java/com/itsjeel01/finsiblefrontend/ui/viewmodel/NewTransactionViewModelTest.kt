package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.ExchangeRateLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.repository.AccountRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import com.itsjeel01.finsiblefrontend.data.repository.ExchangeRateRepository
import com.itsjeel01.finsiblefrontend.data.sync.DataFetcher
import com.itsjeel01.finsiblefrontend.data.sync.IntegrityChecker
import com.itsjeel01.finsiblefrontend.data.sync.NetworkMonitor
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewTransactionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // All dependencies as relaxed mocks — no explicit setup needed
    private val context: Context = mockk(relaxed = true)
    private val categoryLocalRepository: CategoryLocalRepository = mockk(relaxed = true)
    private val accountLocalRepository: AccountLocalRepository = mockk(relaxed = true)
    private val transactionLocalRepository: TransactionLocalRepository = mockk(relaxed = true)
    private val categoryRepository: CategoryRepository = mockk(relaxed = true)
    private val accountRepository: AccountRepository = mockk(relaxed = true)
    private val currencyRepository: CurrencyRepository = mockk(relaxed = true)
    private val exchangeRateLocalRepository: ExchangeRateLocalRepository = mockk(relaxed = true)
    private val exchangeRateRepository: ExchangeRateRepository = mockk(relaxed = true)
    private val dataFetcher: DataFetcher = mockk(relaxed = true)
    private val integrityChecker: IntegrityChecker = mockk(relaxed = true)
    private val currencyFormatter: CurrencyFormatter = mockk(relaxed = true)
    private val preferenceManager: PreferenceManager = mockk(relaxed = true)
    private val networkMonitor: NetworkMonitor = mockk(relaxed = true)

    private lateinit var viewModel: NewTransactionViewModel

    @Before
    fun setUp() {
        viewModel = NewTransactionViewModel(
            context = context,
            categoryLocalRepository = categoryLocalRepository,
            accountLocalRepository = accountLocalRepository,
            transactionLocalRepository = transactionLocalRepository,
            categoryRepository = categoryRepository,
            accountRepository = accountRepository,
            currencyRepository = currencyRepository,
            exchangeRateLocalRepository = exchangeRateLocalRepository,
            exchangeRateRepository = exchangeRateRepository,
            dataFetcher = dataFetcher,
            integrityChecker = integrityChecker,
            currencyFormatter = currencyFormatter,
            preferenceManager = preferenceManager,
            networkMonitor = networkMonitor,
        )
    }

    @Test
    fun `initial type is expense`() {
        assertThat(viewModel.state.value.transactionType).isEqualTo(TransactionType.EXPENSE)
    }

    @Test
    fun `setTransactionType updates type`() {
        viewModel.setTransactionType(TransactionType.INCOME)
        assertThat(viewModel.state.value.transactionType).isEqualTo(TransactionType.INCOME)

        viewModel.setTransactionType(TransactionType.TRANSFER)
        assertThat(viewModel.state.value.transactionType).isEqualTo(TransactionType.TRANSFER)
    }

    @Test
    fun `setTransactionDescription updates description`() {
        viewModel.setTransactionDescription("Groceries")
        assertThat(viewModel.state.value.description).isEqualTo("Groceries")

        viewModel.setTransactionDescription("")
        assertThat(viewModel.state.value.description).isEmpty()
    }

    @Test
    fun `setIsRecurring toggles recurring state`() {
        assertThat(viewModel.state.value.isRecurring).isFalse()

        viewModel.setIsRecurring(true)
        assertThat(viewModel.state.value.isRecurring).isTrue()

        viewModel.setIsRecurring(false)
        assertThat(viewModel.state.value.isRecurring).isFalse()
    }

    @Test
    fun `setRecurringFrequency updates frequency`() {
        viewModel.setRecurringFrequency(TransactionRecurringFrequency.MONTHLY)
        assertThat(viewModel.state.value.recurringFrequency).isEqualTo(TransactionRecurringFrequency.MONTHLY)
    }

    @Test
    fun `validateAmount passes valid decimal`() {
        val result = viewModel.validateAmount("1234.56")
        assertThat(result).isEqualTo("1234.56")
    }

    @Test
    fun `validateAmount filters non-numeric characters`() {
        val result = viewModel.validateAmount("12a34")
        assertThat(result).isEqualTo("1234")
    }

    @Test
    fun `validateAmount rejects multiple decimal points`() {
        // Setting initial amount so the fallback mechanism has a value
        viewModel.setTransactionAmountString("100.00")

        val result = viewModel.validateAmount("100.00.")
        assertThat(result).isEqualTo("100.00")
    }

    @Test
    fun `validateAmount allows only up to 2 decimal places`() {
        viewModel.setTransactionAmountString("100.00")

        val result = viewModel.validateAmount("100.123")
        assertThat(result).isEqualTo("100.00")
    }

    @Test
    fun `validateAmount passes empty string`() {
        val result = viewModel.validateAmount("")
        assertThat(result).isEmpty()
    }

    @Test
    fun `setTransactionAmountString updates amount string`() {
        viewModel.setTransactionAmountString("500.50")
        assertThat(viewModel.state.value.amountString).isEqualTo("500.50")
    }

    @Test
    fun `reset restores default state`() {
        viewModel.setTransactionType(TransactionType.INCOME)
        viewModel.setTransactionDescription("Test")
        viewModel.setTransactionAmountString("100")

        viewModel.reset()

        assertThat(viewModel.state.value.transactionType).isEqualTo(TransactionType.EXPENSE)
        assertThat(viewModel.state.value.description).isEmpty()
        assertThat(viewModel.state.value.amountString).isEmpty()
        assertThat(viewModel.state.value.categoryId).isNull()
    }
}
