package com.itsjeel01.finsiblefrontend.data.remote.interceptor

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import okhttp3.Interceptor
import okhttp3.Response

/** Interceptor that adds JWT Authorization header to requests except for auth endpoints. */
class AuthInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        val token = preferenceManager.getJwt()

        if (request.url.encodedPath.contains("auth")) {
            Logger.Network.d("Auth request, skipping token: $url")
            return chain.proceed(request)
        }

        if (token.isNullOrEmpty()) {
            Logger.Network.w("No JWT token available for request: $url")
        } else {
            Logger.Network.d("Adding auth token to request: $url")
        }

        val authorizedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authorizedRequest)
    }
}
