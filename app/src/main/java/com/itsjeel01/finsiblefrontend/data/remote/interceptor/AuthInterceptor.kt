package com.itsjeel01.finsiblefrontend.data.remote.interceptor

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.sync.ScopeManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
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

    @Volatile
    private var jwt: String? = null

    companion object {
        private const val MAX_JWT_RETRIES = 10
        private const val BASE_DELAY_MS = 1_000L
    }

    init {
        jwt = runBlocking {
            withTimeoutOrNull(1_500L) { preferenceManager.getJwt() }
        }

        scopeManager.scope.launch {
            preferenceManager.jwtFlow
                .retryWhen { cause, attempt ->
                    if (attempt >= MAX_JWT_RETRIES) false
                    else {
                        val delayMs = BASE_DELAY_MS * (1L shl attempt.toInt().coerceAtMost(5))
                        Logger.Network.e("JWT flow error (attempt ${attempt + 1}), retrying in ${delayMs}ms", cause)
                        delay(delayMs)
                        true
                    }
                }
                .catch { Logger.Network.e("JWT flow exhausted after $MAX_JWT_RETRIES retries, using cached value", it) }
                .collect { jwt = it }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (noAuthPrefixes.any { path.startsWith(it) }) {
            Logger.Network.d("Skipping token for auth endpoint: $path")
            return chain.proceed(request)
        }

        val token = jwt
        if (token.isNullOrEmpty()) {
            Logger.Network.w("No JWT token available for request: $path")
        } else {
            Logger.Network.d("Adding auth token to request: $path")
        }

        val authorizedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authorizedRequest)
    }
}
