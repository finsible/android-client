# AGENTS

Audience: AI assistants working in this Android Jetpack Compose, offline-first app. Keep guidance
short, opinionated, and rooted in existing patterns.

This document is the canonical, comprehensive implementation playbook for this repository.

## Architecture at a Glance

- Offline-first stack is fixed:
  `ViewModel -> LocalRepository (ObjectBox) -> pending ops queue -> SyncManager/EntitySyncHandler -> Retrofit ApiService`;
  reads flow through `Repository + CacheManager` auto-cache.
- ViewModels only write through LocalRepositories; Repository layer is read-only for GET/caching.
- Local creates use negative IDs from `LocalIdGenerator`; handlers remap IDs after server create.
- `ResponseHandlingConverterFactory` + `CacheManager` must route `BaseResponse.cache` payloads into
  `LocalRepository.addAll`.
- Startup integrity path remains mandatory: `FinsibleApp` + `IntegrityResolverService` +
  `ScopeManager`, then `PostAuthInitializer` + `DataFetcher`/`IntegrityChecker` post-login.

## Source Sets and Variant Boundaries

- Shared implementation goes in `app/src/main/**`.
- Debug-only behavior goes in `app/src/debug/**`.
- Release-only behavior goes in `app/src/release/**`.
- Mirror package/class paths across variants when providing variant-specific implementations.
- Keep variant code thin; push reusable logic into `main`.

## Ownership Map (Where Code Should Live)

- `ui/navigation/**`: route graph declarations and transitions.
- `ui/screen/**`: composable screen rendering and user intent forwarding.
- `ui/viewmodel/**`: state management, intent handling, and data flow orchestration.
- `data/local/entity/**`: ObjectBox persistence models.
- `data/local/repository/**`: local writes, local queries, pending operation creation.
- `data/repository/**`: read-side remote fetch/caching integration only.
- `data/sync/**`: pending operation execution, retries, and remote write synchronization.
- `data/di/**`: Hilt module bindings/providers (including handler maps).

## Placement Decision Rules

- Put logic in ViewModel when it transforms domain data into UI state.
- Put logic in LocalRepository when it mutates local storage or queues operations.
- Put logic in SyncHandlers/SyncManager when it talks to network write endpoints or remaps IDs.
- Put logic in Repository when it performs GET/read concerns and cache hydration.
- Keep debug diagnostics/mocks in `src/debug`; never leak these into release paths.

## Developer Workflows

- Prefer focused diagnostics first (IDE/static checks such as `get_errors`-style file checks) for
  small/localized changes.
- Run full checks (`./gradlew assembleDebug`, `./gradlew testDebugUnitTest`, install flows) only for
  comprehensive/cross-layer changes.
- ObjectBox schema updates: edit entities in `data/local/entity`, commit
  `app/objectbox-models/default.json`, bump `DATABASE_SCHEMA_VERSION` in `app/build.gradle.kts` when
  breaking.

## Conventions & Patterns

- DI via Hilt modules in `data/di`; SyncHandlers map bound by `EntityType` in `SyncModule.kt`;
  shared coroutine scope from `ScopeManager`.
- Networking: OkHttp stack = MockInterceptor (debug) -> HttpLogging (debug) -> AuthInterceptor;
  Retrofit base URL from `BuildConfig.BASE_URL`.
- Logging: use `common.logging.Logger` domains (`Logger.Sync`, `Logger.Database`, `Logger.App`);
  avoid ad-hoc `Log.*`.
- Theming/UI tokens: use `FinsibleTheme` values only for colors/spacing/typography/shape.
- Durations: use `ui.constants.Duration` constants instead of inline timing literals.
- Icons: use the composables icon packs already used in the project and route category/domain
  mapping through `ui/util/IconResolver.kt`.
- UI structure: navigation entry in `ui/navigation/NavigationRoot.kt`; screens in `ui/screen`;
  state/event handling in `ui/viewmodel`.
- Component creation: follow strict 3-layer templates in `ui/component/templates/**`; expose
  `modifier` first; include `@Preview`; keep composables stateless by default.
