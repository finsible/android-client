---
applyTo: "app/src/main/java/**/ui/**"
---

# UI Layer Instructions

## Composable Anatomy

### Screen-level (e.g., `HistoryTab`, `AccountsTab`)

- Receives `viewModel: XViewModel` and `modifier: Modifier = Modifier`.
- Calls `collectAsStateWithLifecycle()` for each `StateFlow`.
- Passes plain data + lambdas down — no ViewModel reference below this level.
- Owns `LaunchedEffect` for one-time effects triggered by state changes.

### Component-level (e.g., `TransactionListItem`, `FilteredResultsSummary`)

- Parameters: only data it renders + lambdas it invokes.
- No `State` objects from the ViewModel — accept resolved values.
- Local ephemeral state (`var expanded by remember { mutableStateOf(false) }`) is fine here.

### Design system (e.g., `FinsibleButton`, `FinsibleText`, `FinsibleLoader`)

- Stateless. All configuration via parameters with `Finsible*Defaults` companion objects.
- `FinsibleSize`, `FinsibleShape` enums for sizing/shape variants.
- `FinsibleButtonVariant`, `FinsibleTextVariant` sealed classes for style variants.

## State Hoisting Rules

- Hoist state **exactly one level above** where it's consumed by two or more children.
- Bottom sheet visibility: `var showFilterSheet by remember { mutableStateOf(false) }` lives in the
  screen that owns both the trigger button and the sheet.
- List scroll state (`rememberLazyListState`) lives at the screen level and is passed to list
  composables.
- `rememberCoroutineScope()` at screen level; pass `scope.launch { … }` lambdas down, not the scope
  itself.

## Recomposition Optimisation

- Wrap stable list parameters in `ImmutableList<T>` / `ImmutableMap<K,V>`.
- Derive boolean display flags with `remember { derivedStateOf { … } }`:
  ```kotlin
  val showScrollToTop by remember {
      derivedStateOf { listState.firstVisibleItemIndex >= THRESHOLD }
  }
  ```
- `key()` in `LazyColumn` items on a stable ID (`transaction.id`), not index.
- Extract frequently-recomposing children into separate composables.

## LaunchedEffect Discipline

- One concern per `LaunchedEffect`. Key on the state that drives the effect:
  ```kotlin
  LaunchedEffect(filterState) { listState.scrollToItem(0) }
  ```
- Pagination trigger via `snapshotFlow { … }.distinctUntilChanged().collect { … }` inside a
  `LaunchedEffect`.
- Never put data loading logic inside `LaunchedEffect` — call a ViewModel function instead.

## Navigation

- Screens are registered in `NavigationRoot` (build-variant specific).
- Routes are sealed objects in `Route.kt`.
- Bottom tab navigation via `BottomTabNavigator`; `BottomNavState` tracks selected tab.
- Pass `NavController` to the navigation host only; resolve destinations in `NavigationRoot`.

## Theme Usage

```kotlin
FinsibleTheme.colors.*   // semantic color tokens
FinsibleTheme.spacing.*    // layout spacing (insetXs, stackMd, gapLg…)
FinsibleTheme.sizes.*      // size tokens (icon, touch…)
FinsibleTheme.typography.* // text styles
```

Never hardcode `dp` or `sp` values — always use `FinsibleTheme.spacing.*` or
`FinsibleTheme.sizes.*`.

## Animation

- Duration constants from `Duration` object (`Duration.MS_150`, `Duration.MS_200`).
- Enter: `fadeIn + slideInVertically`. Exit: `fadeOut + slideOutVertically`.
- Wrap conditional visibility in `AnimatedVisibility` with `tween()` spec.

## ViewModel Consumption Pattern

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
val filterState by viewModel.filterState.collectAsStateWithLifecycle()
```

Collect each `StateFlow` separately — do not zip or combine in the UI layer.

## Mappers

UI model mappers (`toUiModel(…)`) live in `ui/mapper/`. They take domain entities and formatting
dependencies (`CurrencyFormatter`, `CurrencyRepository`, `Context`) and return `*UIModel` data
classes. Call them on `Dispatchers.IO` inside the ViewModel, not in composables.