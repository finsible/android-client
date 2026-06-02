package com.itsjeel01.finsiblefrontend.data.sync

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DataFetcherTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dataFetcher: DataFetcher

    @Before
    fun setUp() {
        dataFetcher = DataFetcher()
    }

    @Test
    fun `ensureDataFetched skips fetch when integrity is valid`() = runTest {
        val verifyIntegrity: suspend () -> Boolean = mockk()
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        coEvery { verifyIntegrity() } returns true

        val result = dataFetcher.ensureDataFetched(
            verifyIntegrity = verifyIntegrity,
            fetcher = fetcher
        )

        assertThat(result).isTrue()
        coVerify(exactly = 0) { fetcher() }
    }

    @Test
    fun `ensureDataFetched fetches when integrity is invalid`() = runTest {
        val verifyIntegrity: suspend () -> Boolean = mockk()
        val mockResponse: BaseResponse<*> = mockk()
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        coEvery { verifyIntegrity() } returns false
        coEvery { fetcher() } returns mockResponse
        every { mockResponse.success } returns true

        val result = dataFetcher.ensureDataFetched(
            verifyIntegrity = verifyIntegrity,
            fetcher = fetcher
        )

        assertThat(result).isTrue()
        coVerify(exactly = 1) { fetcher() }
    }

    @Test
    fun `ensureDataFetched returns false when fetch fails`() = runTest {
        val verifyIntegrity: suspend () -> Boolean = mockk()
        val mockResponse: BaseResponse<*> = mockk()
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        coEvery { verifyIntegrity() } returns false
        coEvery { fetcher() } returns mockResponse
        every { mockResponse.success } returns false
        every { mockResponse.message } returns "Server error"

        val result = dataFetcher.ensureDataFetched(
            verifyIntegrity = verifyIntegrity,
            fetcher = fetcher
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `ensureDataFetched returns false on exception`() = runTest {
        val verifyIntegrity: suspend () -> Boolean = mockk()
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        coEvery { verifyIntegrity() } throws RuntimeException("Network error")

        val result = dataFetcher.ensureDataFetched(
            verifyIntegrity = verifyIntegrity,
            fetcher = fetcher
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `refreshData returns true on success`() = runTest {
        val mockResponse: BaseResponse<*> = mockk()
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        coEvery { fetcher() } returns mockResponse
        every { mockResponse.success } returns true

        val result = dataFetcher.refreshData(fetcher = fetcher)

        assertThat(result).isTrue()
    }

    @Test
    fun `refreshData returns false on failure`() = runTest {
        val mockResponse: BaseResponse<*> = mockk()
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        coEvery { fetcher() } returns mockResponse
        every { mockResponse.success } returns false

        val result = dataFetcher.refreshData(fetcher = fetcher)

        assertThat(result).isFalse()
    }

    @Test
    fun `refreshData returns false on exception`() = runTest {
        val fetcher: suspend () -> BaseResponse<*> = mockk()

        coEvery { fetcher() } throws RuntimeException("Timeout")

        val result = dataFetcher.refreshData(fetcher = fetcher)

        assertThat(result).isFalse()
    }
}
