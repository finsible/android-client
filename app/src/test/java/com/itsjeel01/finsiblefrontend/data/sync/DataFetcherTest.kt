package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for DataFetcher with snapshot-based integrity verification. */
class DataFetcherTest {

    private lateinit var dataFetcher: DataFetcher

    @Before
    fun setup() {
        dataFetcher = DataFetcher()
    }

    @Test
    fun `ensureDataFetched skips fetch when integrity passes`() = runTest {
        // Given: Integrity check returns true
        val verifyIntegrity: suspend () -> Boolean = { true }
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        // When
        val result = dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)

        // Then: Fetcher not called, returns success
        assertTrue(result)
        coVerify(exactly = 0) { fetcher() }
    }

    @Test
    fun `ensureDataFetched triggers fetch when integrity fails`() = runTest {
        // Given: Integrity check returns false
        val verifyIntegrity: suspend () -> Boolean = { false }
        val fetcher: suspend () -> BaseResponse<*> = mockk {
            coEvery { this@mockk.invoke() } returns BaseResponse(
                success = true,
                message = "Success",
                data = emptyList<Any>()
            )
        }

        // When
        val result = dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)

        // Then: Fetcher called, returns success
        assertTrue(result)
        coVerify(exactly = 1) { fetcher() }
    }

    @Test
    fun `ensureDataFetched returns false when fetch fails`() = runTest {
        // Given: Integrity fails, fetch returns error
        val verifyIntegrity: suspend () -> Boolean = { false }
        val fetcher: suspend () -> BaseResponse<*> = mockk {
            coEvery { this@mockk.invoke() } returns BaseResponse(
                success = false,
                message = "Fetch failed",
                data = null
            )
        }

        // When
        val result = dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)

        // Then: Returns false
        assertFalse(result)
        coVerify(exactly = 1) { fetcher() }
    }

    @Test
    fun `ensureDataFetched returns false when fetch throws exception`() = runTest {
        // Given: Integrity fails, fetch throws
        val verifyIntegrity: suspend () -> Boolean = { false }
        val fetcher: suspend () -> BaseResponse<*> = mockk {
            coEvery { this@mockk.invoke() } throws Exception("Network error")
        }

        // When
        val result = dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)

        // Then: Returns false
        assertFalse(result)
        coVerify(exactly = 1) { fetcher() }
    }

    @Test
    fun `ensureDataFetched handles integrity check exception`() = runTest {
        // Given: Integrity check throws
        val verifyIntegrity: suspend () -> Boolean = { throw Exception("Snapshot API error") }
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        // When
        val result = dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)

        // Then: Exception caught, returns false
        assertFalse(result)
        coVerify(exactly = 0) { fetcher() }
    }

    @Test
    fun `refreshData triggers fetch regardless of state`() = runTest {
        // Given: Fetcher returns success
        val fetcher: suspend () -> BaseResponse<*> = mockk {
            coEvery { this@mockk.invoke() } returns BaseResponse(
                success = true,
                message = "Success",
                data = emptyList<Any>()
            )
        }

        // When
        val result = dataFetcher.refreshData(fetcher)

        // Then: Fetcher called, returns success
        assertTrue(result)
        coVerify(exactly = 1) { fetcher() }
    }

    @Test
    fun `refreshData returns false when fetch fails`() = runTest {
        // Given: Fetcher returns error
        val fetcher: suspend () -> BaseResponse<*> = mockk {
            coEvery { this@mockk.invoke() } returns BaseResponse(
                success = false,
                message = "Refresh failed",
                data = null
            )
        }

        // When
        val result = dataFetcher.refreshData(fetcher)

        // Then: Returns false
        assertFalse(result)
        coVerify(exactly = 1) { fetcher() }
    }

    @Test
    fun `refreshData returns false when fetch throws exception`() = runTest {
        // Given: Fetcher throws
        val fetcher: suspend () -> BaseResponse<*> = mockk {
            coEvery { this@mockk.invoke() } throws Exception("Network error")
        }

        // When
        val result = dataFetcher.refreshData(fetcher)

        // Then: Returns false
        assertFalse(result)
        coVerify(exactly = 1) { fetcher() }
    }

    @Test
    fun `ensureDataFetched with successful integrity and fetch flow`() = runTest {
        // Given: Integrity passes initially
        var integrityCallCount = 0
        val verifyIntegrity: suspend () -> Boolean = {
            integrityCallCount++
            true
        }
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        // When: Called multiple times
        val result1 = dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)
        val result2 = dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)

        // Then: Both succeed, fetcher never called
        assertTrue(result1)
        assertTrue(result2)
        assertEquals(2, integrityCallCount)
        coVerify(exactly = 0) { fetcher() }
    }

    @Test
    fun `ensureDataFetched with failing integrity triggers fetch once per call`() = runTest {
        // Given: Integrity always fails
        val verifyIntegrity: suspend () -> Boolean = { false }
        var fetchCallCount = 0
        val fetcher: suspend () -> BaseResponse<*> = mockk {
            coEvery { this@mockk.invoke() } answers {
                fetchCallCount++
                BaseResponse(success = true, message = "OK", data = emptyList<Any>())
            }
        }

        // When: Called twice
        dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)
        dataFetcher.ensureDataFetched(verifyIntegrity, fetcher)

        // Then: Fetch called twice (once per call)
        assertEquals(2, fetchCallCount)
    }

    @Test
    fun `refreshData always fetches even if data exists`() = runTest {
        // Given: Multiple refresh calls
        var fetchCallCount = 0
        val fetcher: suspend () -> BaseResponse<*> = mockk {
            coEvery { this@mockk.invoke() } answers {
                fetchCallCount++
                BaseResponse(success = true, message = "OK", data = emptyList<Any>())
            }
        }

        // When: Called three times
        dataFetcher.refreshData(fetcher)
        dataFetcher.refreshData(fetcher)
        dataFetcher.refreshData(fetcher)

        // Then: Fetch called three times
        assertEquals(3, fetchCallCount)
    }
}

