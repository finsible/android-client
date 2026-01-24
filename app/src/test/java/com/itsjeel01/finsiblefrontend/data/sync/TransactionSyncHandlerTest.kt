package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionUpdateRequest
import com.itsjeel01.finsiblefrontend.data.repository.TransactionRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/** Unit tests for TransactionSyncHandler. */
class TransactionSyncHandlerTest {

    private lateinit var mockRepository: TransactionRepository
    private lateinit var mockLocalRepository: TransactionLocalRepository
    private lateinit var json: Json
    private lateinit var syncHandler: TransactionSyncHandler

    @Before
    fun setUp() {
        mockRepository = mockk()
        mockLocalRepository = mockk(relaxed = true)
        json = Json { ignoreUnknownKeys = true }
        syncHandler = TransactionSyncHandler(mockRepository, mockLocalRepository, json)
    }

    // ============================================
    // Entity Type Tests
    // ============================================

    @Test
    fun testEntityTypeIsTransaction() {
        assertEquals("Handler should be for TRANSACTION", EntityType.TRANSACTION, syncHandler.entityType)
    }

    // ============================================
    // CREATE Operation Tests
    // ============================================

    @Test
    fun testProcessCreateSuccess() = runTest {
        val localId = -1L
        val serverId = 1001L
        val request = TransactionCreateRequest(
            type = "EXPENSE",
            totalAmount = "100.00",
            transactionDate = System.currentTimeMillis(),
            categoryId = 1L
        )
        val payload = json.encodeToString(TransactionCreateRequest.serializer(), request)
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            localEntityId = localId
            this.payload = payload
        }

        val serverTransaction = Transaction(
            id = serverId,
            type = "EXPENSE",
            totalAmount = "100.00",
            transactionDate = System.currentTimeMillis().toString(),
            categoryId = 1L,
            categoryName = "Test Category",
            description = null,
            currency = Currency.INR
        )

        val response = BaseResponse(
            success = true,
            message = "Transaction created",
            data = serverTransaction,
            cache = false
        )

        coEvery { mockRepository.createTransaction(any()) } returns response
        every { mockLocalRepository.remapId(any(), any(), any()) } just Runs

        syncHandler.processCreate(operation)

