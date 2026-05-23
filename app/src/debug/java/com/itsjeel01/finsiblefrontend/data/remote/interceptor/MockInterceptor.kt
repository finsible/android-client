package com.itsjeel01.finsiblefrontend.data.remote.interceptor

import android.content.Context
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TestPreferenceManager
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.ExchangeRatesData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

/** Intercepts network requests and returns mock responses when enabled. */
class MockInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val testPrefs: TestPreferenceManager
) : Interceptor {

    @Serializable
    private data class ExchangeRateSeed(
        val baseCurrencyCode: String,
        val targetCurrencyCode: String,
        val rate: Double
    )

    /** Data class representing a mock endpoint configuration. */
    private data class MockRule(
        val matches: (String) -> Boolean,
        val isEnabled: () -> Boolean,
        val resId: Int
    )

    /** Mock rules for each endpoint. */
    private val rules by lazy {
        listOf(
            MockRule(
                matches = { it.contains("/auth/googleSignIn") },
                isEnabled = { testPrefs.isMockAuthEnabled() },
                resId = R.raw.mock_auth
            ),
            MockRule(
                matches = { it.contains("/categories") && it.contains("type=INCOME") },
                isEnabled = { testPrefs.isMockIncomeCategoriesEnabled() },
                resId = R.raw.mock_income_categories
            ),
            MockRule(
                matches = { it.contains("/categories") && it.contains("type=EXPENSE") },
                isEnabled = { testPrefs.isMockExpenseCategoriesEnabled() },
                resId = R.raw.mock_expense_categories
            ),
            MockRule(
                matches = { it.contains("/categories") && it.contains("type=TRANSFER") },
                isEnabled = { testPrefs.isMockTransferCategoriesEnabled() },
                resId = R.raw.mock_transfer_categories
            ),
            MockRule(
                matches = { it.contains("/account-groups/all") },
                isEnabled = { testPrefs.isMockAccountGroupsEnabled() },
                resId = R.raw.mock_account_groups
            ),
            MockRule(
                matches = { it.contains("/accounts/all") },
                isEnabled = { testPrefs.isMockAccountsFreshEnabled() },
                resId = R.raw.mock_accounts_fresh
            ),
            MockRule(
                matches = { it.contains("/accounts/all") },
                isEnabled = { testPrefs.isMockAccountsEnabled() && !testPrefs.isMockAccountsFreshEnabled() },
                resId = R.raw.mock_accounts
            ),
            MockRule(
                matches = { it.contains("/sync/snapshot") },
                isEnabled = { testPrefs.isMockSnapshotEnabled() },
                resId = R.raw.mock_snapshot
            ),
            MockRule(
                matches = { it.contains("/transaction/all") },
                isEnabled = { testPrefs.isMockTransactionsEnabled() },
                resId = R.raw.mock_all_transactions
            )
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        // Skip mocking if disabled
        if (!testPrefs.isMockApiEnabled()) {
            return chain.proceed(request)
        }

        // Dynamic mock: exchange rates endpoint varies by base currency.
        if (url.contains("/currency/rates") && testPrefs.isMockExchangeRatesEnabled()) {
            val base = request.url.queryParameter("base")?.trim()?.uppercase().orEmpty().ifBlank { "INR" }
            val mockBody = buildMockExchangeRatesResponse(base)
            Logger.Network.d("Returning mock exchange rates for base=$base")
            return buildMockOkResponse(request = request, jsonBody = mockBody)
        }

        // Check each endpoint and return mock response if enabled
        val mockResponse = rules.firstOrNull { it.matches(url) && it.isEnabled() }
            ?.let { loadMockResponse(it.resId) }

        return if (mockResponse != null) {
            Logger.Network.d("Returning mock response for: $url")
            buildMockOkResponse(request = request, jsonBody = mockResponse)
        } else {
            // Proceed with real network call
            chain.proceed(request)
        }
    }

    private fun buildMockOkResponse(request: okhttp3.Request, jsonBody: String): Response {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val responseBody = jsonBody.toResponseBody(mediaType)
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK (Mock)")
            .body(responseBody)
            .build()
    }

    private fun buildMockExchangeRatesResponse(baseCurrencyCode: String): String {
        return try {
            val json = Json { ignoreUnknownKeys = true }
            val seeds = context.resources.openRawResource(R.raw.mock_exchange_rates)
                .bufferedReader()
                .use { json.decodeFromString<List<ExchangeRateSeed>>(it.readText()) }

            val rates = seeds
                .asSequence()
                .filter { it.baseCurrencyCode.equals(baseCurrencyCode, ignoreCase = true) }
                .associate { it.targetCurrencyCode.uppercase() to it.rate }

            val response = BaseResponse(
                success = true,
                message = "OK (Mock)",
                data = ExchangeRatesData(
                    base = baseCurrencyCode,
                    rates = rates
                ),
                cache = false
            )

            Json.encodeToString(response)
        } catch (e: Exception) {
            Logger.Network.e("Error building mock exchange rates response", e)
            // Keep schema-compatible even on error.
            Json.encodeToString(
                BaseResponse(
                    success = false,
                    message = "Mock exchange rates error: ${e.message}",
                    data = ExchangeRatesData(base = baseCurrencyCode, rates = emptyMap()),
                    cache = false
                )
            )
        }
    }

    private fun loadMockResponse(resourceId: Int): String {
        return try {
            context.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Logger.Network.e("Error loading mock response", e)
            """{"success":false,"message":"Mock response loading error: ${e.message}","resourceId":$resourceId,"data":null}"""
        }
    }
}

