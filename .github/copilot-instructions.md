# Copilot Instructions

Audience: GitHub Copilot / AI pair-programming in this offline-first Jetpack Compose app.

Use this file as a quick-start. `AGENTS.md` is the canonical, comprehensive playbook and should be
treated as source of truth for detailed checklists, triage matrices, and project structure
references.

## Canonical Reference

- Primary guide: `AGENTS.md`.
- If guidance conflicts, follow system/developer instructions first, then `AGENTS.md`.
- Keep detailed process updates in `AGENTS.md`; keep this file concise.

## Non-Negotiable Architecture Rules

- Preserve the offline-first chain:
  `ViewModel -> LocalRepository (ObjectBox) -> pending ops queue -> SyncManager/EntitySyncHandler -> Retrofit ApiService`.
- Keep reads in `Repository + CacheManager` pathways.
- Writes must not go through `data/repository/**`; ViewModels write via LocalRepositories only.
- Local creates use negative IDs from `LocalIdGenerator`; sync handlers remap IDs after server
  create.
- Route `BaseResponse.cache` through `ResponseHandlingConverterFactory` + `CacheManager` into
  `LocalRepository.addAll`.
- Preserve startup integrity sequence: `FinsibleApp` + `IntegrityResolverService` + `ScopeManager`,
  then `PostAuthInitializer` + `DataFetcher`/`IntegrityChecker`.

## Source Sets and Placement Rules

- Shared production code: `app/src/main/**`.
- Debug-only behavior: `app/src/debug/**`.
- Release-only behavior: `app/src/release/**`.
- Mirror package/class paths across variants when the same class exists.
- Keep variant-specific code thin and reusable logic in `main`.

## Quick Ownership Map

- `ui/navigation/**`: route graph + transitions only.
- `ui/screen/**`: rendering + event forwarding only.
- `ui/viewmodel/**`: state orchestration and LocalRepository write calls.
- `data/local/**`: persistence, local mutations, pending-op enqueue.
- `data/repository/**`: read-side fetch/cache only.
- `data/sync/**`: queued network writes, retries, remaps, reconciliation.

## Code Practices (Required)

- Logging: use `common.logging.Logger` domains (`Logger.Sync`, `Logger.Database`, `Logger.App`), not
  ad-hoc `Log.*`.
- UI tokens: use `FinsibleTheme` for colors/spacing/typography/shapes.
- Timing: use `ui.constants.Duration` constants instead of raw timing literals.
- Icons: use project composables icon packs and resolver patterns in `ui/util/IconResolver.kt`.
- Components: follow `ui/component/templates/**`, keep `modifier: Modifier = Modifier` first, keep
  reusable base composables stateless, include `@Preview` coverage.

## Validation Policy

- Prefer focused diagnostics first (for example `get_errors`-style checks) for localized changes.
- Run full Gradle checks only when changes are broad/cross-layer/architecture-risky:
    - `./gradlew assembleDebug`
    - `./gradlew assembleRelease`
    - `./gradlew testDebugUnitTest`
    - `./gradlew installDebug` (if device behavior changed)
- For ObjectBox schema changes: update entity files, commit `app/objectbox-models/default.json`,
  bump `DATABASE_SCHEMA_VERSION` in `app/build.gradle.kts` when required.

## Anti-Patterns (Do Not Introduce)

- Direct server writes from ViewModel or Repository.
- Bypassing pending operations for mutable actions.
- Hardcoded UI/timing tokens when project tokens/constants exist.
- New icon sources when existing composables packs already cover the need.
- Shared business logic living only in variant source sets.

## Detailed Playbook in AGENTS.md

For full guidance, use `AGENTS.md` sections:

- `Quick Request Triage Matrix`
- `Standard Task Execution Sequence`
- `Delivery Checklists`
- `Validation and Quality Gate`
- `Project Structure Reference (Keep Updated)`
- `Pointers to Explore First`
