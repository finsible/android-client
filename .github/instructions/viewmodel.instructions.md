---
applyTo: "app/src/main/java/**/viewmodel/**"
---

# ViewModel Instructions

## State Shape

Each ViewModel exposes one primary UI state and zero or more auxiliary states:

```kotlin
// Primary (screen content)
private val _uiState = MutableStateFlow(TransactionListState())
val uiState: StateFlow<TransactionListState> = _uiState.asStateFlow()

// Auxiliary (orthogonal concerns — never merge into primary state)
private val _filterState = MutableStateFlow(TransactionsFilterState.DEFAULT)
val filterState: StateFlow<TransactionsFilterState> = _filterState.asStateFlow()

private val _isSearchExpanded = MutableStateFlow(false)
val isSearchExpanded: StateFlow<Boolean> = _isSearchExpanded.asStateFlow()
```

**Never** merge filter/sort/search state into the same data class as loading/error/data state. They
evolve independently.

## Loading & Error Pattern

```kotlin
_uiState.update { it.copy(isLoading = true) }
try {
    val result = withContext(Dispatchers.IO) { repository.query(…) }
    _uiState.update { it.copy(isLoading = false, data = result, error = null) }
} catch (e: Exception) {
    Logger.UI.e("…", e)
    _uiState.update { it.copy(isLoading = false, error = context.getString(R.string.error_…)) }
}
```

## Intent Functions (MVI)

Public functions on the ViewModel are intents — they describe **user actions**, not internal
operations:

- `loadMore()`, `applyFilterCriteria(state)`, `toggleSearchExpanded()`, `collapseSearch()`,
  `clearAllFilters()`
- Guard repeated calls: `if (loadMoreJob?.isActive == true || !_uiState.value.hasMoreData) return`
- Debounce search: cancel the previous job before launching a new one.

## Pagination

- `currentOffset: Int` tracks current page offset — reset to 0 on every new query.
- `ITEMS_PER_PAGE = 50` constant in companion object.
- `hasMoreData` in UI state: `true` iff `result.size == limit`.
- `loadMoreJob` and `searchDebounceJob` are explicit `Job?` references — cancel before re-launch.

## Derived / Aggregate Flows

Flows derived from preference or repository streams:

```kotlin
val currencyCode: StateFlow<String> = preferenceManager.defaultCurrencyCodeFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
```

Always use `SharingStarted.WhileSubscribed(5000)` to avoid upstream leaks.

## Dispatchers

- IO operations: `withContext(Dispatchers.IO) { … }` inside the coroutine.
- State updates always on Main — after the `withContext` block or via
  `withContext(Dispatchers.Main)`.
- Never `launch(Dispatchers.IO)` — use `launch` + `withContext` pattern.

## KDoc Guidance

Only document public functions where the intent is non-obvious from the name:

```kotlin
/** Load next page; no-op if already loading or no more data. */
fun loadMore() {
    …
}
```

No KDoc on state properties — the `StateFlow` type + property name is self-describing.