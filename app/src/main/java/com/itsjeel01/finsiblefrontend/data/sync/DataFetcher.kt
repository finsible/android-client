package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.BaseEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncableEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.SyncableLocalRepository
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized utility for ensuring data is fetched from server if not present locally.
 * Uses SyncMetadataEntity to check if data has been synced before.
 *
 * Usage:
 * ```
 * dataFetcher.ensureDataFetched(
 *     localRepo = transactionLocalRepo,
 *     scopeKey = null,
 *     fetcher = { transactionRepository.getTransactions() }
 * )
 * ```
 */
@Singleton
class DataFetcher @Inject constructor() {

    /**
     * Ensures data is available for the given scope. Fetches from server if not synced before.
     *
     * @param localRepo The local repository to check sync metadata
     * @param scopeKey Optional scope key (e.g., "2026-01" for period, "EXPENSE" for category type)
     * @param fetcher Suspend function that fetches data from server
     * @return true if data was already synced or fetch succeeded, false if fetch failed
     */
    suspend fun <DTO, Entity> ensureDataFetched(
        localRepo: SyncableLocalRepository<DTO, Entity>,
        scopeKey: String? = null,
        fetcher: suspend () -> BaseResponse<*>
    ): Boolean where Entity : BaseEntity, Entity : SyncableEntity {
        // Check if data already synced for this scope
        if (localRepo.hasDataForScope(scopeKey)) {
            Logger.Sync.d("Data already synced for scope=$scopeKey")
            return true
        }

        Logger.Sync.i("No sync metadata for scope=$scopeKey, fetching from server")

        return try {
            val response = fetcher()

            if (response.success) {
                // Update sync metadata to mark as synced
                localRepo.updateLastSyncTime(scopeKey, System.currentTimeMillis())
                Logger.Sync.i("Fetch succeeded for scope=$scopeKey")
                true
            } else {
                Logger.Sync.w("Fetch failed for scope=$scopeKey: ${response.message}")
                false
            }
        } catch (e: Exception) {
            Logger.Sync.e("Fetch error for scope=$scopeKey: ${e.message}")
            false
        }
    }

    /**
     * Force refresh data even if already synced.
     *
     * @param localRepo The local repository to update sync metadata
     * @param scopeKey Optional scope key
     * @param fetcher Suspend function that fetches data from server
     * @return true if fetch succeeded, false otherwise
     */
    suspend fun <DTO, Entity> refreshData(
        localRepo: SyncableLocalRepository<DTO, Entity>,
        scopeKey: String? = null,
        fetcher: suspend () -> BaseResponse<*>
    ): Boolean where Entity : BaseEntity, Entity : SyncableEntity {
        Logger.Sync.i("Force refreshing scope=$scopeKey")

        return try {
            val response = fetcher()

            if (response.success) {
                localRepo.updateLastSyncTime(scopeKey, System.currentTimeMillis())
                Logger.Sync.i("Refresh succeeded for scope=$scopeKey")
                true
            } else {
                Logger.Sync.w("Refresh failed for scope=$scopeKey: ${response.message}")
                false
            }
        } catch (e: Exception) {
            Logger.Sync.e("Refresh error for scope=$scopeKey: ${e.message}")
            false
        }
    }
}

