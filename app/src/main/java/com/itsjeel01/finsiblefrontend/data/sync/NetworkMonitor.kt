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

/** Monitors network connectivity in real-time. */
@Singleton
class NetworkMonitor @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val coroutineScope: CoroutineScope
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOnline = MutableStateFlow(checkConnectivity())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    init {
        registerNetworkCallback()
    }

    private fun checkConnectivity(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun registerNetworkCallback() {
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
        }

        networkCallback = callback
        connectivityManager.registerNetworkCallback(request, callback)
    }

    /** Unregisters the network callback. Called during cleanup. */
    fun cleanup() {
        networkCallback?.let {
            try {
                connectivityManager.unregisterNetworkCallback(it)
                Logger.Network.d("Network callback unregistered")
            } catch (e: IllegalArgumentException) {
                Logger.Network.w("Network callback already unregistered", e)
            }
        }
        networkCallback = null
    }
}