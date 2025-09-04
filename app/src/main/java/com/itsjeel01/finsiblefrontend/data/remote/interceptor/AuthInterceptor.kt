package com.itsjeel01.finsiblefrontend.data.remote.interceptor

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

/** Interceptor that adds JWT Authorization header to requests except for auth endpoints. */
class AuthInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = preferenceManager.getJwt()

        if (request.url().encodedPath().contains("auth")) {
            return chain.proceed(request)
        }

        val authorizedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authorizedRequest)
    }
}
