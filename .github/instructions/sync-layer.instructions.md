---
applyTo: "app/src/main/java/**/sync/**"
---

# Sync Layer Instructions

## SyncManager Responsibilities

- Singleton orchestrator. Observes `NetworkMonitor.isOnline` and calls `processQueue()` on
  reconnect.
- Exposes `syncState: StateFlow<SyncState>` and `pendingCount: StateFlow<Int>`.
- `MAX_RETRIES = 3`. After max retries, op is marked `Status.FAILED` — not removed.
- `retryFailed()` resets failed ops to `Status.PENDING` and re-runs `processQueue()`.
- `isSyncing: Boolean` guard prevents concurrent queue processing.

## EntitySyncHandler Contract

Each entity type implements `EntitySyncHandler`:

```kotlin
interface EntitySyncHandler {
    suspend fun processCreate(operation: PendingOperationEntity)
    suspend fun processUpdate(operation: PendingOperationEntity)
    suspend fun processDelete(operation: PendingOperationEntity)
}
```

Registered via Hilt `@IntoMap` with `@EntityTypeKey(EntityType.TRANSACTION)`.

## Error Handling

- `SyncException(message, isRetryable, cause?)` — throw for any server or mapping error.
- `isRetryable = false` for permanent failures (missing handler, null op type, 4xx that won't
  change).
- `isRetryable = true` for transient failures (network, 5xx).
- Handler must not swallow exceptions — let `SyncManager.processOperation` catch them.

## PendingOperationEntity Lifecycle

```
PENDING → (processOperation succeeds) → COMPLETED → (removeCompleted) → deleted
PENDING → (error, retryable, retries < MAX) → PENDING (retryCount++)
PENDING → (non-retryable OR retries == MAX) → FAILED
FAILED  → (retryFailed()) → PENDING (retryCount = 0)
```

## NetworkMonitor

- `isOnline: StateFlow<Boolean>` — reactive network state.
- Call `initialize()` in `SyncManager.init` — do not call it from outside.
- Check `networkMonitor.isOnline.value` before each operation in the queue loop to abort early on
  connection loss.

## IntegrityChecker / IntegrityResolverService

- `IntegrityResolverService.checkAndResolveOnLaunch()` runs at app start (called from
  `FinsibleApp.onCreate()`).
- Detects orphaned pending ops or local entities with no server ID after cold boot.
- Do not call from ViewModel or repository — app-level concern only.