---
mode: agent
description: Write unit tests for a Finsible class
---

Write unit tests for the specified class. Match the patterns in `test/java/…`.

## Test Structure

- Package mirrors source: `com.itsjeel01.finsiblefrontend.data.local.repository` →
  `TransactionLocalRepositoryTest`.
- JUnit 4 (`@Test`, `@Before`, `@Rule`).
- Use `MockK` for mocking (`mockk()`, `every { }`, `verify { }`).
- Use `kotlinx-coroutines-test` for coroutine testing (`runTest`, `TestCoroutineScheduler`).

## Repository Tests

- Mock the `Box<Entity>` and its `QueryBuilder`.
- Test each query variant (date filter only, amount filter only, combined, no filters).
- Assert result count and `hasMore` flag for pagination.
- Test aggregate queries (`sum`, `count`) separately.

## ViewModel Tests

- Use `TestDispatcher` and `StandardTestDispatcher` for coroutine control.
- Set `Dispatchers.setMain(testDispatcher)` in `@Before`, reset in `@After`.
- Test each intent function: call it, `advanceUntilIdle()`, assert `uiState.value`.
- Test debounce: advance by `SEARCH_DEBOUNCE_MS - 1`, assert no query fired; advance past, assert
  query fired.
- Test guard conditions (`loadMoreJob` active, `hasMoreData = false`).

## SyncManager Tests

- Mock `PendingOperationRepository`, `NetworkMonitor`, `EntitySyncHandler` map.
- Test queue processing: verify handler called in order.
- Test retry logic: op marked `FAILED` after `MAX_RETRIES`.
- Test `retryFailed()` resets status and re-runs queue.
- Test network loss mid-queue: verify `isSyncing` resets.

## Naming

```
fun `given X when Y then Z`() { … }
```

## What NOT to Test

- Trivial getters/setters.
- Compose UI rendering (use screenshot or Espresso tests instead).
- Private functions directly — test via public API.