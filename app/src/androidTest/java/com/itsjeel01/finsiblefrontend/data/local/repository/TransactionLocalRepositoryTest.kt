package com.itsjeel01.finsiblefrontend.data.local.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TransactionLocalRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var transactionLocalRepository: TransactionLocalRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        transactionLocalRepository.clearAll()
    }

    @Test
    fun queryByDateRangeReturnsOnlyTransactionsWithinRange() {
        transactionLocalRepository.addAll(
            listOf(
                Transaction(id = 1, type = "EXPENSE", totalAmount = "1.00", transactionDate = 1_700_000_000_000L, categoryId = 1, categoryName = "Food", currencyCode = "INR"),
                Transaction(id = 2, type = "EXPENSE", totalAmount = "2.00", transactionDate = 1_700_086_400_000L, categoryId = 1, categoryName = "Food", currencyCode = "INR"),
                Transaction(id = 3, type = "EXPENSE", totalAmount = "3.00", transactionDate = 1_700_172_800_000L, categoryId = 1, categoryName = "Food", currencyCode = "INR"),
            ),
            additionalInfo = null
        )

        val result = transactionLocalRepository.queryTransactions(
            dateRangeStart = 1_700_000_000_000L,
            dateRangeEnd = 1_700_086_400_000L,
        )

        assertThat(result.transactions).hasSize(2)
    }

    @Test
    fun combinedFiltersCorrectlyIntersectResults() {
        transactionLocalRepository.addAll(
            listOf(
                Transaction(id = 4, type = "EXPENSE", totalAmount = "1.00", transactionDate = 1_700_000_000_000L, categoryId = 1, categoryName = "Food", currencyCode = "INR"),
                Transaction(id = 5, type = "INCOME", totalAmount = "5.00", transactionDate = 1_700_086_400_000L, categoryId = 2, categoryName = "Salary", currencyCode = "INR"),
            ),
            additionalInfo = null
        )

        val result = transactionLocalRepository.queryTransactions(
            transactionTypes = setOf(com.itsjeel01.finsiblefrontend.common.TransactionType.INCOME),
        )

        assertThat(result.transactions).hasSize(1)
    }
}
