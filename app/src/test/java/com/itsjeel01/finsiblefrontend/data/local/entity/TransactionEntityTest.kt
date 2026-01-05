package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

/** Unit tests for TransactionEntity data class and computed properties. */
class TransactionEntityTest {

    @Test
    fun `test periodKey computed property for January 2025`() {
        val cal = Calendar.getInstance().apply {
            set(2025, Calendar.JANUARY, 15, 12, 0, 0)
        }
        val entity = createTransaction(transactionDate = cal.timeInMillis)

        assertEquals("2025-01", entity.periodKey)
    }

    @Test
    fun `test periodKey computed property for December 2024`() {
        val cal = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 25, 10, 30, 0)
        }
        val entity = createTransaction(transactionDate = cal.timeInMillis)

        assertEquals("2024-12", entity.periodKey)
    }

    @Test
    fun `test periodKey pads single digit months`() {
        val cal = Calendar.getInstance().apply {
            set(2025, Calendar.MARCH, 1, 0, 0, 0)
        }
        val entity = createTransaction(transactionDate = cal.timeInMillis)

        assertEquals("2025-03", entity.periodKey)
    }

    @Test
    fun `test periodKey for first day of month`() {
        val cal = Calendar.getInstance().apply {
            set(2025, Calendar.JUNE, 1, 0, 0, 0)
        }
        val entity = createTransaction(transactionDate = cal.timeInMillis)

        assertEquals("2025-06", entity.periodKey)
    }

    @Test
    fun `test periodKey for last day of month`() {
        val cal = Calendar.getInstance().apply {
            set(2025, Calendar.FEBRUARY, 28, 23, 59, 59)
        }
        val entity = createTransaction(transactionDate = cal.timeInMillis)

        assertEquals("2025-02", entity.periodKey)
    }

    @Test
    fun `test isLocalOnly returns true for negative IDs`() {
        val entity = createTransaction(id = -1L)
        assertTrue("ID -1 should be local only", entity.isLocalOnly)

        val entity2 = createTransaction(id = -999L)
        assertTrue("ID -999 should be local only", entity2.isLocalOnly)
    }

    @Test
    fun `test isLocalOnly returns false for positive IDs`() {
        val entity = createTransaction(id = 1L)
        assertFalse("ID 1 should not be local only", entity.isLocalOnly)

        val entity2 = createTransaction(id = 1000L)
        assertFalse("ID 1000 should not be local only", entity2.isLocalOnly)
    }

    @Test
    fun `test isLocalOnly returns false for zero ID`() {
        val entity = createTransaction(id = 0L)
        assertFalse("ID 0 should not be local only", entity.isLocalOnly)
    }

    @Test
    fun `test toDTO converts all fields correctly`() {
        val entity = TransactionEntity(
            id = 123L,
            type = TransactionType.EXPENSE,
            totalAmount = "150.50",
            transactionDate = 1735689600000L,
            categoryId = 5L,
            categoryName = "Food",
            description = "Lunch",
            currency = "INR",
            fromAccountId = 10L,
            toAccountId = null,
            syncStatus = Status.COMPLETED,
            spaceId = null,
            userShare = null,
            isSplit = false,
            paidByUserId = null,
            paidByUserName = null
        )

        val dto = entity.toDTO()

        assertEquals(123L, dto.id)
        assertEquals("EXPENSE", dto.type)
        assertEquals("150.50", dto.totalAmount)
        assertEquals("1735689600000", dto.transactionDate)
        assertEquals(5L, dto.categoryId)
        assertEquals("Food", dto.categoryName)
        assertEquals("Lunch", dto.description)
        assertEquals("INR", dto.currency)
        assertEquals(10L, dto.fromAccountId)
        assertNull(dto.toAccountId)
        assertFalse(dto.isSplit)
    }

    @Test
    fun `test toDTO handles transfer type`() {
        val entity = createTransaction(
            type = TransactionType.TRANSFER,
            fromAccountId = 1L,
            toAccountId = 2L
        )

        val dto = entity.toDTO()

        assertEquals("TRANSFER", dto.type)
        assertEquals(1L, dto.fromAccountId)
        assertEquals(2L, dto.toAccountId)
    }

    @Test
    fun `test toDTO handles income type`() {
        val entity = createTransaction(
            type = TransactionType.INCOME,
            toAccountId = 5L
        )

        val dto = entity.toDTO()

        assertEquals("INCOME", dto.type)
        assertEquals(5L, dto.toAccountId)
    }

    @Test
    fun `test toDTO handles split expense fields`() {
        val entity = createTransaction(
            isSplit = true,
            spaceId = 100L,
            userShare = "50.00",
            paidByUserId = 42L,
            paidByUserName = "John Doe"
        )

        val dto = entity.toDTO()

        assertTrue(dto.isSplit)
        assertEquals(100L, dto.spaceId)
        assertEquals("50.00", dto.userShare)
        assertEquals(42L, dto.paidByUserId)
        assertEquals("John Doe", dto.paidByUserName)
    }

    @Test
    fun `test default values are set correctly`() {
        val entity = TransactionEntity()

        assertEquals(0L, entity.id)
        assertEquals(TransactionType.EXPENSE, entity.type)
        assertEquals("0.0", entity.totalAmount)
        assertEquals(0L, entity.transactionDate)
        assertEquals("INR", entity.currency)
        assertEquals(Status.COMPLETED, entity.syncStatus)
        assertFalse(entity.isSplit)
    }

    @Test
    fun `test entity with all nullable fields null`() {
        val entity = TransactionEntity(
            id = 1L,
            type = TransactionType.EXPENSE,
            totalAmount = "100.00",
            transactionDate = System.currentTimeMillis(),
            categoryId = 1L,
            categoryName = "Test",
            description = null,
            currency = "INR",
            fromAccountId = null,
            toAccountId = null,
            syncStatus = Status.PENDING,
            lastSyncAttempt = null,
            syncError = null,
            spaceId = null,
            userShare = null,
            isSplit = false,
            paidByUserId = null,
            paidByUserName = null
        )

        val dto = entity.toDTO()

        assertNull(dto.description)
        assertNull(dto.fromAccountId)
        assertNull(dto.toAccountId)
        assertNull(dto.spaceId)
        assertNull(dto.userShare)
        assertNull(dto.paidByUserId)
        assertNull(dto.paidByUserName)
    }

    private fun createTransaction(
        id: Long = 1L,
        type: TransactionType = TransactionType.EXPENSE,
        totalAmount: String = "100.00",
        transactionDate: Long = System.currentTimeMillis(),
        categoryId: Long = 1L,
        categoryName: String = "Test Category",
        description: String? = null,
        currency: String = "INR",
        fromAccountId: Long? = 1L,
        toAccountId: Long? = null,
        syncStatus: Status = Status.COMPLETED,
        isSplit: Boolean = false,
        spaceId: Long? = null,
        userShare: String? = null,
        paidByUserId: Long? = null,
        paidByUserName: String? = null
    ): TransactionEntity = TransactionEntity(
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
        syncStatus = syncStatus,
        isSplit = isSplit,
        spaceId = spaceId,
        userShare = userShare,
        paidByUserId = paidByUserId,
        paidByUserName = paidByUserName
    )
}

