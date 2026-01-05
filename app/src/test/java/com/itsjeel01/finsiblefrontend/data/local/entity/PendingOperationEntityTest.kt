package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Unit tests for PendingOperationEntity data class and fields. */
class PendingOperationEntityTest {

    @Test
    fun `test default values are set correctly`() {
        val entity = PendingOperationEntity()

        assertEquals(0L, entity.localId)
        assertNull(entity.entityType)
        assertNull(entity.operationType)
        assertEquals(0L, entity.entityId)
        assertEquals(0L, entity.localEntityId)
        assertEquals("", entity.payload)
        assertEquals(0L, entity.createdAt)
        assertEquals(0, entity.retryCount)
        assertNull(entity.lastError)
        assertEquals(Status.PENDING, entity.status)
    }

    @Test
    fun `test entity with all fields specified`() {
        val entity = PendingOperationEntity(
            localId = 1L,
            entityType = EntityType.TRANSACTION,
            operationType = OperationType.CREATE,
            entityId = 0L,
            localEntityId = -1L,
            payload = """{"type":"EXPENSE","amount":100.50}""",
            createdAt = 1735689600000L,
            retryCount = 2,
            lastError = "Network timeout",
            status = Status.FAILED
        )

        assertEquals(1L, entity.localId)
        assertEquals(EntityType.TRANSACTION, entity.entityType)
        assertEquals(OperationType.CREATE, entity.operationType)
        assertEquals(0L, entity.entityId)
        assertEquals(-1L, entity.localEntityId)
        assertEquals("""{"type":"EXPENSE","amount":100.50}""", entity.payload)
        assertEquals(1735689600000L, entity.createdAt)
        assertEquals(2, entity.retryCount)
        assertEquals("Network timeout", entity.lastError)
        assertEquals(Status.FAILED, entity.status)
    }

    @Test
    fun `test CREATE operation entity`() {
        val entity = createPendingOperation(
            operationType = OperationType.CREATE,
            entityId = 0L,
            localEntityId = -1L
        )

        assertEquals(OperationType.CREATE, entity.operationType)
        assertEquals(0L, entity.entityId)
        assertEquals(-1L, entity.localEntityId)
    }

    @Test
    fun `test UPDATE operation entity`() {
        val entity = createPendingOperation(
            operationType = OperationType.UPDATE,
            entityId = 123L,
            localEntityId = 123L
        )

        assertEquals(OperationType.UPDATE, entity.operationType)
        assertEquals(123L, entity.entityId)
    }

    @Test
    fun `test DELETE operation entity`() {
        val entity = createPendingOperation(
            operationType = OperationType.DELETE,
            entityId = 456L
        )

        assertEquals(OperationType.DELETE, entity.operationType)
        assertEquals(456L, entity.entityId)
    }

    @Test
    fun `test status transitions`() {
        val entity = createPendingOperation(status = Status.PENDING)

        assertEquals(Status.PENDING, entity.status)

        entity.status = Status.SYNCING
        assertEquals(Status.SYNCING, entity.status)

        entity.status = Status.COMPLETED
        assertEquals(Status.COMPLETED, entity.status)
    }

    @Test
    fun `test retry count increment`() {
        val entity = createPendingOperation(retryCount = 0)

        assertEquals(0, entity.retryCount)

        entity.retryCount = 1
        assertEquals(1, entity.retryCount)

        entity.retryCount = 2
        assertEquals(2, entity.retryCount)
    }

    @Test
    fun `test error tracking`() {
        val entity = createPendingOperation(lastError = null, status = Status.PENDING)

        assertNull(entity.lastError)
        assertEquals(Status.PENDING, entity.status)

        entity.lastError = "Connection refused"
        entity.status = Status.FAILED

        assertEquals("Connection refused", entity.lastError)
        assertEquals(Status.FAILED, entity.status)
    }

