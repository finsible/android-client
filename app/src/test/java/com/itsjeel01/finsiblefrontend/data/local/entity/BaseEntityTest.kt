package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for BaseEntity TTL persistence and staleness checks. */
class BaseEntityTest {

    private lateinit var testEntity: TestEntity

    @Before
    fun setUp() {
        testEntity = TestEntity()
    }

    @Test
    fun `test cachedTime updates when id is set`() {
        val beforeTime = System.currentTimeMillis()
        testEntity.id = 123L
        val afterTime = System.currentTimeMillis()

        assertTrue("cachedTime should be set", testEntity.cachedTime > 0)
        assertTrue("cachedTime should be recent", testEntity.cachedTime >= beforeTime)
        assertTrue("cachedTime should not exceed current time", testEntity.cachedTime <= afterTime)
    }

    @Test
    fun `test isStale returns true when cachedTime is zero`() {
        testEntity.cachedTime = 0L
        assertTrue("Entity with zero cachedTime should be stale", testEntity.isStale())
    }

    @Test
    fun `test isStale returns true when TTL expired`() {
        // Set cached time to 2 hours ago with 1 hour TTL
        testEntity.cachedTime = System.currentTimeMillis() - (2 * 60 * 60 * 1000)
        testEntity.cacheTtlMinutes = 60L

        assertTrue("Entity should be stale after TTL expiry", testEntity.isStale())
    }

    @Test
    fun `test isStale returns false when within TTL`() {
        // Set cached time to 30 minutes ago with 1 hour TTL
        testEntity.cachedTime = System.currentTimeMillis() - (30 * 60 * 1000)
        testEntity.cacheTtlMinutes = 60L

        assertFalse("Entity should not be stale within TTL", testEntity.isStale())
    }

    @Test
    fun `test isStale returns false when TTL is null`() {
        testEntity.cachedTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000) // 1 day ago
        testEntity.cacheTtlMinutes = null

        assertFalse("Entity with null TTL should never be stale", testEntity.isStale())
    }

    @Test
    fun `test updateCacheTime refreshes timestamp`() {
        val oldTime = System.currentTimeMillis() - 1000
        testEntity.cachedTime = oldTime

        Thread.sleep(10) // Small delay to ensure time difference
        testEntity.updateCacheTime()

        assertTrue(
            "cachedTime should be updated to current time",
            testEntity.cachedTime > oldTime
        )
    }

    @Test
    fun `test updateCacheTime updates TTL when provided`() {
        testEntity.cacheTtlMinutes = 30L
        testEntity.updateCacheTime(ttlMinutes = 120L)

        assertEquals("TTL should be updated", 120L, testEntity.cacheTtlMinutes)
    }

    @Test
    fun `test updateCacheTime preserves TTL when not provided`() {
        testEntity.cacheTtlMinutes = 45L
        testEntity.updateCacheTime()

        assertEquals("TTL should be preserved", 45L, testEntity.cacheTtlMinutes)
    }

    @Test
    fun `test default TTL is Duration_DEFAULT_TTL_MINUTES`() {
        val freshEntity = TestEntity()
        assertEquals(
            "Default TTL should match Duration.DEFAULT_TTL_MINUTES",
            Duration.DEFAULT_TTL_MINUTES,
            freshEntity.cacheTtlMinutes
        )
    }

    @Test
    fun `test cachedTime field is public and persists`() {
        testEntity.cachedTime = 123456789L
        assertEquals("cachedTime should be publicly accessible", 123456789L, testEntity.cachedTime)
    }

    @Test
    fun `test cacheTtlMinutes field is public and persists`() {
        testEntity.cacheTtlMinutes = 999L
        assertEquals("cacheTtlMinutes should be publicly accessible", 999L, testEntity.cacheTtlMinutes)
    }

    /** Test implementation of BaseEntity for unit testing. */
    private class TestEntity : BaseEntity()
}

