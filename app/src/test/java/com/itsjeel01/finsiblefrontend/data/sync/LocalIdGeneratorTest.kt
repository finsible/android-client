package com.itsjeel01.finsiblefrontend.data.sync

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocalIdGeneratorTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val preferenceManager: PreferenceManager = mockk()
    private lateinit var generator: LocalIdGenerator

    @Before
    fun setUp() {
        coEvery { preferenceManager.getLocalIdCounter() } returns 0L
        coEvery { preferenceManager.saveLocalIdCounter(any()) } returns Unit
        generator = LocalIdGenerator(preferenceManager)
    }

    @Test
    fun `nextLocalId returns negative IDs`() = runTest {
        val id = generator.nextLocalId()
        assertThat(id).isLessThan(0)
    }

    @Test
    fun `nextLocalId produces decreasing sequence`() = runTest {
        val id1 = generator.nextLocalId()
        val id2 = generator.nextLocalId()
        val id3 = generator.nextLocalId()

        assertThat(id1).isLessThan(0)
        assertThat(id2).isLessThan(id1)
        assertThat(id3).isLessThan(id2)
    }

    @Test
    fun `nextLocalId produces unique IDs across multiple calls`() = runTest {
        val ids = (1..100).map { generator.nextLocalId() }
        val uniqueIds = ids.toSet()
        assertThat(uniqueIds).hasSize(100)
    }

    @Test
    fun `isLocalId returns true for negative IDs`() {
        assertThat(generator.isLocalId(-1L)).isTrue()
        assertThat(generator.isLocalId(-100L)).isTrue()
    }

    @Test
    fun `isLocalId returns false for non-negative IDs`() {
        assertThat(generator.isLocalId(0L)).isFalse()
        assertThat(generator.isLocalId(1L)).isFalse()
        assertThat(generator.isLocalId(100L)).isFalse()
    }

    @Test
    fun `nextLocalId persists counter after each call`() = runTest {
        generator.nextLocalId()
        generator.nextLocalId()

        coVerify(exactly = 2) { preferenceManager.saveLocalIdCounter(any()) }
    }

    @Test
    fun `concurrent calls produce unique IDs with no collisions`() = runTest {
        val ids = coroutineScope {
            (1..50).map {
                async { generator.nextLocalId() }
            }.map { it.await() }
        }

        // All 50 IDs should be unique and negative
        assertThat(ids.toSet()).hasSize(50)
        assertThat(ids.all { it < 0 }).isTrue()

        // Values should be strictly decreasing
        val sorted = ids.sortedDescending()
        assertThat(ids).isEqualTo(sorted)
    }

    @Test
    fun `persists exact counter value after concurrent access`() = runTest {
        coroutineScope {
            (1..10).map {
                async { generator.nextLocalId() }
            }.forEach { it.await() }
        }

        // Should have saved 10 times (once per call)
        coVerify(exactly = 10) { preferenceManager.saveLocalIdCounter(any()) }
    }

    @Test
    fun `starts from persisted value`() = runTest {
        coEvery { preferenceManager.getLocalIdCounter() } returns -1000L

        val generator2 = LocalIdGenerator(preferenceManager)
        val id = generator2.nextLocalId()

        // Should start from -1000 and decrement
        assertThat(id).isEqualTo(-1001L)
    }
}