        coVerify(exactly = 1) { mockRepository.createTransaction(any()) }
        verify(exactly = 1) { mockLocalRepository.remapId(localId, serverId, any()) }
    }

    @Test
    fun testProcessCreateWithNullResponse() = runTest {
        val request = TransactionCreateRequest(
            type = "INCOME",
            totalAmount = "200.00",
            transactionDate = System.currentTimeMillis(),
            categoryId = 2L
        )
        val payload = json.encodeToString(TransactionCreateRequest.serializer(), request)
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            localEntityId = -1L
            this.payload = payload
        }

        val response = mockk<BaseResponse<Transaction>>(relaxed = true)
        every { response.success } returns false
        every { response.message } returns "Creation failed"

        coEvery { mockRepository.createTransaction(any()) } returns response

        try {
            syncHandler.processCreate(operation)
            fail("Should have thrown SyncException")
        } catch (e: SyncException) {
            assertTrue("Should contain error message", e.message!!.contains("Creation failed"))
        }
    }

    @Test
    fun testProcessCreateNetworkError() = runTest {
        val request = TransactionCreateRequest(
            type = "EXPENSE",
            totalAmount = "50.00",
            transactionDate = System.currentTimeMillis(),
            categoryId = 3L
        )
        val payload = json.encodeToString(TransactionCreateRequest.serializer(), request)
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            localEntityId = -2L
            this.payload = payload
        }

        coEvery { mockRepository.createTransaction(any()) } throws IOException("Network unreachable")

        try {
            syncHandler.processCreate(operation)
            fail("Should have thrown SyncException")
        } catch (e: SyncException) {
            assertTrue("Should be retryable", e.isRetryable)
            assertTrue("Should be network error", e.message!!.contains("Network"))
        }
    }

    @Test
    fun testProcessCreateHttpError401() = runTest {
        val request = TransactionCreateRequest(
            type = "EXPENSE",
            totalAmount = "75.00",
            transactionDate = System.currentTimeMillis(),
            categoryId = 4L
        )
        val payload = json.encodeToString(TransactionCreateRequest.serializer(), request)
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            localEntityId = -3L
            this.payload = payload
        }

        val httpException = HttpException(Response.error<Transaction>(401, mockk(relaxed = true)))
        coEvery { mockRepository.createTransaction(any()) } throws httpException

        try {
            syncHandler.processCreate(operation)
            fail("Should have thrown SyncException")
        } catch (e: SyncException) {
            assertFalse("401 should not be retryable", e.isRetryable)
            assertTrue("Should be auth error", e.message!!.contains("Authentication"))
        }
    }

    @Test
    fun testProcessCreateHttpError409() = runTest {
        val request = TransactionCreateRequest(
            type = "TRANSFER",
            totalAmount = "150.00",
            transactionDate = System.currentTimeMillis(),
            categoryId = 5L
        )
        val payload = json.encodeToString(TransactionCreateRequest.serializer(), request)
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            localEntityId = -4L
            this.payload = payload
        }

        val httpException = HttpException(Response.error<Transaction>(409, mockk(relaxed = true)))
        coEvery { mockRepository.createTransaction(any()) } throws httpException

        try {
            syncHandler.processCreate(operation)
            fail("Should have thrown SyncException")
        } catch (e: SyncException) {
            assertFalse("Conflict should not be retryable", e.isRetryable)
            assertTrue("Should be conflict error", e.message!!.contains("Conflict"))
        }
    }

    // ============================================
    // UPDATE Operation Tests
    // ============================================

    @Test
    fun testProcessUpdateSuccess() = runTest {
        val entityId = 1001L
        val request = TransactionUpdateRequest(totalAmount = "120.00")
        val payload = json.encodeToString(TransactionUpdateRequest.serializer(), request)
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.UPDATE
            this.entityId = entityId
            this.payload = payload
        }

        val updatedTransaction = Transaction(
            id = entityId,
            type = "EXPENSE",
            totalAmount = "120.00",
            transactionDate = System.currentTimeMillis().toString(),
            categoryId = 1L,
            categoryName = "Test Category",
            description = "Updated",
            currency = Currency.INR
        )

        val response = BaseResponse(
            success = true,
            message = "Updated",
            data = updatedTransaction,
            cache = false
        )

        coEvery { mockRepository.updateTransaction(entityId, any()) } returns response
        every { mockLocalRepository.upsert(any()) } just Runs

        syncHandler.processUpdate(operation)

        coVerify(exactly = 1) { mockRepository.updateTransaction(entityId, any()) }
        verify(exactly = 1) { mockLocalRepository.upsert(any()) }
    }

    @Test
    fun testProcessUpdateFailure() = runTest {
        val entityId = 1002L
        val request = TransactionUpdateRequest(description = "New description")
        val payload = json.encodeToString(TransactionUpdateRequest.serializer(), request)
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.UPDATE
            this.entityId = entityId
            this.payload = payload
        }

        val response = mockk<BaseResponse<Transaction>>(relaxed = true)
        every { response.success } returns false
        every { response.message } returns "Update failed"

        coEvery { mockRepository.updateTransaction(entityId, any()) } returns response

        try {
            syncHandler.processUpdate(operation)
            fail("Should have thrown SyncException")
        } catch (e: SyncException) {
            assertTrue("Should contain error message", e.message!!.contains("Update failed"))
        }
    }

    // ============================================
    // DELETE Operation Tests
    // ============================================

    @Test
    fun testProcessDeleteSuccess() = runTest {
        val entityId = 1003L
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.DELETE
            this.entityId = entityId
            payload = "{}"
        }

        val response = BaseResponse(
            success = true,
            message = "Deleted",
            data = Unit,
            cache = false
        )

        coEvery { mockRepository.deleteTransaction(entityId) } returns response
        every { mockLocalRepository.removeById(entityId) } just Runs

        syncHandler.processDelete(operation)

        coVerify(exactly = 1) { mockRepository.deleteTransaction(entityId) }
        verify(exactly = 1) { mockLocalRepository.removeById(entityId) }
    }

    @Test
    fun testProcessDeleteAlreadyDeleted() = runTest {
        val entityId = 1004L
        val operation = PendingOperationEntity().apply {
            entityType = EntityType.TRANSACTION
            operationType = OperationType.DELETE
            this.entityId = entityId
            payload = "{}"
        }

        val httpException = HttpException(Response.error<Unit>(404, mockk(relaxed = true)))
        coEvery { mockRepository.deleteTransaction(entityId) } throws httpException
        every { mockLocalRepository.removeById(entityId) } just Runs

        // Should not throw - 404 on delete means already deleted
        syncHandler.processDelete(operation)

        // Should still clean up locally
        verify(exactly = 1) { mockLocalRepository.removeById(entityId) }
    }
}
