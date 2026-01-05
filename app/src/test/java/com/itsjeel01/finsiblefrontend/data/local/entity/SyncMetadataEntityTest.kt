package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.EntityType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/** Unit tests for SyncMetadataEntity companion object helpers and data class. */
class SyncMetadataEntityTest {

    @Test
    fun `test buildSyncKey for TRANSACTION without period`() {
        val syncKey = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, null)

        assertEquals("TRANSACTION", syncKey)
    }

    @Test
    fun `test buildSyncKey for TRANSACTION with period`() {
        val syncKey = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, "2025-01")

        assertEquals("TRANSACTION:2025-01", syncKey)
    }

    @Test
    fun `test buildSyncKey for ACCOUNT without period`() {
        val syncKey = SyncMetadataEntity.buildSyncKey(EntityType.ACCOUNT, null)

        assertEquals("ACCOUNT", syncKey)
    }

    @Test
    fun `test buildSyncKey for CATEGORY without period`() {
        val syncKey = SyncMetadataEntity.buildSyncKey(EntityType.CATEGORY, null)

        assertEquals("CATEGORY", syncKey)
    }

    @Test
    fun `test buildSyncKey with different periods`() {
        val jan2025 = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, "2025-01")
        val feb2025 = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, "2025-02")
        val dec2024 = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, "2024-12")

        assertEquals("TRANSACTION:2025-01", jan2025)
        assertEquals("TRANSACTION:2025-02", feb2025)
        assertEquals("TRANSACTION:2024-12", dec2024)
        assertNotEquals(jan2025, feb2025)
        assertNotEquals(jan2025, dec2024)
    }

    @Test
    fun `test forScope creates entity with correct syncKey`() {
        val entity = SyncMetadataEntity.forScope(EntityType.TRANSACTION, "2025-01")

        assertEquals("TRANSACTION:2025-01", entity.syncKey)
        assertEquals(EntityType.TRANSACTION, entity.entityType)
    }

    @Test
    fun `test forScope creates entity without period`() {
        val entity = SyncMetadataEntity.forScope(EntityType.ACCOUNT, null)

        assertEquals("ACCOUNT", entity.syncKey)
        assertEquals(EntityType.ACCOUNT, entity.entityType)
    }

    @Test
    fun `test forScope sets default values`() {
        val entity = SyncMetadataEntity.forScope(EntityType.TRANSACTION, null)

        assertEquals(0L, entity.localId)
        assertEquals(0L, entity.lastSyncTime)
        assertEquals(0L, entity.lastFullSyncTime)
    }

    @Test
    fun `test default constructor values`() {
        val entity = SyncMetadataEntity()

        assertEquals(0L, entity.localId)
        assertEquals("", entity.syncKey)
        assertEquals(EntityType.TRANSACTION, entity.entityType)
        assertEquals(0L, entity.lastSyncTime)
        assertEquals(0L, entity.lastFullSyncTime)
    }

    @Test
    fun `test entity with all fields specified`() {
        val entity = SyncMetadataEntity(
            localId = 123L,
            syncKey = "TRANSACTION:2025-06",
            entityType = EntityType.TRANSACTION,
            lastSyncTime = 1735689600000L,
            lastFullSyncTime = 1735600000000L
        )

        assertEquals(123L, entity.localId)
        assertEquals("TRANSACTION:2025-06", entity.syncKey)
        assertEquals(EntityType.TRANSACTION, entity.entityType)
        assertEquals(1735689600000L, entity.lastSyncTime)
        assertEquals(1735600000000L, entity.lastFullSyncTime)
    }

    @Test
    fun `test syncKey uniqueness for different entity types`() {
        val transactionKey = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, null)
        val accountKey = SyncMetadataEntity.buildSyncKey(EntityType.ACCOUNT, null)
        val categoryKey = SyncMetadataEntity.buildSyncKey(EntityType.CATEGORY, null)

        assertNotEquals(transactionKey, accountKey)
        assertNotEquals(transactionKey, categoryKey)
        assertNotEquals(accountKey, categoryKey)
    }

    @Test
    fun `test syncKey uniqueness for same entity type different periods`() {
        val jan = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, "2025-01")
        val feb = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, "2025-02")
        val global = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, null)

        assertNotEquals(jan, feb)
        assertNotEquals(jan, global)
        assertNotEquals(feb, global)
    }

    @Test
    fun `test entity data class equality`() {
        val entity1 = SyncMetadataEntity(
            localId = 1L,
            syncKey = "TRANSACTION:2025-01",
            entityType = EntityType.TRANSACTION,
            lastSyncTime = 1000L,
            lastFullSyncTime = 500L
        )
        val entity2 = SyncMetadataEntity(
            localId = 1L,
            syncKey = "TRANSACTION:2025-01",
            entityType = EntityType.TRANSACTION,
            lastSyncTime = 1000L,
            lastFullSyncTime = 500L
        )

        assertEquals(entity1, entity2)
    }

    @Test
    fun `test entity copy with modified lastSyncTime`() {
        val original = SyncMetadataEntity.forScope(EntityType.TRANSACTION, "2025-01")
        original.lastSyncTime = 1000L

        val copy = original.copy(lastSyncTime = 2000L)

        assertEquals(1000L, original.lastSyncTime)
        assertEquals(2000L, copy.lastSyncTime)
        assertEquals(original.syncKey, copy.syncKey)
        assertEquals(original.entityType, copy.entityType)
    }

    @Test
    fun `test buildSyncKey handles special characters in period`() {
        // Edge case: what if period contains special chars (shouldn't happen but test defensively)
        val syncKey = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, "2025-Q1")

        assertEquals("TRANSACTION:2025-Q1", syncKey)
    }

    @Test
    fun `test buildSyncKey with empty period string`() {
        // Empty string should be treated as having a period (not null)
        val syncKey = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, "")

        assertEquals("TRANSACTION:", syncKey)
    }

    @Test
    fun `test forScope for all EntityType values`() {
        EntityType.entries.forEach { entityType ->
            val entity = SyncMetadataEntity.forScope(entityType, null)

            assertEquals(entityType.name, entity.syncKey)
            assertEquals(entityType, entity.entityType)
        }
    }

    @Test
    fun `test forScope with period for all EntityType values`() {
        val period = "2025-01"
        EntityType.entries.forEach { entityType ->
            val entity = SyncMetadataEntity.forScope(entityType, period)

            assertEquals("${entityType.name}:$period", entity.syncKey)
            assertEquals(entityType, entity.entityType)
        }
    }
}

