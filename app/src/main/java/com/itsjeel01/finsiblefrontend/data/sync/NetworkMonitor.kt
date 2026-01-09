package com.itsjeel01.finsiblefrontend.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/** Monitors network connectivity in real-time */
@Singleton
class NetworkMonitor @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val coroutineScope: CoroutineScope
) {
    private val connectivityManager: ConnectivityManager? = try {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    } catch (e: Exception) {
        Logger.Network.e("Failed to get ConnectivityManager - network monitoring disabled", e)
        null
    }

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var isInitialized = false

    /** Lazy initialization - call this when network monitoring is actually needed. */
    @Synchronized
    fun initialize() {
        if (isInitialized) {
            Logger.Network.d("NetworkMonitor already initialized")
            return
        }

        if (connectivityManager == null) {
            Logger.Network.w("ConnectivityManager unavailable - app will operate in offline mode")
            _isOnline.value = false
            isInitialized = true
            return
        }

        try {
            // Check initial connectivity state
            _isOnline.value = checkConnectivity()
            registerNetworkCallback()
            isInitialized = true
            Logger.Network.i("NetworkMonitor initialized successfully (initial state: ${_isOnline.value})")
        } catch (e: SecurityException) {
            Logger.Network.w("Missing network permission - assuming offline mode", e)
            _isOnline.value = false
            isInitialized = true
        } catch (e: Exception) {
            Logger.Network.e("Failed to initialize network monitoring - assuming offline mode", e)
            _isOnline.value = false
            isInitialized = true
        }
    }

    private fun checkConnectivity(): Boolean {
        if (connectivityManager == null) return false

        return try {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } catch (e: Exception) {
            Logger.Network.w("Error checking connectivity", e)
            false
        }
    }

    private fun registerNetworkCallback() {
        if (connectivityManager == null) return

        try {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    coroutineScope.launch {
                        _isOnline.emit(true)
                    }
                    Logger.Network.d("Network status: Online")
                }

                override fun onLost(network: Network) {
                    coroutineScope.launch {
                        _isOnline.emit(false)
                    }
                    Logger.Network.d("Network status: Offline")
                }

                override fun onUnavailable() {
                    coroutineScope.launch {
                        _isOnline.emit(false)
                    }
                    Logger.Network.d("Network status: Unavailable")
                }
            }

            networkCallback = callback
            connectivityManager.registerNetworkCallback(request, callback)
            Logger.Network.d("Network callback registered successfully")
        } catch (e: Exception) {
            Logger.Network.w("Failed to register network callback - monitoring disabled", e)
        }
    }

    /** Unregisters the network callback. Called during cleanup. */
    @Synchronized
    fun cleanup() {
        if (!isInitialized) {
            Logger.Network.d("NetworkMonitor was never initialized - nothing to clean up")
            return
        }

        networkCallback?.let { callback ->
            connectivityManager?.let { manager ->
                try {
                    manager.unregisterNetworkCallback(callback)
                    Logger.Network.d("Network callback unregistered successfully")
                } catch (e: IllegalArgumentException) {
                    Logger.Network.w("Network callback was already unregistered", e)
                } catch (e: Exception) {
                    Logger.Network.w("Error unregistering network callback", e)
                }
            }
        }
        networkCallback = null
    }
}