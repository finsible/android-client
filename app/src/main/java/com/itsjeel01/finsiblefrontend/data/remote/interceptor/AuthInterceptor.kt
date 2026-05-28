package com.itsjeel01.finsiblefrontend.data.remote.interceptor

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.sync.ScopeManager
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

/** Interceptor that adds JWT Authorization header to all requests except an explicit allowlist. */
class AuthInterceptor(
    private val preferenceManager: PreferenceManager,
    scopeManager: ScopeManager,
) : Interceptor {

    private val noAuthPrefixes = setOf(
        "/auth/googleSignIn",
    )

    init {
        scopeManager.scope.launch {
            preferenceManager.primeJwtCache()
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (noAuthPrefixes.any { path.startsWith(it) }) {
            Logger.Network.d("Skipping token for auth endpoint: $path")
            return chain.proceed(request)
        }

        val token = preferenceManager.getCachedJwt()
        if (token.isNullOrEmpty()) {
            Logger.Network.w("No JWT token available for request: $path")
            return chain.proceed(request)
        }

        Logger.Network.d("Adding auth token to request: $path")
        val authorizedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authorizedRequest)
    }
}