- New reusable component template checklist:
    - Stateless composable with `modifier: Modifier = Modifier` first.
    - Optional stateful wrapper only when screen-level state hoisting is insufficient.
    - Preview coverage for primary UI states (default/loading/empty/error where applicable).
    - Style only with `FinsibleTheme` tokens (colors/typography/dimensions/gradients/shapes).
- Data integrity: prefer `DataFetcher.ensureDataFetched` + `IntegrityChecker` to avoid redundant
  fetches; when adding new entities, provide integrity checks and cache routing.
- Sync queue: `SyncManager` listens to `NetworkMonitor` and retries failed ops (max 3);
  `PendingOperationEntity` stores payload JSON for handlers; provide
  `toCreateRequest/toUpdateRequest` in LocalRepositories via `SyncableLocalRepository`.

## Screen, ViewModel, and Data Flow Contract

- Screen composables:
    - render state and emit UI events only.
    - avoid embedding business rules, sync rules, or persistence logic.
- ViewModels:
    - own screen state models and event reducers.
    - perform writes via LocalRepositories only.
    - avoid direct write API invocations.
- Repositories:
    - provide read models from network/cache/local pathways.
    - remain mutation-free.
- Sync layer:
    - executes queued operations, retries failures, and reconciles IDs/state.
    - remains the only network-write execution path.

## Delivery Checklists

- New reusable component:
    - start from `ui/component/templates/**` and keep `modifier` first.
    - provide stateless API first, stateful wrapper only if necessary.
    - add `@Preview` variants for key states.
    - style with `FinsibleTheme` tokens; timing with `Duration` constants.
    - use existing composables icon packs and resolver mapping patterns.
- New screen/flow:
    - add routes/wiring in `ui/navigation/NavigationRoot.kt`.
    - split render logic (`ui/screen`) from orchestration (`ui/viewmodel`).
    - consume read data via Repository-backed flows.
    - route writes through LocalRepository paths that enqueue pending ops.
    - ensure loading/empty/error UX states are represented.
- New entity support:
    - add local entity + LocalRepository operations.
    - ensure create uses negative IDs and queue serialization payloads.
    - add/update SyncHandler and bind in `SyncModule.kt`.
    - implement `toCreateRequest` and `toUpdateRequest` mapping.
    - add cache routing and integrity checks/fetches.

## Repository and Sync Structuring Rules

- Keep read concerns in repositories and write concerns in LocalRepository + SyncHandler flows.
- New entity support must include local entity/repository wiring, sync handler registration in
  `SyncModule.kt`, API mapping, and integrity/fetch coverage.
- Do not bypass pending operations for write paths.

## Anti-Patterns (Hard No)

- Direct server writes from ViewModel or Repository.
- Hardcoded UI values for colors/spacing/typography/shapes/timings.
- Ad-hoc logging APIs when `common.logging.Logger` domains are available.
- Introducing unrelated icon libraries when project icon packs already provide equivalents.
- Business logic living in navigation files or composable previews.
- Shared production logic implemented only in variant-specific (`debug`/`release`) source sets.

## Validation and Quality Gate

- Use focused diagnostics first (`get_errors`-style checks) for localized edits.
- Run full Gradle checks only when scope is comprehensive/cross-layer.
- Recommended full checks for broad changes:
    - `./gradlew assembleDebug`
    - `./gradlew testDebugUnitTest`
    - `./gradlew installDebug` (when device behavior is part of the change)
- Pre-PR self-check:
    - architecture chain preserved,
    - source-set boundaries respected,
    - UI template/theming/duration/icon rules followed,
    - integrity/cache/sync wiring updated,
    - docs tree updated if structure changed.

## Quick Request Triage Matrix

- New screen or flow:
    - Start in `ui/navigation/NavigationRoot.kt`, `ui/screen/**`, `ui/viewmodel/**`.
    - Keep reads repository-backed and writes LocalRepository-backed.
    - Validate touched files with focused diagnostics.
    - Utilise templatized components from `ui/component/templates/**` as much as possible.
- New reusable component:
    - Start from `ui/component/templates/**`, then place in `ui/component/**`.
    - Keep stateless API first, `modifier` first, previews included, `FinsibleTheme` + `Duration`
      only.
    - Validate component + preview files.
