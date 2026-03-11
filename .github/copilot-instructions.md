- Limit JavaDoc generation to strictly single line comments, even the comment identifiers. e.g.
  `/** This is a single line comment. */`
- Generate imperative commit messages. Make sure to append comprehensive details in the body of the
  commit message. e.g. `Add feature X to improve user experience` is better than `Added feature X`.
- When refactoring jetpack compose code, ensure that state hoisting principles are followed.

# Repo-specific guidance for AI coding agents

1. Purpose: help contributors modify an Android app (Kotlin + Jetpack Compose) using Hilt DI,
   ObjectBox local DB, Retrofit + kotlinx.serialization, and Gradle/KSP codegen.

2. Key Files and Examples:

   | Concept                  | Path                                                                              |
       |--------------------------|-----------------------------------------------------------------------------------|
   | App entry & DI init      | `app/src/main/java/.../FinsibleApp.kt` (`@HiltAndroidApp`, `ObjectBoxModule.init`) |
   | Module config            | `app/build.gradle.kts` (Hilt, KSP, ObjectBox, secrets, Compose BOM)              |
   | ObjectBox model          | `app/objectbox-models/default.json`                                               |
   | Generated sources        | `app/build/generated/ksp/`, `app/build/generated/hilt/`, `app/build/generated/res/` — **never edit** |
   | Secrets                  | `secrets.properties` via secrets-gradle-plugin (not committed)                    |
   | Version catalog          | `gradle/libs.versions.toml`                                                       |
   | Reference ViewModel      | `ui/viewmodel/HistoryViewModel.kt`                                                |
   | Reference screen         | `ui/screen/HistoryTab.kt`                                                         |
   | Reference components     | `ui/component/historytab/` (full feature directory)                                |
   | Reference local repo     | `data/local/repository/TransactionLocalRepository.kt`                             |
   | Reference filter sheet   | `ui/component/historytab/TransactionFilterSheet.kt`                                |

3. Architecture Summary:

    - **Single-module** Android app: Jetpack Compose UI + Navigation (type-safe via
      kotlinx.serialization `@Serializable` route objects in `ui/navigation/Route.kt`).
    - **DI**: Hilt (`@HiltAndroidApp` in `FinsibleApp.kt`) + KSP Hilt compiler. Prefer constructor
      injection for ViewModels and repositories.
    - **Local persistence**: ObjectBox (model under `app/objectbox-models/`, plugin in
      `app/build.gradle.kts`, initialized in `FinsibleApp`). Generated entity classes live under
      `build/generated`.
    - **Networking**: Retrofit with kotlinx.serialization converter.
    - **Secrets**: secrets plugin reads `secrets.properties` — do not commit.
    - **State management**: ViewModels expose `StateFlow` (never `MutableStateFlow`) to UI. Use
      sealed
      classes / `@Immutable` data classes for UI state.
    - **Logging**: domain-specific via `Logger` object (e.g., `Logger.UI.e(...)`,
      `Logger.Database.d(...)`). Never use raw `Log` or `println`.

4. Developer Workflows (zsh):

    ```zsh
    ./gradlew assembleDebug          # Build + regenerate KSP/ObjectBox
    ./gradlew installDebug            # Install on device/emulator
    ./gradlew :app:testDebugUnitTest  # Module unit tests
    ./gradlew connectedAndroidTest    # Instrumentation tests
    ./gradlew clean                   # Clean build artifacts
    ./gradlew clean build             # Full clean + rebuild (needed when KSP/Hilt codegen is stale)
    ```

5. Compose UI Conventions (strict — follow the HistoryTab reference):

    - **Screen-level composables (`ui/screen/`)**:
        - Accept the ViewModel as a parameter (allows screen-level injection by parent).
        - Collect all `StateFlow` fields using `collectAsStateWithLifecycle()`.
        - **Never** mutate ViewModel state directly from UI — pass lambdas.
        - Use `remember` for expensive callbacks to avoid recomposition allocations.
        - Use `derivedStateOf` for computed read-only snapshot values.
        - Paginated infinite scroll uses `LaunchedEffect` + `snapshotFlow` +
          `distinctUntilChanged()`.
        - Extract all constants as `private const val` at file level.

    - **Component composables (`ui/component/`)**:
        - Organize components into feature directories (e.g., `ui/component/historytab/`).
        - Make components **stateless** — all data passed via parameters, events via lambdas.
        - Keep `private` composables for internal layout helpers within the same file.
        - Use `animateColorAsState` with `tween()` for color transitions — never hard-code delays.
        - Reference `Duration` constants from `ui/constants/Duration.kt`.
        - Use `AnimatedContent` with `transitionSpec` for content-swap animations.
        - Use `AnimatedVisibility` for show/hide transitions.

    - **Bottom sheets and filter sheets**:
        - Accept `SheetState` as a parameter with a default.
        - Use a dedicated ViewModel (e.g., `TransactionsFilterViewModel`) to own the **transient
          editing state** of the sheet.
        - Seed the sheet ViewModel from the applied state when the sheet becomes visible.
        - Sheet ViewModel builds the applied state and clears orphan fields before returning.
        - The parent ViewModel receives the result via `onApply` lambda — not shared state.

    - **LazyColumn / lists**:
        - Grouped mode: use `stickyHeader` for date headers with pre-computed aggregates.
        - Flat mode: simple `itemsIndexed` with `key = { _, item -> item.id }`.
        - Always provide stable keys for items.
        - Loading indicator at the bottom when paginating.

