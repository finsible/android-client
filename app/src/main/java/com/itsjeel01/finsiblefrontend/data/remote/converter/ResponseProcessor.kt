package com.itsjeel01.finsiblefrontend.data.remote.converter

import android.util.Log
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.sync.CacheManager

/** Response processor that handles caching logic. */
class ResponseProcessor(private val cacheManager: CacheManager) {

    fun process(response: Any?) {
        if (response is BaseResponse<*>) {
            try {
                cacheManager.cacheData(response)
            } catch (e: Exception) {
                Log.e(Strings.RESPONSE_PROCESSOR, "Error processing response: ${e.message}")
            }
        }
    }
}