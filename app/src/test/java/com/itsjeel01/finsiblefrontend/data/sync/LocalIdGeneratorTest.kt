package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/** Unit tests for LocalIdGenerator negative ID generation and persistence. */
class LocalIdGeneratorTest {

    private lateinit var mockPreferenceManager: PreferenceManager
    private lateinit var localIdGenerator: LocalIdGenerator

    @Before
    fun setUp() {
        mockPreferenceManager = mockk(relaxed = true)
    }

    @Test
    fun `test nextLocalId generates negative IDs`() {
        every { mockPreferenceManager.getLocalIdCounter() } returns 0L
        localIdGenerator = LocalIdGenerator(mockPreferenceManager)

        val id1 = localIdGenerator.nextLocalId()
        val id2 = localIdGenerator.nextLocalId()
        val id3 = localIdGenerator.nextLocalId()

        assertTrue("First ID should be negative", id1 < 0)
        assertTrue("Second ID should be negative", id2 < 0)
        assertTrue("Third ID should be negative", id3 < 0)
        assertEquals("First ID should be -1", -1L, id1)
        assertEquals("Second ID should be -2", -2L, id2)
        assertEquals("Third ID should be -3", -3L, id3)
    }

    @Test
    fun `test nextLocalId generates unique sequential IDs`() {
        every { mockPreferenceManager.getLocalIdCounter() } returns 0L
        localIdGenerator = LocalIdGenerator(mockPreferenceManager)

        val ids = mutableSetOf<Long>()
        repeat(1000) {
            val id = localIdGenerator.nextLocalId()
            assertTrue("ID should be unique", ids.add(id))
            assertTrue("ID should be negative", id < 0)
        }

        assertEquals("Should generate 1000 unique IDs", 1000, ids.size)
    }

    @Test
    fun `test nextLocalId persists each ID to preferences`() {
        every { mockPreferenceManager.getLocalIdCounter() } returns 0L
        localIdGenerator = LocalIdGenerator(mockPreferenceManager)

        localIdGenerator.nextLocalId()
        verify { mockPreferenceManager.saveLocalIdCounter(-1L) }

        localIdGenerator.nextLocalId()
        verify { mockPreferenceManager.saveLocalIdCounter(-2L) }

        localIdGenerator.nextLocalId()
        verify { mockPreferenceManager.saveLocalIdCounter(-3L) }
    }

    @Test
    fun `test initialization restores counter from preferences`() {
        every { mockPreferenceManager.getLocalIdCounter() } returns -100L
        localIdGenerator = LocalIdGenerator(mockPreferenceManager)

        val nextId = localIdGenerator.nextLocalId()

        assertEquals("Should continue from persisted counter", -101L, nextId)
        verify { mockPreferenceManager.getLocalIdCounter() }
    }

    @Test
    fun `test isLocalId correctly identifies negative IDs`() {
        every { mockPreferenceManager.getLocalIdCounter() } returns 0L
        localIdGenerator = LocalIdGenerator(mockPreferenceManager)

        assertTrue("Should identify -1 as local ID", localIdGenerator.isLocalId(-1L))
        assertTrue("Should identify -999 as local ID", localIdGenerator.isLocalId(-999L))
        assertTrue("Should identify -1000000 as local ID", localIdGenerator.isLocalId(-1000000L))
    }

    @Test
    fun `test isLocalId correctly identifies positive IDs as non-local`() {
        every { mockPreferenceManager.getLocalIdCounter() } returns 0L
        localIdGenerator = LocalIdGenerator(mockPreferenceManager)

        assertFalse("Should identify 1 as server ID", localIdGenerator.isLocalId(1L))
        assertFalse("Should identify 999 as server ID", localIdGenerator.isLocalId(999L))
        assertFalse("Should identify 1000000 as server ID", localIdGenerator.isLocalId(1000000L))
    }

    @Test
    fun `test isLocalId handles zero as non-local ID`() {
        every { mockPreferenceManager.getLocalIdCounter() } returns 0L
        localIdGenerator = LocalIdGenerator(mockPreferenceManager)

        assertFalse("Zero should not be considered local ID", localIdGenerator.isLocalId(0L))
    }

    @Test
    fun `test concurrent ID generation produces unique IDs`() {
        every { mockPreferenceManager.getLocalIdCounter() } returns 0L
        localIdGenerator = LocalIdGenerator(mockPreferenceManager)

        val ids = mutableSetOf<Long>()
        val threads = (1 .. 10).map {
            Thread {
                repeat(100) {
                    synchronized(ids) {
                        ids.add(localIdGenerator.nextLocalId())
                    }
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        assertEquals("Should generate 1000 unique IDs across threads", 1000, ids.size)
        assertTrue("All IDs should be negative", ids.all { it < 0 })
    }

    @Test
    fun `test no ID collisions after simulated app restart`() {
        // First session
        every { mockPreferenceManager.getLocalIdCounter() } returns 0L
        val generator1 = LocalIdGenerator(mockPreferenceManager)

        val id1 = generator1.nextLocalId()
        val id2 = generator1.nextLocalId()

        // Simulate app restart - restore from preferences
        every { mockPreferenceManager.getLocalIdCounter() } returns -2L
        val generator2 = LocalIdGenerator(mockPreferenceManager)

        val id3 = generator2.nextLocalId()

        assertEquals("First ID should be -1", -1L, id1)
        assertEquals("Second ID should be -2", -2L, id2)
        assertEquals("Third ID after restart should be -3", -3L, id3)
    }
}