6. ViewModel Conventions (strict — follow HistoryViewModel reference):

    - **Structure**:
        - Annotate with `@HiltViewModel` and use constructor injection.
        - **Private** `MutableStateFlow` + **public** `StateFlow` via `.asStateFlow()`.
        - Use `.update { }` for atomic state mutations.
        - Coroutine scope: always `viewModelScope.launch { }`.
        - Heavy work on `Dispatchers.IO` via `withContext(Dispatchers.IO)`, switch back to `Main`
          for state updates.
        - Search debouncing: cancel previous job, delay, then execute.

    - **Pagination**:
        - Track `currentOffset` as a plain `var` (not in state).
        - Guard against concurrent loads.
        - `hasMore` determined by: `result.transactions.size == limit`.
        - Single entry point for all data loads.

    - **Error handling**:
        - Wrap IO work in try/catch. Log via `Logger.UI.e(...)`. Update state with error string.

7. UI State Model Conventions (strict):

    - **State data classes**:
        - Annotate with `@Immutable`.
        - Use `kotlinx.collections.immutable` types (`ImmutableList`, `ImmutableMap`) for all
          collection fields.

    - **UI model data classes**:
        - Annotate with `@Immutable`.
        - Separate from domain/entity models — live in `ui/model/`.
        - Include pre-formatted display strings computed during mapping (e.g., `formattedAmount`).
        - Include raw values when components need to compute (e.g., `rawAmountCentis` for
          aggregation).

    - **Filter / form state**:
        - Use a single `@Immutable` data class with a `DEFAULT` companion.
        - Include computed properties for UI convenience.
        - Separate **sheet-exclusive orphan fields** from fields consumed by the parent ViewModel.

    - **Sealed classes for complex UI state**:
        - Use sealed classes with `@Immutable` annotations.

    - **Enums with display text**:
        - Include `displayText` property and computed flags.

8. Mapper Conventions:

    - Entity → UI model mappers live in `ui/mapper/` as extension functions.
    - All formatting (amount signs, currency symbols, account labels) happens in the mapper, not in
      the composable.
    - Domain model → Entity mappers live as extension functions in `data/model/`.

9. Local Repository Conventions (strict — follow TransactionLocalRepository reference):

    - **Structure**:
        - Extend `SyncableLocalRepository` for sync-able entities, `BaseLocalRepository` for
          read-only.
        - Inject `Box<Entity>`, `Box<PendingOperationEntity>`, `LocalIdGenerator`, etc.

    - **Query methods**:
        - Apply **all** filters at the DB level using ObjectBox `QueryBuilder` — no in-memory
          post-filtering.
        - True DB-level pagination with `query.find(offset, limit)`.
        - Always `.use { }` queries.
        - Compute aggregates via separate optimized queries on first page only.
        - Return a `PaginatedResult` container.

    - **CRUD operations**:
        - Use `queueCreateEntity`, `queueUpdateEntity` for sync queueing.
        - Use `queueDeleteEntity`.
        - Always update `searchableText` when description or category name changes.

    - **Logging**:
        - Use `Logger.Database.d(...)` for all repository log messages.

10. Hilt DI Module Conventions:

    - One module per entity domain in `data/di/`.
    - Provide `Box<Entity>` from `BoxStore` with `@Singleton`.
    - Provide local repository with `@Singleton`.
    - Provide API service via `retrofit.create(...)`.
    - Compose access to singleton managers: use Hilt `@EntryPoint` pattern.

11. Design System — FinsibleTheme:

    - **General rules**:
        - **Never** hard-code colors, sizes, or font sizes — always use `FinsibleTheme` tokens.
        - `FinsibleTheme.colors.*` for all color references.
        - `FinsibleTheme.dimes.*` for all spacing/sizing.
        - `FinsibleTheme.typography.*` for all text styles.

    - **Colors**:
        - Structural, Backgrounds, Surfaces, Content/Text, Interactive, Borders, Buttons, Effects,
          Semantic, Brand Accent, Transaction Types, Gradients.

    - **Dimes**:
        - `d0` to `d800`.

    - **Typography**:
        - Tokens: `t72` to `t8`.
        - Modifiers: `.extraLight()` ... `.extraBold()`, `.tight()` ... `.loose()`, `.condensed()`/
          `.expanded()`, `.displayFont()`/`.interfaceFont()`.

    - **Scaling**:
        - `FinsibleUiScaler` auto-scales types and dimes per device width.
        - Use `Dp.adaptedDp()` for one-off runtime Dp scaling.

    - **Implementation details**:
        - Do not changes `FinsibleColorResolver` or remove `PreloadFonts()`.

