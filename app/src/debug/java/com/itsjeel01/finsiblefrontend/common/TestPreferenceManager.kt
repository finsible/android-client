package com.itsjeel01.finsiblefrontend.common

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Manages debug-specific preferences for test screen settings. */
@Singleton
class TestPreferenceManager @Inject constructor(@ApplicationContext context: Context) {
    companion object {
        private const val PREFS_FILE_NAME = "debug_prefs"
        private const val SKIP_DEBUG_SCREEN = "skip_debug_screen"
        private const val MOCK_API_ENABLED = "mock_api_enabled"
        private const val MOCK_AUTH = "mock_auth"
        private const val MOCK_INCOME_CATEGORIES = "mock_income_categories"
        private const val MOCK_EXPENSE_CATEGORIES = "mock_expense_categories"
        private const val MOCK_TRANSFER_CATEGORIES = "mock_transfer_categories"
        private const val MOCK_ACCOUNT_GROUPS = "mock_account_groups"
        private const val MOCK_ACCOUNTS = "mock_accounts"
        private const val MOCK_ACCOUNTS_FRESH = "mock_accounts_fresh"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)

    fun shouldSkipDebugScreen(): Boolean = sharedPreferences.getBoolean(SKIP_DEBUG_SCREEN, false)

    fun setSkipDebugScreen(skip: Boolean) {
        sharedPreferences.edit { putBoolean(SKIP_DEBUG_SCREEN, skip) }
    }

    fun isMockApiEnabled(): Boolean = sharedPreferences.getBoolean(MOCK_API_ENABLED, false)

    fun setMockApiEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(MOCK_API_ENABLED, enabled) }
    }

    fun isMockAuthEnabled(): Boolean = sharedPreferences.getBoolean(MOCK_AUTH, true)

    fun setMockAuthEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(MOCK_AUTH, enabled) }
    }

    fun isMockIncomeCategoriesEnabled(): Boolean =
        sharedPreferences.getBoolean(MOCK_INCOME_CATEGORIES, true)

    fun setMockIncomeCategoriesEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(MOCK_INCOME_CATEGORIES, enabled) }
    }

    fun isMockExpenseCategoriesEnabled(): Boolean =
        sharedPreferences.getBoolean(MOCK_EXPENSE_CATEGORIES, true)

    fun setMockExpenseCategoriesEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(MOCK_EXPENSE_CATEGORIES, enabled) }
    }

    fun isMockTransferCategoriesEnabled(): Boolean =
        sharedPreferences.getBoolean(MOCK_TRANSFER_CATEGORIES, true)

    fun setMockTransferCategoriesEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(MOCK_TRANSFER_CATEGORIES, enabled) }
    }

    fun isMockAccountGroupsEnabled(): Boolean =
        sharedPreferences.getBoolean(MOCK_ACCOUNT_GROUPS, true)

    fun setMockAccountGroupsEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(MOCK_ACCOUNT_GROUPS, enabled) }
    }

    fun isMockAccountsEnabled(): Boolean = sharedPreferences.getBoolean(MOCK_ACCOUNTS, true)

    fun setMockAccountsEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(MOCK_ACCOUNTS, enabled) }
    }

    fun isMockAccountsFreshEnabled(): Boolean =
        sharedPreferences.getBoolean(MOCK_ACCOUNTS_FRESH, false)

    fun setMockAccountsFreshEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(MOCK_ACCOUNTS_FRESH, enabled) }
    }

    /** Resets all debug preferences to defaults. */
    fun resetToDefaults() {
        sharedPreferences.edit { clear() }
        // Re-apply defaults
        setMockAuthEnabled(true)
        setMockIncomeCategoriesEnabled(true)
        setMockExpenseCategoriesEnabled(true)
        setMockTransferCategoriesEnabled(true)
        setMockAccountGroupsEnabled(true)
        setMockAccountsEnabled(true)
        setMockAccountsFreshEnabled(false)
    }
}

