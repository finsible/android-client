package com.itsjeel01.finsiblefrontend.data.sync

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
}
