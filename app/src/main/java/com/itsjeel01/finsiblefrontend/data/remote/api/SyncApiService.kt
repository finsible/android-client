package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.remote.model.EntitySnapshot
import retrofit2.http.GET

/** API service for sync and integrity verification endpoints. */
interface SyncApiService {

    @GET("sync/snapshot")
    suspend fun getSnapshot(): EntitySnapshot
}

