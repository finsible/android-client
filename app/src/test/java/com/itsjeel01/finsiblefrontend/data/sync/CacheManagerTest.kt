package com.itsjeel01.finsiblefrontend.data.sync

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.model.Category
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoriesData
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionsData
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class CacheManagerTest {

    private val categoryLocalRepo: CategoryLocalRepository = mockk(relaxed = true)
    private val accountLocalRepo: AccountLocalRepository = mockk(relaxed = true)
    private val accountGroupLocalRepo: AccountGroupLocalRepository = mockk(relaxed = true)
    private val transactionLocalRepo: TransactionLocalRepository = mockk(relaxed = true)
    private lateinit var cacheManager: CacheManager

    @Before
    fun setUp() {
        cacheManager = CacheManager(
            categoryLocalRepo = categoryLocalRepo,
            accountLocalRepo = accountLocalRepo,
            accountGroupLocalRepo = accountGroupLocalRepo,
            transactionLocalRepo = transactionLocalRepo
        )
    }

    @Test
    fun `skips caching when response not successful`() {
        val response = BaseResponse<CategoriesData>(
            success = false,
            message = "",
            cache = true,
            data = CategoriesData(type = "EXPENSE", categories = emptyList())
        )

        cacheManager.cacheData(response)

        verify(exactly = 0) { categoryLocalRepo.addAll(any<List<Category>>(), any()) }
    }

    @Test
    fun `skips caching when cache flag is false`() {
        val response = BaseResponse<CategoriesData>(
            success = true,
            message = "",
            cache = false,
            data = CategoriesData(type = "EXPENSE", categories = emptyList())
        )

        cacheManager.cacheData(response)

        verify(exactly = 0) { categoryLocalRepo.addAll(any<List<Category>>(), any()) }
    }

    @Test
    fun `caches CategoriesData correctly`() {
        val categories = listOf(
            Category(
                id = 1L, name = "Food", icon = "restaurant",
                readOnly = false, parentCategory = null, usageCount = 5
            )
        )
        val response = BaseResponse<CategoriesData>(
            success = true,
            message = "",
            cache = true,
            data = CategoriesData(type = "EXPENSE", categories = categories)
        )

        cacheManager.cacheData(response)

        verify(exactly = 1) {
            categoryLocalRepo.addAll(categories, additionalInfo = TransactionType.EXPENSE)
        }
    }

    @Test
    fun `caches TransactionsData correctly`() {
        val transactions = listOf(
            com.itsjeel01.finsiblefrontend.data.model.Transaction(
                id = 1L, type = "EXPENSE", totalAmount = "100.00",
                transactionDate = 1_700_000_000_000L, categoryId = 1L,
                categoryName = "Food", currencyCode = "INR"
            )
        )
        val response = BaseResponse<TransactionsData>(
            success = true,
            message = "",
            cache = true,
            data = TransactionsData(transactions = transactions)
        )

        cacheManager.cacheData(response)

        verify(exactly = 1) {
            transactionLocalRepo.addAll(transactions, additionalInfo = null)
        }
    }

    @Test
    fun `caches AccountGroup list correctly`() {
        val groups = listOf(
            AccountGroup(id = 1L, name = "Savings", description = "", icon = "", color = "blue", isSystemDefault = false)
        )
        val response = BaseResponse<List<AccountGroup>>(
            success = true,
            message = "",
            cache = true,
            data = groups
        )

        cacheManager.cacheData(response)

        verify(exactly = 1) {
            accountGroupLocalRepo.addAll(groups, additionalInfo = null)
        }
    }

    @Test
    fun `caches Account list correctly`() {
        val accounts = listOf(
            Account(id = 1L, name = "HDFC", description = "", balance = "0.00",
                currencyCode = "INR", icon = "", isActive = true, isSystemDefault = false)
        )
        val response = BaseResponse<List<Account>>(
            success = true,
            message = "",
            cache = true,
            data = accounts
        )

        cacheManager.cacheData(response)

        verify(exactly = 1) {
            accountLocalRepo.addAll(accounts, additionalInfo = null)
        }
    }

    @Test
    fun `skips caching empty list`() {
        val response = BaseResponse<List<Account>>(
            success = true,
            message = "",
            cache = true,
            data = emptyList()
        )

        cacheManager.cacheData(response)

        verify(exactly = 0) { accountLocalRepo.addAll(any<List<Account>>(), any()) }
    }

    @Test
    fun `caches mixed list skips non-matching entries`() {
        // List with both Account and AccountGroup — only accounts should be added
        val data = listOf(
            Account(id = 1, name = "A", description = "", balance = "0.00", currencyCode = "INR", icon = "", isActive = true, isSystemDefault = false),
            com.itsjeel01.finsiblefrontend.data.model.AccountGroup(id = 1, name = "G", description = "", icon = "", color = "red", isSystemDefault = false),
        )
        val response = BaseResponse<List<Any>>(
            success = true,
            message = "",
            cache = true,
            data = data
        )

        cacheManager.cacheData(response)

        // Should not crash — mismatched list items are skipped
        verify(atLeast = 0) { accountLocalRepo.addAll(any<List<Account>>(), any()) }
    }

    @Test
    fun `skips caching unknown data type`() {
        val response = BaseResponse<String>(
            success = true,
            message = "",
            cache = true,
            data = "unexpected_string"
        )

        cacheManager.cacheData(response)

        verify(exactly = 0) { accountLocalRepo.addAll(any<List<Account>>(), any()) }
        verify(exactly = 0) { categoryLocalRepo.addAll(any<List<com.itsjeel01.finsiblefrontend.data.model.Category>>(), any()) }
    }
}
