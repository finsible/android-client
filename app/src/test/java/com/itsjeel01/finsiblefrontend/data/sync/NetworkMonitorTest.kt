package com.itsjeel01.finsiblefrontend.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for NetworkMonitor lifecycle and cleanup. */
@OptIn(ExperimentalCoroutinesApi::class)
class NetworkMonitorTest {

    private lateinit var mockContext: Context
    private lateinit var mockConnectivityManager: ConnectivityManager
    private lateinit var mockNetwork: Network
    private lateinit var mockNetworkCapabilities: NetworkCapabilities
    private lateinit var testScope: CoroutineScope
    private lateinit var networkMonitor: NetworkMonitor
    private val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockConnectivityManager = mockk(relaxed = true)
        mockNetwork = mockk(relaxed = true)
        mockNetworkCapabilities = mockk(relaxed = true)
        testScope = CoroutineScope(UnconfinedTestDispatcher())

        // Setup mocks
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        every {
            mockConnectivityManager.registerNetworkCallback(
                any<NetworkRequest>(),
                capture(callbackSlot)
            )
        } returns Unit
    }

    @Test
    fun `network callback is registered on initialization`() {
        networkMonitor = NetworkMonitor(mockContext, testScope)

        verify {
            mockConnectivityManager.registerNetworkCallback(
                any<NetworkRequest>(),
                any<ConnectivityManager.NetworkCallback>()
            )
        }
    }

    @Test
    fun `cleanup unregisters network callback`() {
        networkMonitor = NetworkMonitor(mockContext, testScope)

        // Call cleanup
        networkMonitor.cleanup()

        // Verify unregister was called with the same callback
        verify {
            mockConnectivityManager.unregisterNetworkCallback(callbackSlot.captured)
        }
    }

    @Test
    fun `cleanup handles already unregistered callback gracefully`() {
        networkMonitor = NetworkMonitor(mockContext, testScope)

        // Make unregister throw IllegalArgumentException
        every {
            mockConnectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>())
        } throws IllegalArgumentException("Already unregistered")

        // Should not throw exception
        networkMonitor.cleanup()

        // Verify unregister was attempted
        verify {
            mockConnectivityManager.unregisterNetworkCallback(callbackSlot.captured)
        }
    }

    @Test
    fun `multiple cleanup calls are safe`() {
        networkMonitor = NetworkMonitor(mockContext, testScope)

        // Call cleanup multiple times
        networkMonitor.cleanup()
        networkMonitor.cleanup()
        networkMonitor.cleanup()

        // Should only attempt to unregister once (subsequent calls find null callback)
        verify(exactly = 1) {
            mockConnectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>())
        }
    }

    @Test
    fun `initial connectivity state is checked on initialization`() {
        // Setup as offline
        every { mockConnectivityManager.activeNetwork } returns null

        networkMonitor = NetworkMonitor(mockContext, testScope)

        // Should be offline initially
        assertFalse("Should be offline when no active network", networkMonitor.isOnline.value)
    }

    @Test
    fun `initial connectivity state is online when network available`() {
        // Already setup as online in setUp()
        networkMonitor = NetworkMonitor(mockContext, testScope)

        // Should be online initially
        assertTrue("Should be online when active network has internet", networkMonitor.isOnline.value)
    }
}
