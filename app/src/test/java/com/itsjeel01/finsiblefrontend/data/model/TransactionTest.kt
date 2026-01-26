package com.itsjeel01.finsiblefrontend.data.model

import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.toDTO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Unit tests for Transaction DTO and toEntity() conversion. */
class TransactionTest {

    @Test
    fun `test toEntity converts all fields correctly`() {
        val dto = Transaction(
            id = 123L,
            type = "EXPENSE",
            totalAmount = "250.75",
            transactionDate = "1735689600000",
            categoryId = 5L,
            categoryName = "Food",
            description = "Dinner",
            currency = Currency.INR,
            fromAccountId = 10L,
            toAccountId = null,
            spaceId = null,
            userShare = null,
            isSplit = false,
            paidByUserId = null,
            paidByUserName = null
        )

        val entity = dto.toEntity()

        assertEquals(123L, entity.id)
        assertEquals(TransactionType.EXPENSE, entity.type)
        assertEquals("250.75", entity.totalAmount)
        assertEquals(1735689600000L, entity.transactionDate)
        assertEquals(5L, entity.categoryId)
        assertEquals("Food", entity.categoryName)
        assertEquals("Dinner", entity.description)
        assertEquals(Currency.INR, entity.currency)
        assertEquals(10L, entity.fromAccountId)
        assertNull(entity.toAccountId)
        assertEquals(Status.COMPLETED, entity.syncStatus)
    }

    @Test
    fun `test toEntity with custom sync status`() {
        val dto = createTransaction()

        val entity = dto.toEntity(syncStatus = Status.PENDING)

        assertEquals(Status.PENDING, entity.syncStatus)
    }

    @Test
    fun `test toEntity defaults to COMPLETED sync status`() {
        val dto = createTransaction()

        val entity = dto.toEntity()

        assertEquals(Status.COMPLETED, entity.syncStatus)
    }

    @Test
    fun `test toEntity handles INCOME type`() {
        val dto = createTransaction(type = "INCOME")

        val entity = dto.toEntity()

        assertEquals(TransactionType.INCOME, entity.type)
    }

    @Test
    fun `test toEntity handles TRANSFER type`() {
        val dto = createTransaction(
            type = "TRANSFER",
            fromAccountId = 1L,
            toAccountId = 2L
        )

        val entity = dto.toEntity()

        assertEquals(TransactionType.TRANSFER, entity.type)
        assertEquals(1L, entity.fromAccountId)
        assertEquals(2L, entity.toAccountId)
    }

    @Test
    fun `test toEntity handles invalid transactionDate gracefully`() {
        val dto = createTransaction(transactionDate = "invalid")

        val entity = dto.toEntity()

        assertEquals(0L, entity.transactionDate)
    }

    @Test
    fun `test toEntity handles empty transactionDate`() {
        val dto = createTransaction(transactionDate = "")

        val entity = dto.toEntity()

        assertEquals(0L, entity.transactionDate)
    }

    @Test
    fun `test toEntity handles split expense fields`() {
        val dto = Transaction(
            id = 1L,
            type = "EXPENSE",
            totalAmount = "100.00",
            transactionDate = "1735689600000",
            categoryId = 1L,
            categoryName = "Test",
            description = null,
            currency = Currency.INR,
            fromAccountId = 1L,
            toAccountId = null,
            spaceId = 100L,
            userShare = "50.00",
            isSplit = true,
            paidByUserId = 42L,
            paidByUserName = "Alice"
        )

        val entity = dto.toEntity()

        assertEquals(100L, entity.spaceId)
        assertEquals("50.00", entity.userShare)
        assertEquals(true, entity.isSplit)
        assertEquals(42L, entity.paidByUserId)
        assertEquals("Alice", entity.paidByUserName)
    }

    @Test
    fun `test toEntity preserves null optional fields`() {
        val dto = Transaction(
            id = 1L,
            type = "EXPENSE",
            totalAmount = "50.00",
            transactionDate = "1735689600000",
            categoryId = 1L,
            categoryName = "Test",
            description = null,
            currency = Currency.INR,
            fromAccountId = null,
            toAccountId = null,
            spaceId = null,
            userShare = null,
            isSplit = false,
            paidByUserId = null,
            paidByUserName = null
        )

        val entity = dto.toEntity()

        assertNull(entity.description)
        assertNull(entity.fromAccountId)
        assertNull(entity.toAccountId)
        assertNull(entity.spaceId)
        assertNull(entity.userShare)
        assertNull(entity.paidByUserId)
        assertNull(entity.paidByUserName)
    }

    @Test
    fun `test round trip DTO to Entity to DTO preserves data`() {
        val originalDto = Transaction(
            id = 999L,
            type = "EXPENSE",
            totalAmount = "123.45",
            transactionDate = "1735689600000",
            categoryId = 10L,
            categoryName = "Shopping",
            description = "Groceries",
            currency = Currency.INR,
            fromAccountId = 5L,
            toAccountId = null,
            spaceId = null,
            userShare = null,
            isSplit = false,
            paidByUserId = null,
            paidByUserName = null
        )

        val entity = originalDto.toEntity()
        val resultDto = entity.toDTO()

        assertEquals(originalDto.id, resultDto.id)
        assertEquals(originalDto.type, resultDto.type)
        assertEquals(originalDto.totalAmount, resultDto.totalAmount)
        assertEquals(originalDto.transactionDate, resultDto.transactionDate)
        assertEquals(originalDto.categoryId, resultDto.categoryId)
        assertEquals(originalDto.categoryName, resultDto.categoryName)
        assertEquals(originalDto.description, resultDto.description)
        assertEquals(originalDto.currency, resultDto.currency)
        assertEquals(originalDto.fromAccountId, resultDto.fromAccountId)
        assertEquals(originalDto.toAccountId, resultDto.toAccountId)
        assertEquals(originalDto.isSplit, resultDto.isSplit)
    }

    @Test
    fun `test currency value INR is preserved`() {
        val dto = createTransaction(currency = Currency.INR)
        assertEquals(Currency.INR, dto.toEntity().currency)
    }

    private fun createTransaction(
        id: Long = 1L,
        type: String = "EXPENSE",
        totalAmount: String = "100.00",
        transactionDate: String = "1735689600000",
        categoryId: Long = 1L,
        categoryName: String = "Test",
        description: String? = null,
        currency: Currency = Currency.INR,
        fromAccountId: Long? = 1L,
        toAccountId: Long? = null,
        spaceId: Long? = null,
        userShare: String? = null,
        isSplit: Boolean = false,
        paidByUserId: Long? = null,
        paidByUserName: String? = null
    ) = Transaction(
        id = id,
        type = type,
        totalAmount = totalAmount,
        transactionDate = transactionDate,
        categoryId = categoryId,
        categoryName = categoryName,
        description = description,
        currency = currency,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        spaceId = spaceId,
        userShare = userShare,
        isSplit = isSplit,
        paidByUserId = paidByUserId,
        paidByUserName = paidByUserName
    )
}
