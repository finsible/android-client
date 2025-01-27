package com.itsjeel01.finsiblefrontend.data.network

import com.itsjeel01.finsiblefrontend.utils.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

// Adds the Authorization header (JWT) to every request
class AuthInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = preferenceManager.getJwt()

        // Exemptions from adding the Authorization header
        if (originalRequest.url().encodedPath().contains("auth")) {
            return chain.proceed(originalRequest)
        }

        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(modifiedRequest)
    }
}