- New entity support:
    - Start in `data/local/entity/**`, `data/local/repository/**`, `data/sync/**`,
      `data/di/SyncModule.kt`.
    - Ensure negative IDs, pending op enqueue, handler binding, cache + integrity coverage.
    - Use targeted checks first, then full Gradle checks for broad/cross-layer impact.
- Sync failures (retry/remap/order):
    - Start in `data/sync/**` and entity-specific handler.
    - Verify ordering, max retry behavior, and local ID remap after create.
    - Escalate to broader checks when multiple handlers/entities are involved.
- Cache/GET hydration issues:
    - Start in `data/remote/converter/ResponseHandlingConverterFactory.kt`,
      `data/sync/CacheManager.kt`, `data/repository/**`.
    - Preserve `BaseResponse.cache` routing into local repositories.
    - Validate affected repositories and consumers.
- Startup/auth/integrity regression:
    - Start in `FinsibleApp.kt`, `data/sync/PostAuthInitializer.kt`,
      `data/sync/IntegrityResolverService.kt`, launch/onboarding screens.
    - Preserve startup check-and-resolve and post-auth fetch/check flow.
    - Run full checks only if flow changed across multiple layers.
- Debug diagnostics/mock behavior:
    - Implement in `app/src/debug/**` only.
    - Keep variant paths mirrored and avoid release/main coupling.
    - Validate debug-source files directly.
- Release-only implementation:
    - Implement in `app/src/release/**` with mirrored package/class path when variant classes exist.
    - Avoid references to debug-only classes.
    - Validate targeted files; broaden checks if shared startup/navigation is affected.
- UI polish/theming/animation request:
    - Edit `ui/screen/**` or `ui/component/**`; use `ui/theme/**` tokens and
      `ui.constants.Duration`.
    - Avoid hardcoded design values.
    - Validate touched files with focused checks.
- Logging consistency request:
    - Replace ad-hoc logs with `common.logging.Logger` domain logger calls.
    - Use domain fit: `Logger.Sync`, `Logger.Database`, `Logger.App`.
    - Validate touched files only.

## Standard Task Execution Sequence

- Classify request type and expected blast radius.
- Choose owner layer first (UI/ViewModel/LocalRepository/Repository/Sync) using placement rules.
- Implement minimal primary-file changes first, then wire dependent layers.
- Run focused diagnostics (`get_errors`-style) for localized edits.
- Run Gradle checks only when the change is broad, cross-layer, or architecture-risky.
- Re-check anti-pattern rules and update project structure tree when architecture-relevant paths
  change.

## Project Structure Reference (Keep Updated)

Update this tree whenever architecture-relevant files/folders are added, moved, or removed.

```text
FinsibleFrontend/
|- .github/
|  |- copilot-instructions.md
|- AGENTS.md
|- app/
|  |- build.gradle.kts
|  |- objectbox-models/
|  |  |- default.json
|  |- src/
|  |  |- main/java/com/itsjeel01/finsiblefrontend/
|  |  |  |- common/logging/
|  |  |  |- data/
|  |  |  |  |- di/
|  |  |  |  |- local/entity/
|  |  |  |  |- local/repository/
|  |  |  |  |- remote/converter/
|  |  |  |  |- repository/
|  |  |  |  |- sync/
|  |  |  |- ui/
|  |  |  |  |- component/templates/
|  |  |  |  |- constants/
|  |  |  |  |- navigation/
|  |  |  |  |- screen/
|  |  |  |  |- theme/
|  |  |  |  |- util/IconResolver.kt
|  |  |- debug/java/com/itsjeel01/finsiblefrontend/
|  |  |- release/java/com/itsjeel01/finsiblefrontend/
```

## Pointers to Explore First

- Overall flow: Refer project structure
- Sync layer: `data/sync/*`, `data/local/repository/SyncableLocalRepository.kt`,
  `data/local/entity/PendingOperationEntity.kt`.
- Caching + converters: `data/remote/converter/ResponseHandlingConverterFactory.kt`,
  `data/sync/CacheManager.kt`, `data/repository/*`.
- UI system + tokens: `ui/component/templates/**`, `ui/theme/**`
- Startup/auth: `FinsibleApp.kt`, `data/sync/PostAuthInitializer.kt`,
  `data/sync/IntegrityResolverService.kt`, `ui/screen/Launch.kt`/`Onboarding.kt`.

