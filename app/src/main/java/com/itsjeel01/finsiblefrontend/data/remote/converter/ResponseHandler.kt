package com.itsjeel01.finsiblefrontend.data.remote.converter

import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.sync.CacheManager

/** Response processor that handles caching logic. */
class ResponseHandler(private val cacheManager: CacheManager) {

    fun process(response: Any?) {
        if (response is BaseResponse<*>) {
            try {
                if (response.cache && response.success && response.data != null) {
                    Logger.Cache.d("Caching response: ${response.data::class.simpleName}")
                    cacheManager.cacheData(response)
                } else {
                    Logger.Cache.d("Skipping cache: success=${response.success}, cache=${response.cache}, hasData=${response.data != null}")
                }
            } catch (e: Exception) {
                Logger.Cache.e("Error processing response for caching", e)
            }
        }
    }
}