12. Component Library (`ui/component/fin/`):

    - Use `FinsibleButton`, `FinsibleIconButton`, `FinsibleTextField` with config objects (
      `ButtonConfig`, `IconButtonConfig`, `TextFieldConfig`).
    - Use `ComponentSize` (Small, Medium, Large) and `ComponentType`.

13. Animation Conventions:

    - Use `Duration` constants from `ui/constants/Duration.kt`.
    - Prefer `animateColorAsState` + `tween`.
    - Prefer `AnimatedContent` + `transitionSpec`.
    - Prefer `AnimatedVisibility` with `fadeIn`/`fadeOut`.

14. Monetary Amount Conventions:

    - Store amounts as **centis** (`Long`, ×100).
    - Use `CurrencyFormatter` and `.centisToFormattedAmount()` extension.
    - Use `.toAmountCentis()` for parsing text input.

15. String Resources:

    - All user-visible text must use `stringResource(R.string.xxx)`.

16. Logging:

    - Use domain-specific `Logger` object (`Database`, `UI`, `Network`, `Auth`, `Sync`, `Cache`,
      `App`).

17. Navigation:

    - Routes defined as `@Serializable sealed interface Route : NavKey` in `ui/navigation/Route.kt`.
    - Nested routes supported.

18. Common Gotchas:

    - KSP/Hilt codegen missing -> `./gradlew clean build`.
    - Secrets not found -> `secrets.properties`.
    - ObjectBox model changes -> Update entity, run `./gradlew assembleDebug`, never edit
      `default.json`.
    - Stale immutable collections -> Use `.toPersistentList()` after mutations.

19. PR Checklist:

    - [ ] Brief architectural justification
    - [ ] Which generated artifacts are touched and rebuild command
    - [ ] How to test locally
    - [ ] Updated `default.json` committed (if needed)

---

## Checklist: Adding Support for a New API Call

(Same as previous checklist, but adhering to the new strict patterns above)

### 1. Define Remote API Models (`data/remote/model/`)

- [ ] Create response/request models with `@Serializable`.
- [ ] Wrap response in `BaseResponse<T>`.

### 2. Create Domain Model (`data/model/`)

- [ ] Create domain model with `@Serializable`.
- [ ] Add `toEntity()` extension.

### 3. Create ObjectBox Entity (`data/local/entity/`)

- [ ] Extend `BaseEntity` + implement `SyncableEntity` (if needed).
- [ ] Annotate with `@Entity`, `@Id(assignable = true)`.
- [ ] Define relationships (`ToOne<>`).
- [ ] Add `toDTO()` extension.

### 4. Update ObjectBox Model Schema

- [ ] Run `./gradlew assembleDebug`.
- [ ] Commit updated `app/objectbox-models/default.json`.

### 5. Create Retrofit API Service (`data/remote/api/`)

- [ ] Suspend functions returning `BaseResponse<T>`.

### 6. Create Local Repository (`data/local/repository/`)

- [ ] Extend `SyncableLocalRepository` (or `BaseLocalRepository`).
- [ ] Implement `addAll`, `toCreateRequest`, `toUpdateRequest`.
- [ ] Implement DB-level queries using `QueryBuilder` (pagination, filters).
- [ ] Return `PaginatedResult`.

### 7. Create Remote Repository (`data/repository/`)

- [ ] `@Singleton`, delegates to API service.

### 8. Create Hilt DI Module (`data/di/`)

- [ ] Provide `Box<Entity>` and local/remote repositories.

### 9. Create ViewModel (`ui/viewmodel/`)

- [ ] `@HiltViewModel`.
- [ ] Expose `StateFlow` via `.asStateFlow()`.
- [ ] Implement pagination with `currentOffset` var and concurrent-load check.

### 10. Create UI State Models (`ui/model/`)

- [ ] `@Immutable` state class with valid defaults.
- [ ] `@Immutable` filter class (if needed).

### 11. Create Mapper (`ui/mapper/`)

- [ ] Extension function `Entity.toUiModel(formatter)`.

### 12. Implement Compose UI (`ui/screen/` & `ui/component/`)

- [ ] Screen accepts `ViewModel`.
- [ ] Collect state with `collectAsStateWithLifecycle()`.
- [ ] Stateless components using `FinsibleTheme`.
- [ ] Use `FinsibleButton`/`FinsibleTextField`.

### 13. Update Navigation (`ui/navigation/`)

- [ ] Add route to `Routes.kt`.
- [ ] Add to NavGraph.

### 14. Testing

- [ ] `./gradlew clean build`.
- [ ] Verify functionality.

