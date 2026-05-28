---
mode: agent
description: Scaffold a new feature following Finsible conventions
---

Scaffold a complete feature for Finsible. Ask me for the feature name if not provided, then generate
all required files in order.

## Required Artifacts (generate in this order)

### 1. Domain Model — `data/model/FeatureName.kt`

- Plain Kotlin data class, no Android imports.
- `toEntity()` extension if it maps to ObjectBox.

### 2. Entity — `data/local/entity/FeatureNameEntity.kt`

- Annotated with `@Entity`.
- Extends `BaseEntity` (has `id: Long = 0`).
- If syncable, also implement `SyncableEntity`.
- Enum fields use `@Convert` with the appropriate converter from `TypeConverters.kt`.

### 3. Local Repository — `data/local/repository/FeatureNameLocalRepository.kt`

- Extends `SyncableLocalRepository<DomainModel, Entity>` if synced, else `BaseLocalRepository`.
- Implement `entityType`, `idProperty()`, `syncStatusProperty()`, `toCreateRequest()`,
  `toUpdateRequest()`.
- All queries: DB-level filters, `.use { }` blocks, ObjectBox property DSL.

### 4. UI Model — `ui/model/uimodel/FeatureNameUIModel.kt`

- Immutable data class.
- Only fields the UI renders.

### 5. UI State — `ui/model/state/FeatureNameState.kt`

- Data class with `isLoading`, `error`, `data`, and any feature-specific display fields.
- Companion `DEFAULT` object.

### 6. UI Events (if needed) — `ui/model/event/FeatureNameUiEvent.kt`

- Sealed class of side-effect events (navigation, toast, etc.).

### 7. Mapper — `ui/mapper/FeatureNameMapper.kt`

- `fun FeatureNameEntity.toUiModel(…): FeatureNameUIModel`
- All formatting logic here — not in the composable.

### 8. ViewModel — `ui/viewmodel/FeatureNameViewModel.kt`

- `@HiltViewModel`, inject via constructor.
- Private `MutableStateFlow` → public `StateFlow` (`.asStateFlow()`).
- Intent functions named after user actions.
- Loading/error pattern with `withContext(Dispatchers.IO)`.
- `viewModelScope` for all coroutines.

### 9. Screen Composable — `ui/screen/FeatureNameTab.kt` or `FeatureNameScreen.kt`

- Receives `viewModel: FeatureNameViewModel`, `modifier: Modifier = Modifier`.
- `collectAsStateWithLifecycle()` for each flow.
- Passes lambdas and data to sub-components — no ViewModel reference beyond this file.

### 10. Hilt Module — `data/di/FeatureNameModule.kt`

- `@Module @InstallIn(SingletonComponent::class)`.
- `@Provides @Singleton` for repository.
- If syncable, `@IntoMap @EntityTypeKey(EntityType.FEATURE_NAME)` for the sync handler.

## Constraints

- No hardcoded `dp`/`sp` — use `FinsibleTheme.spacing.*` / `FinsibleTheme.sizes.*` / `FinsibleTheme.stroke.*`.
- No `LiveData`, no `GlobalScope`, no `Thread.sleep()`.
- Logging via `Logger.Database.d`, `Logger.UI.e`, etc.
- KDoc only on non-obvious public API.
- No refactoring comments.