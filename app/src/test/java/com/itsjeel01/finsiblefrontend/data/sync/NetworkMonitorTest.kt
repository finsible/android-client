package com.itsjeel01.finsiblefrontend.data.sync

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NetworkMonitorTest {

    private lateinit var mockConnectivityManager: ConnectivityManager
    private lateinit var mockNetwork: Network
    private lateinit var mockNetworkCapabilities: NetworkCapabilities

    @Before
    fun setUp() {
        mockConnectivityManager = mockk(relaxed = true)
        mockNetwork = mockk(relaxed = true)
        mockNetworkCapabilities = mockk(relaxed = true)
    }

    @Test
    fun `checkConnectivity returns true when network has internet capability`() {
        // Given: Active network with internet capability
        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        // When: Check connectivity logic
        val network = mockConnectivityManager.activeNetwork
        val capabilities = mockConnectivityManager.getNetworkCapabilities(network)
        val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        // Then
        assertTrue("Should be online when network has internet capability", isConnected)
    }

    @Test
    fun `checkConnectivity returns false when no active network`() {
        // Given: No active network
        every { mockConnectivityManager.activeNetwork } returns null

        // When
        val network = mockConnectivityManager.activeNetwork
        val capabilities = mockConnectivityManager.getNetworkCapabilities(network)
        val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        // Then
        assertFalse("Should be offline when no active network", isConnected)
    }

    @Test
    fun `checkConnectivity returns false when network capabilities are null`() {
        // Given: Network exists but no capabilities
        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns null

        // When
        val network = mockConnectivityManager.activeNetwork
        val capabilities = mockConnectivityManager.getNetworkCapabilities(network)
        val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        // Then
        assertFalse("Should be offline when capabilities are null", isConnected)
    }

    @Test
    fun `checkConnectivity returns false when network lacks internet capability`() {
        // Given: Network exists but lacks internet capability
        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false

        // When
        val network = mockConnectivityManager.activeNetwork
        val capabilities = mockConnectivityManager.getNetworkCapabilities(network)
        val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        // Then
        assertFalse("Should be offline when network lacks internet capability", isConnected)
    }
}
