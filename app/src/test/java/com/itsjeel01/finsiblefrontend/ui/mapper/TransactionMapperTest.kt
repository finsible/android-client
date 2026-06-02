package com.itsjeel01.finsiblefrontend.ui.mapper

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class TransactionMapperTest {

    private val currencyFormatter: CurrencyFormatter = mockk()
    private val context: Context = mockk()

    @Before
    fun setUp() {
        every { context.getString(R.string.account_unknown) } returns "?"
        // Catch-all for getString(int, Object...) with Java varargs (Object[] as single arg)
        every { context.getString(any<Int>(), *varargAll { true }) } answers {
            val formatArgs = args.drop(1).flatMap {
                if (it is Array<*>) it.toList() else listOf(it)
            }
            "${formatArgs[0]} → ${formatArgs[1]}"
        }
    }

    @Test
    fun `expense with description uses description as title`() {
        every { currencyFormatter.format(any(), "INR", any()) } returns "-₹1,000.00"

        val entity = TransactionEntity(
            id = 1L,
            type = TransactionType.EXPENSE,
            totalAmount = 1_000L,
            description = "Grocery run",
            categoryName = "Groceries",
            categoryIcon = "shopping_cart",
            currencyCode = "INR",
            transactionDate = 1_700_000_000_000L,
            fromAccountName = "HDFC Wallet",
        )

        val uiModel = entity.toUiModel(currencyFormatter, context)

        assertThat(uiModel.title).isEqualTo("Grocery run")
        assertThat(uiModel.subtitle).isEqualTo("HDFC Wallet")
        assertThat(uiModel.formattedAmount).isEqualTo("-₹1,000.00")
    }

    @Test
    fun `expense without description falls back to category name`() {
        every { currencyFormatter.format(any(), "INR", any()) } returns "-₹500.00"

        val entity = TransactionEntity(
            id = 2L,
            type = TransactionType.EXPENSE,
            totalAmount = 500L,
            description = null,
            categoryName = "Transport",
            categoryIcon = "directions_car",
            currencyCode = "INR",
            transactionDate = 1_700_000_000_000L,
            fromAccountName = "Wallet",
        )

        val uiModel = entity.toUiModel(currencyFormatter, context)

        assertThat(uiModel.title).isEqualTo("Transport")
    }

    @Test
    fun `transfer shows both account names in subtitle`() {
        every { currencyFormatter.format(any(), "INR", any()) } returns "₹2,000.00"

        val entity = TransactionEntity(
            id = 3L,
            type = TransactionType.TRANSFER,
            totalAmount = 2_000L,
            description = null,
            categoryName = "Transfer",
            currencyCode = "INR",
            transactionDate = 1_700_000_000_000L,
            fromAccountName = "Savings",
            toAccountName = "Checking",
        )

        val uiModel = entity.toUiModel(currencyFormatter, context)

        assertThat(uiModel.subtitle).isEqualTo("Savings → Checking")
        assertThat(uiModel.formattedAmount).doesNotContain("-")
        assertThat(uiModel.formattedAmount).doesNotContain("+")
    }

    @Test
    fun `transfer with null account names falls back to unknown label`() {
        every { currencyFormatter.format(any(), "USD", any()) } returns "$1.00"

        val entity = TransactionEntity(
            id = 4L,
            type = TransactionType.TRANSFER,
            totalAmount = 100L,
            description = null,
            categoryName = "Transfer",
            currencyCode = "USD",
            transactionDate = 1_700_000_000_000L,
            fromAccountName = null,
            toAccountName = null,
        )

        val uiModel = entity.toUiModel(currencyFormatter, context)

        assertThat(uiModel.subtitle).isEqualTo("? → ?")
    }

    @Test
    fun `income shows positive sign in formatted amount`() {
        every { currencyFormatter.format(any(), "INR", any()) } returns "+₹5,000.00"

        val entity = TransactionEntity(
            id = 5L,
            type = TransactionType.INCOME,
            totalAmount = 5_000L,
            description = "Salary",
            categoryName = "Income",
            currencyCode = "INR",
            transactionDate = 1_700_000_000_000L,
            toAccountName = "Savings",
        )

        val uiModel = entity.toUiModel(currencyFormatter, context)

        assertThat(uiModel.subtitle).isEqualTo("Savings")
        assertThat(uiModel.formattedAmount).startsWith("+")
    }

    @Test
    fun `maps all basic fields correctly`() {
        every { currencyFormatter.format(any(), "INR", any()) } returns "₹1,500.00"

        val entity = TransactionEntity(
            id = 99L,
            type = TransactionType.EXPENSE,
            totalAmount = 1_500L,
            description = "Dinner",
            categoryName = "Food",
            categoryIcon = "restaurant",
            currencyCode = "INR",
            transactionDate = 1_700_500_000_000L,
            fromAccountName = "Cash",
        )

        val uiModel = entity.toUiModel(currencyFormatter, context)

        assertThat(uiModel.id).isEqualTo(99L)
        assertThat(uiModel.type).isEqualTo(TransactionType.EXPENSE)
        assertThat(uiModel.categoryIcon).isEqualTo("restaurant")
        assertThat(uiModel.currencyCode).isEqualTo("INR")
        assertThat(uiModel.transactionDate).isEqualTo(1_700_500_000_000L)
        assertThat(uiModel.rawAmountCentis).isEqualTo(1_500L)
    }

    @Test
    fun `blank description falls back to category name`() {
        every { currencyFormatter.format(any(), "INR", any()) } returns "-₹300.00"

        val entity = TransactionEntity(
            id = 6L,
            type = TransactionType.EXPENSE,
            totalAmount = 300L,
            description = "",
            categoryName = "Coffee",
            currencyCode = "INR",
            transactionDate = 1_700_000_000_000L,
            fromAccountName = "Wallet",
        )

        val uiModel = entity.toUiModel(currencyFormatter, context)

        assertThat(uiModel.title).isEqualTo("Coffee")
    }
}
