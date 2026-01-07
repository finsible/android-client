package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataFetcher @Inject constructor() {

    suspend fun ensureDataFetched(
        verifyIntegrity: suspend () -> Boolean,
        fetcher: suspend () -> BaseResponse<*>
    ): Boolean {
        return try {
            val integrityValid = verifyIntegrity()

            if (integrityValid) {
                Logger.Sync.d("Integrity verified, no fetch needed")
                return true
            }

            Logger.Sync.i("Integrity mismatch detected, fetching from server")

            val response = fetcher()

            if (response.success) {
                Logger.Sync.i("Fetch succeeded, integrity restored")
                true
            } else {
                Logger.Sync.w("Fetch failed: ${response.message}")
                false
            }
        } catch (e: Exception) {
            Logger.Sync.e("Error during integrity check or fetch: ${e.message}")
            false
        }
    }

    suspend fun refreshData(
        fetcher: suspend () -> BaseResponse<*>
    ): Boolean {
        Logger.Sync.i("Force refreshing data")

        return try {
            val response = fetcher()

            if (response.success) {
                Logger.Sync.i("Refresh succeeded")
                true
            } else {
                Logger.Sync.w("Refresh failed: ${response.message}")
                false
            }
        } catch (e: Exception) {
            Logger.Sync.e("Refresh error: ${e.message}")
            false
        }
    }
}

