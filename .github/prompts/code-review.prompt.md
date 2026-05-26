---
mode: agent
description: Full architectural code review for Finsible Kotlin/Compose files
---

Review the selected code against Finsible's architecture and coding standards. Check each category
and call out violations concisely.

## Architecture

- Is the correct layer being used? (UI → ViewModel → Repository → Entity — no skipping)
- Are there any Compose imports in the data layer, or DB/network calls in the UI layer?
- Does state flow unidirectionally (ViewModel → UI via `StateFlow`, UI → ViewModel via intent
  functions)?

## State & Flow

- Are all `StateFlow`s properly exposed as `asStateFlow()` with private `MutableStateFlow`?
- Is `collectAsStateWithLifecycle()` used (not `collectAsState()`)?
- Is `SharingStarted.WhileSubscribed(5000)` used for derived flows?
- Are filter/sort state and content state kept in separate `StateFlow`s?

## Compose Quality

- Is state hoisted to the correct level?
- Are list/map parameters wrapped in `ImmutableList`/`ImmutableMap`?
- Is `derivedStateOf` used for computed display booleans?
- Are `LaunchedEffect` keys minimal and single-concern?
- Are ViewModel references passed below screen level?
- Are any `dp`/`sp` values hardcoded instead of `FinsibleTheme.spacing.*` / `FinsibleTheme.sizes.*` / `FinsibleTheme.stroke.*`?

## Coroutines

- Is IO work dispatched via `withContext(Dispatchers.IO)`?
- Are state updates returned to Main (via `withContext(Dispatchers.Main)` or after the IO block)?
- Are superseded jobs cancelled before re-launch?
- Is `GlobalScope` or `Thread.sleep()` used anywhere?

## ObjectBox Queries

- Are filters applied at DB level, not in-memory post-fetch?
- Is `query.find(offset, limit)` used for pagination?
- Are aggregates computed via property queries (`sum`, `count`)?
- Are queries closed with `.use { … }`?

## Sync

- Are user mutations going through `queueCreateEntity`/`queueUpdateEntity`/`queueDeleteEntity`?
- Are `SyncException` instances thrown with correct `isRetryable` flags?

## Code Quality

- Are there refactoring comments (`// TODO: refactor`, `// FIXME: clean this up`) that should
  instead be fixed now?
- Is `Logger.*.*` used instead of `Log.d`?
- Are there unnecessary type annotations Kotlin can infer?
- Is KDoc present only where genuinely needed (non-obvious public API)?

List each issue with: **category**, **line reference**, **what's wrong**, **suggested fix** (one
line).