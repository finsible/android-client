package com.itsjeel01.finsiblefrontend.data.remote.interceptor

import android.content.Context
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TestPreferenceManager
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject

/** Intercepts network requests and returns mock responses when enabled. */
class MockInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val debugPrefs: TestPreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        // Skip mocking if disabled
        if (!debugPrefs.isMockApiEnabled()) {
            return chain.proceed(request)
        }

        // Check each endpoint and return mock response if enabled
        val mockResponse = run {
            /** Data class representing a mock endpoint configuration. */
            data class MockRule(
                val matches: (String) -> Boolean,
                val isEnabled: () -> Boolean,
                val resId: Int
            )

            val rules = listOf(
                MockRule(
                    matches = { it.contains("/auth/googleSignIn") },
                    isEnabled = { debugPrefs.isMockAuthEnabled() },
                    resId = R.raw.mock_auth
                ),
                MockRule(
                    matches = { it.contains("/categories") && it.contains("type=INCOME") },
                    isEnabled = { debugPrefs.isMockIncomeCategoriesEnabled() },
                    resId = R.raw.mock_income_categories
                ),
                MockRule(
                    matches = { it.contains("/categories") && it.contains("type=EXPENSE") },
                    isEnabled = { debugPrefs.isMockExpenseCategoriesEnabled() },
                    resId = R.raw.mock_expense_categories
                ),
                MockRule(
                    matches = { it.contains("/categories") && it.contains("type=TRANSFER") },
                    isEnabled = { debugPrefs.isMockTransferCategoriesEnabled() },
                    resId = R.raw.mock_transfer_categories
                ),
                MockRule(
                    matches = { it.contains("/account-groups/all") },
                    isEnabled = { debugPrefs.isMockAccountGroupsEnabled() },
                    resId = R.raw.mock_account_groups
                ),
                MockRule(
                    matches = { it.contains("/accounts/all") },
                    isEnabled = { debugPrefs.isMockAccountsFreshEnabled() },
                    resId = R.raw.mock_accounts_fresh
                ),
                MockRule(
                    matches = { it.contains("/accounts/all") },
                    isEnabled = { debugPrefs.isMockAccountsEnabled() && !debugPrefs.isMockAccountsFreshEnabled() },
                    resId = R.raw.mock_accounts
                )
            )

            rules.firstOrNull { it.matches(url) && it.isEnabled() }?.let { loadMockResponse(it.resId) }
        }

        return if (mockResponse != null) {
            Logger.Network.d("Returning mock response for: $url")
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val responseBody = ResponseBody.create(mediaType, mockResponse)
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK (Mock)")
                .body(responseBody)
                .build()
        } else {
            // Proceed with real network call
            chain.proceed(request)
        }
    }

    private fun loadMockResponse(resourceId: Int): String {
        return try {
            context.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Logger.Network.e("Error loading mock response", e)
            """{"success":false,"message":"Mock response loading error","data":null}"""
        }
    }
}