    @Test
    fun `test payload stores JSON correctly`() {
        val jsonPayload = """
            {
                "type": "EXPENSE",
                "totalAmount": 250.75,
                "categoryId": 5,
                "description": "Groceries",
                "fromAccountId": 1
            }
        """.trimIndent()

        val entity = createPendingOperation(payload = jsonPayload)

        assertEquals(jsonPayload, entity.payload)
    }

    @Test
    fun `test entity for TRANSACTION type`() {
        val entity = createPendingOperation(entityType = EntityType.TRANSACTION)

        assertEquals(EntityType.TRANSACTION, entity.entityType)
    }

    @Test
    fun `test entity for ACCOUNT type`() {
        val entity = createPendingOperation(entityType = EntityType.ACCOUNT)

        assertEquals(EntityType.ACCOUNT, entity.entityType)
    }

    @Test
    fun `test entity for CATEGORY type`() {
        val entity = createPendingOperation(entityType = EntityType.CATEGORY)

        assertEquals(EntityType.CATEGORY, entity.entityType)
    }

    @Test
    fun `test data class equality`() {
        val entity1 = PendingOperationEntity(
            localId = 1L,
            entityType = EntityType.TRANSACTION,
            operationType = OperationType.CREATE,
            entityId = 0L,
            localEntityId = -1L,
            payload = "{}",
            createdAt = 1000L,
            retryCount = 0,
            lastError = null,
            status = Status.PENDING
        )

        val entity2 = PendingOperationEntity(
            localId = 1L,
            entityType = EntityType.TRANSACTION,
            operationType = OperationType.CREATE,
            entityId = 0L,
            localEntityId = -1L,
            payload = "{}",
            createdAt = 1000L,
            retryCount = 0,
            lastError = null,
            status = Status.PENDING
        )

        assertEquals(entity1, entity2)
    }

    @Test
    fun `test copy with modified status`() {
        val original = createPendingOperation(status = Status.PENDING)

        val copy = original.copy(status = Status.COMPLETED)

        assertEquals(Status.PENDING, original.status)
        assertEquals(Status.COMPLETED, copy.status)
    }

    @Test
    fun `test copy with modified retry count and error`() {
        val original = createPendingOperation(retryCount = 0, lastError = null)

        val copy = original.copy(retryCount = 3, lastError = "Max retries exceeded")

        assertEquals(0, original.retryCount)
        assertNull(original.lastError)
        assertEquals(3, copy.retryCount)
        assertEquals("Max retries exceeded", copy.lastError)
    }

    @Test
    fun `test local entity ID is negative for offline creates`() {
        val entity = createPendingOperation(
            operationType = OperationType.CREATE,
            localEntityId = -1L
        )

        assertEquals(-1L, entity.localEntityId)
        assert(entity.localEntityId < 0) { "Local entity ID should be negative" }
    }

    @Test
    fun `test createdAt timestamp for FIFO ordering`() {
        val entity1 = createPendingOperation(createdAt = 1000L)
        val entity2 = createPendingOperation(createdAt = 2000L)
        val entity3 = createPendingOperation(createdAt = 3000L)

        assert(entity1.createdAt < entity2.createdAt)
        assert(entity2.createdAt < entity3.createdAt)
    }

    private fun createPendingOperation(
        localId: Long = 0L,
        entityType: EntityType = EntityType.TRANSACTION,
        operationType: OperationType = OperationType.CREATE,
        entityId: Long = 0L,
        localEntityId: Long = -1L,
        payload: String = "{}",
        createdAt: Long = System.currentTimeMillis(),
        retryCount: Int = 0,
        lastError: String? = null,
        status: Status = Status.PENDING
    ) = PendingOperationEntity(
        localId = localId,
        entityType = entityType,
        operationType = operationType,
        entityId = entityId,
        localEntityId = localEntityId,
        payload = payload,
        createdAt = createdAt,
        retryCount = retryCount,
        lastError = lastError,
        status = status
    )
}

