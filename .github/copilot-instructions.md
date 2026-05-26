# Finsible Frontend — Copilot Instructions

## Project Identity

Android finance app (Kotlin + Jetpack Compose) using Hilt DI, ObjectBox local DB, Retrofit, and a
custom MVVM+MVI hybrid. Package root: `com.itsjeel01.finsiblefrontend`.

## Architecture at a Glance

```
FinsibleApp (Application)
  └── MainActivity
        └── NavigationRoot
              └── Screens (HistoryTab, AccountsTab, DashboardTab, …)
                    └── ViewModels (Hilt @HiltViewModel)
                          └── Repositories (local + remote)
                                └── Entities / Models
```

### Layer Rules

- **UI layer** (`ui/`): Composables + ViewModels only. No direct DB or network calls.
- **Data layer** (`data/`): Repositories, entities, sync, remote API. Zero Compose imports.
- **Common** (`common/`): Pure utilities, enums, formatters, logging. No Android framework imports
  beyond `Context` where unavoidable.
- Cross-layer communication is **always** via `StateFlow` or suspend functions — never callbacks or
  LiveData.

## State & Flow Philosophy

- Every screen state is a single sealed/data class emitted by a `StateFlow` in the ViewModel.
- UI state = `MutableStateFlow` (private) + `StateFlow` (public, `asStateFlow()`).
- Collect with `collectAsStateWithLifecycle()` — never `collectAsState()`.
- Prefer `SharingStarted.WhileSubscribed(5000)` for flows derived from repositories.
- Side effects travel via a separate `SharedFlow<UiEvent>` — never embedded in state.

## MVVM + MVI Hybrid

- **MVVM** owns state shape and data loading (ViewModel holds `StateFlow` per concern).
- **MVI** owns intent dispatch: UI calls named `fun` on ViewModel (`loadMore()`,
  `applyFilterCriteria()`, `toggleSearchExpanded()`). No direct state mutation from UI.
- Filter/sort state is a separate `StateFlow<TransactionsFilterState>` — never merged into the main
  `TransactionListState`.

## Compose Rules

- Modifier position: follows required params, before optional params. If all params have defaults,
  modifier may be first.
- State hoisting: lift state to the **lowest common ancestor** that needs it. Local `remember`/
  `mutableStateOf` for ephemeral UI-only state (bottom sheet visibility, scroll position).
- Every composable receives only what it uses — no passing entire `ViewModel` references beyond
  screen-level composables.
- Screen-level composables (e.g., `HistoryTab`) receive a typed ViewModel. Sub-components receive
  plain data + lambdas.
- Stability: use `kotlinx-collections-immutable` (`ImmutableList`, `ImmutableMap`) for list/map
  parameters to avoid unnecessary recomposition.
- Derive display state with `remember { derivedStateOf { … } }`, not repeated inline calculations.
- `LaunchedEffect` keys must be minimal and precise — one concern per `LaunchedEffect`.
- `snapshotFlow` for reactive observation of Compose state inside coroutines.

## Coroutine Rules

- All IO work runs on `Dispatchers.IO` via `withContext(Dispatchers.IO)`.
- `viewModelScope` for ViewModel-scoped coroutines.
- Long-running app-level coroutines use `ScopeManager`-provided `CoroutineScope` (`@Singleton`).
- Cancel superseded jobs explicitly (e.g., `searchDebounceJob?.cancel()`).
- `delay()` for debounce — never `Thread.sleep()`.

## ObjectBox / Local DB

- All queries go through `BaseLocalRepository<DTO, Entity>` or `SyncableLocalRepository`.
- DB-level filtering always preferred over in-memory post-filtering.
- Pagination via `query.find(offset, limit)` — never `box.all` then slice.
- Aggregates (`sum`, `count`) via ObjectBox property queries — never stream + fold in Kotlin.
- Wrap multi-step reads in `box.store.callInReadTx { … }`.

## Sync Architecture

- `SyncManager` is `@Singleton`. Processes `PendingOperationEntity` queue on network restore.
- Optimistic local writes: entity written locally first with `Status.PENDING`, synced in background.
- Each entity type has an `EntitySyncHandler` registered in the Hilt
  `Map<EntityType, EntitySyncHandler>`.
- Max retries = `SyncManager.MAX_RETRIES` (3). Failed ops marked `Status.FAILED`, retriable via
  `retryFailed()`.

## Naming Conventions

| Artifact              | Convention                                  | Example                                 |
|-----------------------|---------------------------------------------|-----------------------------------------|
| Entities              | `*Entity`                                   | `TransactionEntity`                     |
| Domain models         | plain noun                                  | `Transaction`                           |
| UI models             | `*UIModel`                                  | `TransactionUIModel`                    |
| UI state              | `*State`                                    | `TransactionListState`                  |
| UI events             | `*UiEvent`                                  | `NewTransactionUiEvent`                 |
| Repositories (local)  | `*LocalRepository`                          | `TransactionLocalRepository`            |
| Repositories (facade) | `*Repository`                               | `TransactionRepository`                 |
| ViewModels            | `*ViewModel`                                | `HistoryViewModel`                      |
| Screens               | tab name                                    | `HistoryTab`, `AccountsTab`             |
| Composable components | `Finsible*` (design system) or feature name | `FinsibleButton`, `TransactionListItem` |

## DI (Hilt)

- `@HiltViewModel` for all ViewModels.
- `@Singleton` for repositories, sync infrastructure, scope manager.
- Scoped Hilt modules per feature: `TransactionModule`, `AccountModule`, `SyncModule`, etc.
- Multi-binding (`@IntoMap`) for `EntitySyncHandler` map.

## Logging

Use the domain-scoped logger — never `Log.d` directly:

```kotlin
Logger.Database.d("…")
Logger.Sync.i("…")
Logger.UI.e("…", exception)
Logger.App.d("…")
```

## KDoc Policy

- KDoc only on **public API** of repositories, sync handlers, and non-obvious ViewModel functions.
- Single-line `/** … */` for brief clarifications. `@param`/`@return` only for low-level widely used
  definitions.
- Composables: no KDoc unless the component has non-obvious behavior or required contract.
- Never comment what the code clearly shows. Prefer expressive names.
- No state kDoc

## What NOT to Do

- No `LiveData` — use `StateFlow`/`SharedFlow` only.
- No `ViewModel` references inside non-screen composables.
- No `box.all` for queries that need filtering.
- No in-memory sort/filter after DB fetch when DB-level is possible.
- No `Thread.sleep()`, no `GlobalScope`.
- No refactoring/justifying/bandage comments (`// TODO: refactor`, `// FIXME: clean this up`,
  `// Changed to this because that`, `// Replace with this`, etc).
- No redundant type annotations Kotlin can infer.
- No unused imports.
- No inline full package names — use imports instead.
- No hardcoded animations, colors, dimensions — always use resources or available semantic tokens
  from `FinsibleTheme`.
- No arithmetic operations on semantic tokens (e.g., `FinsibleTheme.spacing.medium * 2`) — define
  new tokens for specific use cases instead.
- No imperative state usages, always ensure reactive flow-based patterns. No mutable state exposed
  to UI.
- **Anti-Patterns (Hard No):**
  - No direct server writes from ViewModel or Repository — always go through LocalRepository pending ops.
  - No ad-hoc `Log.*` — use `Logger.{Domain}.d/e/w` (`Logger.Sync`, `Logger.Database`, `Logger.UI`, `Logger.App`).
  - No unrelated icon libraries when project icon packs already provide equivalents.
  - No business logic in navigation files or `@Preview` composables.
  - No shared production logic in variant-specific (`debug`/`release`) source sets.