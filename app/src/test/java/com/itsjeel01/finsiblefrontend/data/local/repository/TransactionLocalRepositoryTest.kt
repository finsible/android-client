package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.objectbox.Box
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/** Unit tests for TransactionLocalRepository CRUD and sync operations. */
class TransactionLocalRepositoryTest {

    private lateinit var mockTransactionBox: Box<TransactionEntity>
    private lateinit var mockSyncMetadataBox: Box<SyncMetadataEntity>
    private lateinit var mockPendingOperationBox: Box<PendingOperationEntity>
    private lateinit var mockLocalIdGenerator: LocalIdGenerator
    private lateinit var repository: TransactionLocalRepository

    @Before
    fun setUp() {
        mockTransactionBox = mockk(relaxed = true)
        mockSyncMetadataBox = mockk(relaxed = true)
        mockPendingOperationBox = mockk(relaxed = true)
        mockLocalIdGenerator = mockk(relaxed = true)
        repository = TransactionLocalRepository(
            mockTransactionBox,
            mockSyncMetadataBox,
            mockPendingOperationBox,
            mockLocalIdGenerator
        )
    }

    @Test
    fun `test addAll persists transactions to box`() {
        val transactions = listOf(
            createTransactionDTO(id = 1L),
            createTransactionDTO(id = 2L),
            createTransactionDTO(id = 3L)
        )

        repository.addAll(transactions, null, null)

        verify { mockTransactionBox.put(any<List<TransactionEntity>>()) }
    }

    @Test
    fun `test addAll converts DTOs to entities`() {
        val capturedEntities = slot<List<TransactionEntity>>()
        every { mockTransactionBox.put(capture(capturedEntities)) } returns Unit

        val transactions = listOf(
            createTransactionDTO(id = 1L, totalAmount = "100.00"),
            createTransactionDTO(id = 2L, totalAmount = "200.00")
        )

        repository.addAll(transactions, null, null)

        assertEquals(2, capturedEntities.captured.size)
        assertEquals(1L, capturedEntities.captured[0].id)
        assertEquals("100.00", capturedEntities.captured[0].totalAmount)
        assertEquals(2L, capturedEntities.captured[1].id)
        assertEquals("200.00", capturedEntities.captured[1].totalAmount)
    }

    @Test
    fun `test addAll converts transaction types correctly`() {
        val capturedEntities = slot<List<TransactionEntity>>()
        every { mockTransactionBox.put(capture(capturedEntities)) } returns Unit

        val transactions = listOf(
            createTransactionDTO(id = 1L, type = "EXPENSE"),
            createTransactionDTO(id = 2L, type = "INCOME"),
            createTransactionDTO(id = 3L, type = "TRANSFER")
        )

        repository.addAll(transactions, null, null)

        assertEquals(TransactionType.EXPENSE, capturedEntities.captured[0].type)
        assertEquals(TransactionType.INCOME, capturedEntities.captured[1].type)
        assertEquals(TransactionType.TRANSFER, capturedEntities.captured[2].type)
    }

    @Test
    fun `test updateSyncStatus updates existing transaction`() {
        val transaction = createTransactionEntity(id = 123L, syncStatus = Status.PENDING)
        every { mockTransactionBox.get(123L) } returns transaction

        repository.updateSyncStatus(123L, Status.COMPLETED, null)

        assertEquals(Status.COMPLETED, transaction.syncStatus)
        assertNull(transaction.syncError)
        verify { mockTransactionBox.put(transaction) }
    }

    @Test
    fun `test updateSyncStatus sets error message on failure`() {
        val transaction = createTransactionEntity(id = 123L)
        every { mockTransactionBox.get(123L) } returns transaction

        repository.updateSyncStatus(123L, Status.FAILED, "Network error")

        assertEquals(Status.FAILED, transaction.syncStatus)
        assertEquals("Network error", transaction.syncError)
    }

