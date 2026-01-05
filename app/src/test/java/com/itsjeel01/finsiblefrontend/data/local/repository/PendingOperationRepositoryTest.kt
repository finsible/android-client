package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.objectbox.Box
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/** Unit tests for PendingOperationRepository sync queue management. */
class PendingOperationRepositoryTest {

    private lateinit var mockBox: Box<PendingOperationEntity>
    private lateinit var repository: PendingOperationRepository

    @Before
    fun setUp() {
        mockBox = mockk(relaxed = true)
        repository = PendingOperationRepository(mockBox)
    }

    @Test
    fun `test add sets createdAt timestamp`() {
        val capturedEntity = slot<PendingOperationEntity>()
        every { mockBox.put(capture(capturedEntity)) } returns 1L

        val operation = PendingOperationEntity(
            entityType = EntityType.TRANSACTION,
            operationType = OperationType.CREATE,
            entityId = 0L,
            localEntityId = -1L,
            payload = "{}",
            status = Status.PENDING
        )

        val beforeTime = System.currentTimeMillis()
        repository.add(operation)
        val afterTime = System.currentTimeMillis()

        verify { mockBox.put(any<PendingOperationEntity>()) }
        assert(capturedEntity.captured.createdAt in beforeTime .. afterTime) {
            "createdAt should be set to current time"
        }
    }

    @Test
    fun `test add persists operation to box`() {
        val operation = createPendingOperation()

        repository.add(operation)

        verify { mockBox.put(any<PendingOperationEntity>()) }
    }

    @Test
    fun `test update persists changes to box`() {
        val operation = createPendingOperation(
            localId = 1L,
            retryCount = 2,
            lastError = "Network error"
        )

        repository.update(operation)

        verify { mockBox.put(operation) }
    }

    @Test
    fun `test add preserves operation type CREATE`() {
        val capturedEntity = slot<PendingOperationEntity>()
        every { mockBox.put(capture(capturedEntity)) } returns 1L

        val operation = createPendingOperation(operationType = OperationType.CREATE)
        repository.add(operation)

        assertEquals(OperationType.CREATE, capturedEntity.captured.operationType)
    }

    @Test
    fun `test add preserves operation type UPDATE`() {
        val capturedEntity = slot<PendingOperationEntity>()
        every { mockBox.put(capture(capturedEntity)) } returns 1L

        val operation = createPendingOperation(operationType = OperationType.UPDATE)
        repository.add(operation)

        assertEquals(OperationType.UPDATE, capturedEntity.captured.operationType)
    }

    @Test
    fun `test add preserves operation type DELETE`() {
        val capturedEntity = slot<PendingOperationEntity>()
        every { mockBox.put(capture(capturedEntity)) } returns 1L

        val operation = createPendingOperation(operationType = OperationType.DELETE)
        repository.add(operation)

        assertEquals(OperationType.DELETE, capturedEntity.captured.operationType)
    }

    @Test
    fun `test add preserves payload`() {
        val capturedEntity = slot<PendingOperationEntity>()
        every { mockBox.put(capture(capturedEntity)) } returns 1L

        val payload = """{"type":"EXPENSE","amount":100.50}"""
        val operation = createPendingOperation(payload = payload)
        repository.add(operation)

        assertEquals(payload, capturedEntity.captured.payload)
    }

    @Test
    fun `test add preserves entity type`() {
        val capturedEntity = slot<PendingOperationEntity>()
        every { mockBox.put(capture(capturedEntity)) } returns 1L

        val operation = createPendingOperation(entityType = EntityType.ACCOUNT)
        repository.add(operation)

        assertEquals(EntityType.ACCOUNT, capturedEntity.captured.entityType)
    }

    @Test
    fun `test add preserves local entity ID`() {
        val capturedEntity = slot<PendingOperationEntity>()
        every { mockBox.put(capture(capturedEntity)) } returns 1L

        val operation = createPendingOperation(localEntityId = -42L)
        repository.add(operation)

        assertEquals(-42L, capturedEntity.captured.localEntityId)
    }

    @Test
    fun `test pending operation entity is created with correct defaults`() {
        val operation = createPendingOperation()

        assertNotNull(operation)
        assertEquals(Status.PENDING, operation.status)
        assertEquals(0, operation.retryCount)
    }

    private fun createPendingOperation(
        localId: Long = 0L,
        entityType: EntityType = EntityType.TRANSACTION,
        operationType: OperationType = OperationType.CREATE,
        entityId: Long = 0L,
        localEntityId: Long = -1L,
        payload: String = "{}",
        createdAt: Long = 0L,
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
