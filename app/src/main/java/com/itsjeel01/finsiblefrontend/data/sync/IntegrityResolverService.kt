package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.repository.AccountGroupRepository
import com.itsjeel01.finsiblefrontend.data.repository.AccountRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
import com.itsjeel01.finsiblefrontend.data.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/** Service that runs integrity checks on app launch and resolves discrepancies in background. */
@Singleton
class IntegrityResolverService @Inject constructor(
    private val integrityChecker: IntegrityChecker,
    private val categoryRepository: CategoryRepository,
    private val accountGroupRepository: AccountGroupRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val preferenceManager: PreferenceManager,
    private val dataFetcher: DataFetcher,
    private val networkMonitor: NetworkMonitor
) {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun checkAndResolveOnLaunch() {
        if (!preferenceManager.isLoggedIn()) {
            Logger.Sync.d("User not authenticated, skipping integrity check")
            return
        }

        Logger.Sync.i("Starting background integrity check on app launch")

        applicationScope.launch {
            try {
                networkMonitor.initialize()

                if (!networkMonitor.isOnline.value) {
                    Logger.Sync.i("Network unavailable - skipping integrity check (offline-first mode)")
                    Logger.Sync.d("App will continue with local data. Integrity will be checked when network becomes available.")
                    return@launch
                }

                val report = integrityChecker.verifyAllIntegrity()

                if (!report.networkAvailable) {
                    Logger.Sync.i("Network became unavailable during integrity check")
                    Logger.Sync.d("App continues with local data. Integrity check will retry when online.")
                    return@launch
                }

                if (!report.hasDiscrepancy) {
                    Logger.Sync.i("✓ All entity counts match server snapshot - integrity verified")
                    return@launch
                }

                Logger.Sync.w("⚠ Integrity discrepancies detected - starting resolution")
                logDiscrepancyReport(report)

                if (!report.categoriesMatch) {
                    resolveCategories()
                }

                if (!report.accountGroupsMatch) {
                    resolveAccountGroups()
                }

                if (!report.accountsMatch) {
                    resolveAccounts()
                }

                if (!report.transactionsMatch) {
                    resolveTransactions()
                }

                // Verify integrity again after resolution
                val finalReport = integrityChecker.verifyAllIntegrity()

                if (!finalReport.networkAvailable) {
                    Logger.Sync.w("Network lost during resolution - app continues with partial sync")
                    return@launch
                }

                if (!finalReport.hasDiscrepancy) {
                    Logger.Sync.i("✓ All discrepancies resolved successfully")
                } else {
                    Logger.Sync.w("⚠ Some discrepancies remain after resolution attempt")
                    logDiscrepancyReport(finalReport)
                }
            } catch (e: Exception) {
                Logger.Sync.e("Error during integrity check and resolution", e)
                Logger.Sync.d("App continues with local data despite error")
            }
        }
    }

    private fun logDiscrepancyReport(report: IntegrityReport) {
        report.serverSnapshot?.let { snapshot ->
            Logger.Sync.w(
                "Server snapshot: Categories=${snapshot.categories}, " +
                        "AccountGroups=${snapshot.accountGroups}, " +
                        "Accounts=${snapshot.accounts}, " +
                        "Transactions=${snapshot.transactions}"
            )
        }
        Logger.Sync.w(
            "Discrepancy status: " +
                    "Categories=${if (report.categoriesMatch) "✓" else "✗"}, " +
                    "AccountGroups=${if (report.accountGroupsMatch) "✓" else "✗"}, " +
                    "Accounts=${if (report.accountsMatch) "✓" else "✗"}, " +
                    "Transactions=${if (report.transactionsMatch) "✓" else "✗"}"
        )
    }

    private suspend fun resolveCategories() {
        Logger.Sync.i("Resolving category discrepancies")
        try {
            val incomeSuccess = dataFetcher.refreshData {
                categoryRepository.getCategories(TransactionType.INCOME.name)
            }
            val expenseSuccess = dataFetcher.refreshData {
                categoryRepository.getCategories(TransactionType.EXPENSE.name)
            }
            val transferSuccess = dataFetcher.refreshData {
                categoryRepository.getCategories(TransactionType.TRANSFER.name)
            }

            if (incomeSuccess && expenseSuccess && transferSuccess) {
                Logger.Sync.i("Category discrepancies resolved")
            } else {
                Logger.Sync.w("Failed to resolve some category discrepancies")
            }
        } catch (e: Exception) {
            Logger.Sync.e("Error resolving categories: ${e.message}", e)
        }
    }

    private suspend fun resolveAccountGroups() {
        Logger.Sync.i("Resolving account group discrepancies")
        try {
            val success = dataFetcher.refreshData {
                accountGroupRepository.getAccountGroups()
            }
            if (success) {
                Logger.Sync.i("Account group discrepancies resolved")
            } else {
                Logger.Sync.w("Failed to resolve account group discrepancies")
            }
        } catch (e: Exception) {
            Logger.Sync.e("Error resolving account groups: ${e.message}", e)
        }
    }

    private suspend fun resolveAccounts() {
        Logger.Sync.i("Resolving account discrepancies")
        try {
            val success = dataFetcher.refreshData {
                accountRepository.getAccounts()
            }
            if (success) {
                Logger.Sync.i("Account discrepancies resolved")
            } else {
                Logger.Sync.w("Failed to resolve account discrepancies")
            }
        } catch (e: Exception) {
            Logger.Sync.e("Error resolving accounts: ${e.message}", e)
        }
    }

    private suspend fun resolveTransactions() {
        Logger.Sync.i("Resolving transaction discrepancies")
        try {
            val success = dataFetcher.refreshData {
                transactionRepository.fetchAllTransactions()
            }
            if (success) {
                Logger.Sync.i("Transaction discrepancies resolved")
            } else {
                Logger.Sync.w("Failed to resolve transaction discrepancies")
            }
        } catch (e: Exception) {
            Logger.Sync.e("Error resolving transactions: ${e.message}", e)
        }
    }
}
