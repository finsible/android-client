package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.TestPreferenceManager
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** ViewModel for debug test screen operations. */
@HiltViewModel
class TestViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val testPrefs: TestPreferenceManager,
    private val boxStore: BoxStore,
    private val categoryBox: Box<CategoryEntity>,
    private val accountBox: Box<AccountEntity>,
    private val accountGroupBox: Box<AccountGroupEntity>
) : ViewModel() {

    private val _operationStatus = MutableStateFlow<OperationStatus>(OperationStatus.Idle)
    val operationStatus: StateFlow<OperationStatus> = _operationStatus

    private val _mockApiEnabled = MutableStateFlow(testPrefs.isMockApiEnabled())
    val mockApiEnabled: StateFlow<Boolean> = _mockApiEnabled

    private val _skipDebugScreen = MutableStateFlow(testPrefs.shouldSkipDebugScreen())
    val skipDebugScreen: StateFlow<Boolean> = _skipDebugScreen

    // Individual endpoint toggles
    private val _mockAuth = MutableStateFlow(testPrefs.isMockAuthEnabled())
    val mockAuth: StateFlow<Boolean> = _mockAuth

    private val _mockIncomeCategories = MutableStateFlow(testPrefs.isMockIncomeCategoriesEnabled())
    val mockIncomeCategories: StateFlow<Boolean> = _mockIncomeCategories

    private val _mockExpenseCategories = MutableStateFlow(testPrefs.isMockExpenseCategoriesEnabled())
    val mockExpenseCategories: StateFlow<Boolean> = _mockExpenseCategories

    private val _mockTransferCategories = MutableStateFlow(testPrefs.isMockTransferCategoriesEnabled())
    val mockTransferCategories: StateFlow<Boolean> = _mockTransferCategories

    private val _mockAccountGroups = MutableStateFlow(testPrefs.isMockAccountGroupsEnabled())
    val mockAccountGroups: StateFlow<Boolean> = _mockAccountGroups

    private val _mockAccounts = MutableStateFlow(testPrefs.isMockAccountsEnabled())
    val mockAccounts: StateFlow<Boolean> = _mockAccounts

    private val _mockAccountsFresh = MutableStateFlow(testPrefs.isMockAccountsFreshEnabled())
    val mockAccountsFresh: StateFlow<Boolean> = _mockAccountsFresh

    private val _mockSnapshot = MutableStateFlow(testPrefs.isMockSnapshotEnabled())
    val mockSnapshot: StateFlow<Boolean> = _mockSnapshot

    private val _mockTransactions = MutableStateFlow(testPrefs.isMockTransactionsEnabled())
    val mockTransactions: StateFlow<Boolean> = _mockTransactions

    /** Clears all app data including preferences and ObjectBox database. */
    fun clearAllAppData() {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading("Clearing all app data...")
                withContext(Dispatchers.IO) {
                    // Clear encrypted preferences
                    preferenceManager.clearAuthData()

                    // Clear entire ObjectBox database
                    boxStore.removeAllObjects()
                }
                _operationStatus.value = OperationStatus.Success("All app data cleared successfully")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Failed to clear app data: ${e.message}")
            }
        }
    }

    /** Clears only encrypted shared preferences. */
    fun clearPreferences() {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading("Clearing preferences...")
                withContext(Dispatchers.IO) {
                    preferenceManager.clearAuthData()
                }
                _operationStatus.value = OperationStatus.Success("Preferences cleared successfully")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Failed to clear preferences: ${e.message}")
            }
        }
    }

    /** Flushes entire ObjectBox database. */
    fun flushEntireDatabase() {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading("Flushing entire database...")
                withContext(Dispatchers.IO) {
                    boxStore.removeAllObjects()
                }
                _operationStatus.value = OperationStatus.Success("Entire database flushed successfully")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Failed to flush database: ${e.message}")
            }
        }
    }

    /** Flushes specific entity from ObjectBox. */
    fun flushEntity(entityName: String) {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading("Flushing $entityName...")
                withContext(Dispatchers.IO) {
                    when (entityName) {
                        "Categories" -> categoryBox.removeAll()
                        "Accounts" -> accountBox.removeAll()
                        "Account Groups" -> accountGroupBox.removeAll()
                        else -> throw IllegalArgumentException("Unknown entity: $entityName")
                    }
                }
                _operationStatus.value = OperationStatus.Success("$entityName flushed successfully")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Failed to flush $entityName: ${e.message}")
            }
        }
    }

    /** Toggles mock API globally. */
    fun toggleMockApi(enabled: Boolean) {
        testPrefs.setMockApiEnabled(enabled)
        _mockApiEnabled.value = enabled
    }

    /** Toggles skip debug screen preference. */
    fun toggleSkipDebugScreen(skip: Boolean) {
        testPrefs.setSkipDebugScreen(skip)
        _skipDebugScreen.value = skip
    }

    /** Toggles individual endpoint mocks. */
    fun toggleMockAuth(enabled: Boolean) {
        testPrefs.setMockAuthEnabled(enabled)
        _mockAuth.value = enabled
    }

    fun toggleMockIncomeCategories(enabled: Boolean) {
        testPrefs.setMockIncomeCategoriesEnabled(enabled)
        _mockIncomeCategories.value = enabled
    }

    fun toggleMockExpenseCategories(enabled: Boolean) {
        testPrefs.setMockExpenseCategoriesEnabled(enabled)
        _mockExpenseCategories.value = enabled
    }

    fun toggleMockTransferCategories(enabled: Boolean) {
        testPrefs.setMockTransferCategoriesEnabled(enabled)
        _mockTransferCategories.value = enabled
    }

    fun toggleMockAccountGroups(enabled: Boolean) {
        testPrefs.setMockAccountGroupsEnabled(enabled)
        _mockAccountGroups.value = enabled
    }

    fun toggleMockAccounts(enabled: Boolean) {
        testPrefs.setMockAccountsEnabled(enabled)
        _mockAccounts.value = enabled
    }

    fun toggleMockAccountsFresh(enabled: Boolean) {
        testPrefs.setMockAccountsFreshEnabled(enabled)
        _mockAccountsFresh.value = enabled
    }

    fun toggleMockSnapshot(enabled: Boolean) {
        testPrefs.setMockSnapshotEnabled(enabled)
        _mockSnapshot.value = enabled
    }

    fun toggleMockTransactions(enabled: Boolean) {
        testPrefs.setMockTransactionsEnabled(enabled)
        _mockTransactions.value = enabled
    }

    /** Resets all debug settings to defaults. */
    fun resetToDefaults() {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading("Resetting to defaults...")
                testPrefs.resetToDefaults()

                // Update all state flows
                _mockApiEnabled.value = testPrefs.isMockApiEnabled()
                _skipDebugScreen.value = testPrefs.shouldSkipDebugScreen()
                _mockAuth.value = testPrefs.isMockAuthEnabled()
                _mockIncomeCategories.value = testPrefs.isMockIncomeCategoriesEnabled()
                _mockExpenseCategories.value = testPrefs.isMockExpenseCategoriesEnabled()
                _mockTransferCategories.value = testPrefs.isMockTransferCategoriesEnabled()
                _mockAccountGroups.value = testPrefs.isMockAccountGroupsEnabled()
                _mockAccounts.value = testPrefs.isMockAccountsEnabled()
                _mockAccountsFresh.value = testPrefs.isMockAccountsFreshEnabled()
                _mockSnapshot.value = testPrefs.isMockSnapshotEnabled()
                _mockTransactions.value = testPrefs.isMockTransactionsEnabled()

                _operationStatus.value = OperationStatus.Success("Settings reset to defaults")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Failed to reset settings: ${e.message}")
            }
        }
    }

    /** Clears operation status. */
    fun clearStatus() {
        _operationStatus.value = OperationStatus.Idle
    }
}

sealed class OperationStatus {
    data object Idle : OperationStatus()
    data class Loading(val message: String) : OperationStatus()
    data class Success(val message: String) : OperationStatus()
    data class Error(val message: String) : OperationStatus()
}