    @Test
    fun `test updateSyncStatus does nothing when transaction not found`() {
        every { mockTransactionBox.get(999L) } returns null

        repository.updateSyncStatus(999L, Status.COMPLETED, null)

        verify(exactly = 0) { mockTransactionBox.put(any<TransactionEntity>()) }
    }

    @Test
    fun `test remapId removes old ID and inserts with new ID`() {
        val updatedEntity = createTransactionEntity(id = 1001L)

        repository.remapId(-1L, 1001L, updatedEntity)

        verify { mockTransactionBox.remove(-1L) }
        verify { mockTransactionBox.put(updatedEntity) }
        assertEquals(Status.COMPLETED, updatedEntity.syncStatus)
        assertNull(updatedEntity.syncError)
    }

    @Test
    fun `test upsert persists transaction`() {
        val entity = createTransactionEntity(id = 1L)

        repository.upsert(entity)

        verify { mockTransactionBox.put(entity) }
    }

    @Test
    fun `test remove deletes transaction by ID`() {
        repository.removeById(123L)

        verify { mockTransactionBox.remove(123L) }
    }

    @Test
    fun `test clearAll removes all transactions`() {
        repository.clearAll()

        verify { mockTransactionBox.removeAll() }
    }

    @Test
    fun `test addAll preserves category information`() {
        val capturedEntities = slot<List<TransactionEntity>>()
        every { mockTransactionBox.put(capture(capturedEntities)) } returns Unit

        val transactions = listOf(
            createTransactionDTO(id = 1L, categoryId = 5L, categoryName = "Food")
        )

        repository.addAll(transactions, null, null)

        assertEquals(5L, capturedEntities.captured[0].categoryId)
        assertEquals("Food", capturedEntities.captured[0].categoryName)
    }

    @Test
    fun `test addAll preserves account information`() {
        val capturedEntities = slot<List<TransactionEntity>>()
        every { mockTransactionBox.put(capture(capturedEntities)) } returns Unit

        val dto = Transaction(
            id = 1L,
            type = "TRANSFER",
            totalAmount = "100.00",
            transactionDate = "1735689600000",
            categoryId = 1L,
            categoryName = "Transfer",
            currency = "INR",
            fromAccountId = 10L,
            toAccountId = 20L,
            description = null,
            spaceId = null,
            userShare = null,
            isSplit = false,
            paidByUserId = null,
            paidByUserName = null
        )

        repository.addAll(listOf(dto), null, null)

        assertEquals(10L, capturedEntities.captured[0].fromAccountId)
        assertEquals(20L, capturedEntities.captured[0].toAccountId)
    }

    private fun createTransactionDTO(
        id: Long = 1L,
        type: String = "EXPENSE",
        totalAmount: String = "100.00",
        transactionDate: String = "1735689600000",
        categoryId: Long = 1L,
        categoryName: String = "Test",
        currency: String = "INR"
    ) = Transaction(
        id = id,
        type = type,
        totalAmount = totalAmount,
        transactionDate = transactionDate,
        categoryId = categoryId,
        categoryName = categoryName,
        description = null,
        currency = currency,
        fromAccountId = null,
        toAccountId = null,
        spaceId = null,
        userShare = null,
        isSplit = false,
        paidByUserId = null,
        paidByUserName = null
    )

    private fun createTransactionEntity(
        id: Long = 1L,
        type: TransactionType = TransactionType.EXPENSE,
        totalAmount: String = "100.00",
        transactionDate: Long = System.currentTimeMillis(),
        categoryId: Long = 1L,
        categoryName: String = "Test",
        currency: String = "INR",
        fromAccountId: Long? = null,
        toAccountId: Long? = null,
        syncStatus: Status = Status.COMPLETED
    ) = TransactionEntity(
        id = id,
        type = type,
        totalAmount = totalAmount,
        transactionDate = transactionDate,
        categoryId = categoryId,
        categoryName = categoryName,
        description = null,
        currency = currency,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        syncStatus = syncStatus
    )
}
