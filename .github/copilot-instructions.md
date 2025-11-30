- Limit JavaDoc generation to strictly single line comments, even the comment identifiers. e.g.
  `/** This is a single line comment. */`
- Generate imperative commit messages. Make sure to append comprehensive details in the body of the
  commit message. e.g. `Add feature X to improve user experience` is better than `Added feature X`.
- When refactoring jetpack compose code, ensure that state hoisting principles are followed.

# Repo-specific guidance for AI coding agents

1. Purpose: help contributors modify an Android app (Kotlin + Jetpack Compose) using Hilt DI,
   ObjectBox local DB, Retrofit + kotlinx.serialization, and Gradle/KSP codegen.

2. Key files and examples to cite when making changes:

    - App entry & DI initialization:
      `app/src/main/java/com/itsjeel01/finsiblefrontend/FinsibleApp.kt` (annotated with
      `@HiltAndroidApp` and calls `ObjectBoxModule.init(this)`).
    - Module config: `app/build.gradle.kts` (plugins: Hilt, KSP, ObjectBox, secrets plugin; Compose
      enabled; compose BOM usage).
    - ObjectBox model: `app/objectbox-models/default.json` (update model here and regenerate via
      Gradle build).
    - Generated sources: `app/build/generated/ksp/`, `app/build/generated/hilt/`,
      `app/build/generated/res/` (do not edit generated code directly).
    - Secrets: `secrets.properties` is used by the secrets-gradle-plugin; keys in
      `secrets.properties` are referenced in `app/build.gradle.kts` via the plugin's API.

3. Architecture summary (what to know quickly):

    - Single-module Android app using Jetpack Compose UI and Navigation (
      `androidx.navigation.compose` with type-safe navigation via kotlinx.serialization).
    - Dependency Injection: Hilt (`@HiltAndroidApp` in `FinsibleApp.kt`) + KSP Hilt compiler. Prefer
      constructor injection for ViewModels and repositories.
    - Local persistence: ObjectBox (model under `app/objectbox-models/`, plugin applied in
      `app/build.gradle.kts` and initialized in `FinsibleApp`). ObjectBox generates entity classes
      under `build/generated`.
    - Networking: Retrofit with kotlinx.serialization converter (look for usages of `Retrofit` and
      `kotlinx.serialization` in `app/src`).
    - Secrets & config: secrets plugin reads `secrets.properties` (do not commit secrets; the repo
      already has `secrets.properties` in .gitignore via the plugin convention).
    - State management: ViewModels expose `StateFlow` (never `MutableStateFlow`) to UI. Use sealed
      classes for UI state (see `AuthState` in `ui/model/AuthState.kt`).

4. Developer workflows & commands (zsh examples):

    - Build the app and regenerate KSP/Generated code: `./gradlew assembleDebug` or
      `./gradlew build`.
    - Install on connected device/emulator: `./gradlew installDebug`.
    - Run unit tests: `./gradlew test` (module-specific: `./gradlew :app:testDebugUnitTest`).
    - Run instrumentation tests: `./gradlew connectedAndroidTest` or
      `./gradlew :app:connectedAndroidTest`.
    - Clean build artifacts: `./gradlew clean`.
    - If you modify `app/objectbox-models/default.json`, run a full build to regenerate ObjectBox
      classes: `./gradlew assembleDebug`.

5. Project-specific conventions & patterns to follow (strict, discoverable):

    - JavaDoc style: always use single-line JavaDoc comments: `/** Single-line comment. */` (see
      repo `.github/copilot-instructions.md`).
    - Commit messages: imperative subject line + detailed body. Example:
        - Subject: `Add safe sign-in flow using Credentials API`
        - Body:
          `Implement sign-in using Android Credentials API; update AuthRepository and inject into AuthViewModel. Add unit tests for sign-in success and network error cases.`
    - Avoid modifying generated code under `app/build/generated/*` or `build/` — change the source (
      KSP annotations, `app/objectbox-models/default.json`, or Gradle scripts) and rebuild.
    - Prefer using DI (Hilt) for repositories and data sources; application-level singletons are
      configured in modules under `app/src/main/java/.../data/di` (e.g. `ObjectBoxModule`,
      `NetworkModule`, `NotificationModule`).
    - When adding new libraries, use the central versions via `gradle/libs.versions.toml` (project
      root `gradle/` contains version catalog).
    - Use `@Singleton` for app-wide services (e.g., `AuthRepository`, `NotificationManager`,
      `LoadingIndicatorManager`).
    - Compose access to singleton managers: use Hilt entry points via composable helpers like
      `hiltNotificationManager()` and `hiltLoadingManager()` (see `data/di/NotificationModule.kt`).

6. Common gotchas & how to handle them:

    - KSP/Hilt codegen missing: do a clean and full build (KSP sometimes needs
      `./gradlew clean build` to re-generate sources).
    - Secrets not found at build-time: place local values in `secrets.properties` (do not commit).
      The plugin expects `secrets.properties` by name (see `app/build.gradle.kts`).
    - ObjectBox model changes: update `app/objectbox-models/default.json` and rebuild; ensure
      `ObjectBoxModule.init(this)` remains called from `FinsibleApp`.
    - Compose previews failing in IDE but build passes: rely on Gradle builds for authoritative
      results; ensure Compose compiler version in `app/build.gradle.kts` is consistent.

7. Testing guidance (what to look for in this repo):

    - Unit tests live under `app/src/test` and instrumentation tests under `app/src/androidTest`.
    - Use `./gradlew :app:testDebugUnitTest` for a quick local run of the module unit tests.

8. Integration & external dependencies to be aware of:

    - Google sign-in / ID token: `libs.googleid` and `androidx.credentials.play.services.auth`
      usages — check auth and credentials code paths when changing login flows.
    - Network serialization: `kotlinx.serialization` + Retrofit converter — changing data classes
      requires coordinated updates to serializers and API interfaces.
    - ObjectBox plugin `io.objectbox` in `app/build.gradle.kts` — DB schema and generated entity
      classes live under `app/objectbox-models/` and `build/generated`.
    - Post-authentication data sync: `PostAuthInitializer` (in `data/sync/`) fetches categories,
      accounts, and account groups after successful auth. Triggered from `AuthViewModel` after
      sign-in.

9. When proposing code changes, include these in PRs:

    - Brief architectural justification (why DI/DB/network change is needed).
    - Which generated artifacts will be touched (KSP/Hilt/ObjectBox) and the build command to
      regenerate them.
    - How to test locally (commands above) and which unit/instrumentation tests to run.

10. Examples from repo to reference in diffs or suggestions:

- `FinsibleApp.kt` — show DI and ObjectBox init pattern.
- `app/build.gradle.kts` — show applied plugins (Hilt, KSP, ObjectBox, secrets) and Compose setup.
- `app/objectbox-models/default.json` — single source of truth for ObjectBox entities.

11. Navigation & UI architecture patterns:

- App uses type-safe navigation with kotlinx.serialization (`@Serializable` route objects in
  `ui/navigation/Routes.kt`).
- Main navigation structure: `MainActivity` → `AppRoutes` (Launch/Onboarding/Dashboard) →
  `HomeRoutes` (nested navigation for Dashboard/Analytics/NewTransaction/Accounts/Settings tabs).
- `NavHost` setup in `MainActivity` wraps content with `FinsibleTheme` → `LoadingIndicatorHost` →
  `NotificationHost` (global UI overlays managed via singleton managers).
- Navigation graphs: `appNavGraph()` in `ui/navigation/LaunchNavGraph.kt` for app-level routes;
  `homeNavGraph()` in `ui/navigation/HomeNavGraph.kt` for tab-based navigation with custom
  animations.
- ViewModels: always use `@HiltViewModel` with constructor injection. Expose `StateFlow<T>` (not
  `MutableStateFlow`) to UI. Use sealed classes for complex UI states (see `AuthState`).
- Compose screens collect state via `collectAsStateWithLifecycle()` and pass lambdas for events to
  ViewModels (never mutate ViewModel state directly from UI).
- Global UI services (`NotificationManager`, `LoadingIndicatorManager`): inject via Hilt in
  ViewModels or access in Compose via `hiltNotificationManager()` / `hiltLoadingManager()` (see
  `data/di/NotificationModule.kt` for pattern).
- Encrypted preferences: `PreferenceManager` uses `EncryptedSharedPreferences` for storing auth
  tokens (JWT) and user data. Injected as singleton; check `common/PreferenceManager.kt`.

12. Theme / styling (FinsibleTheme) — available options and how to use them:

- Purpose: The app applies a custom design system via `FinsibleTheme` (root composable). Many
  screens access tokens via the `FinsibleTheme` object — prefer these tokens instead of hard-coded
  values.

- Where it's provided: `MainActivity` wraps the UI with `FinsibleTheme { ... }`. Screens and
  components (e.g., `NotificationHost`, `OnboardingScreen`) use `FinsibleTheme` tokens directly.

- Primary APIs you will use in code:

    - `FinsibleTheme.colors` — access color tokens (see list below).
    - `FinsibleTheme.dimes` — spacing tokens (d0, d1, d2, ... d300, d800). Use like
      `FinsibleTheme.dimes.d16`.
    - `FinsibleTheme.typography` — tokenized TextStyles (t72, t64, t56, ..., t8). Use like
      `style = FinsibleTheme.typography.t16`.
    - `FinsibleTheme.materialTypography` — underlying Material typography if needed.
    - `FinsibleTheme.resolveColor(token: String, fallback: Color? = null)` — resolve color by token
      via `FinsibleColorResolver`.
    - `FinsibleTheme.isDarkTheme()` — returns whether system dark theme is currently used.
    - `FinsibleTheme.screenWidth` / `screenHeight` — device dimensions (Dp) used by the scaler.
    - `Dp.adaptedDp()` extension — scale a custom Dp using `FinsibleUiScaler` (useful for
      programmatic dims).

- Color tokens (available on `FinsibleTheme.colors`):
    - **Structural**: transparent, black, white, same, inverse
    - **Backgrounds**: primaryBackground, secondaryBackground
    - **Surfaces**: surface, surfaceContainer, surfaceContainerHigh, surfaceContainerLow,
      surfaceContainerDim, card (legacy - prefer specific surface tokens)
    - **Content/Text**: primaryContent (+ 80/60/40/20/10/5), secondaryContent, tertiaryContent,
      onSurfaceVariant, placeholder, disabledContent
    - **Interactive Surfaces**: hover, hoverStrong, pressed, focused, disabled, input
    - **Borders/Outlines**: border, outline, outlineVariant, divider
    - **Buttons**: primaryButton, secondaryButton, link, selection
    - **Effects**: shadow, ripple, overlay, scrim
    - **Semantic**: error, success, warning, info
    - **Semantic Containers**: errorContainer, successContainer, warningContainer, infoContainer (
      10% opacity backgrounds)
    - **Brand Accent**: brandAccent and tonal steps (brandAccent90, 80, 70, 60, 50, 40, 30, 20, 10)
    - **Transaction Types**: income, expense, transfer
    - **Financial Card Gradients**: Use `FinsibleGradients` helper with `CardGradientType` enum (
      BRAND, SUCCESS, WARNING, INCOME, EXPENSE, SAVINGS, INVESTMENT, BUDGET, NEUTRAL, PREMIUM) - 10
      gradient pairs = 20 tokens (gradientBrand1/2, gradientSuccess1/2, etc.)
- Example color usage:

    - FinsibleTheme.colors.brandAccent40
    - FinsibleTheme.colors.surfaceContainerHigh
    - FinsibleTheme.colors.tertiaryContent
    - FinsibleGradients.getLinearGradient(CardGradientType.BRAND)
    - val c = FinsibleTheme.resolveColor("brandAccent50")

- Dimes (spacing) tokens (available on `FinsibleTheme.dimes`):
    - d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d12, d14, d16, d18, d20, d22, d24, d26, d28, d30,
      d32, d36, d40, d44, d48, d52, d56, d60, d64, d68, d72, d80, d88, d96, d100, d112, d124, d136,
      d148, d160, d180, d200, d220, d240, d260, d280, d300, d800
- Example dim usage:

    - Modifier.padding(FinsibleTheme.dimes.d16)
    - Spacer(Modifier.height(FinsibleTheme.dimes.d32))

- Typography tokens & helpers (available on `FinsibleTheme.typography` and via TextStyle helpers):
    - tokens: t72, t64, t56, t48, t44, t40, t36, t32, t28, t24, t20, t16, t14, t12, t10, t8
    - weight modifiers: `.extraLight()`, `.light()`, `.normal()`, `.medium()`, `.semiBold()`,
      `.bold()`, `.extraBold()`
    - line-height helpers: `.tight()`, `.standard()`, `.loose()`
    - letter-spacing helpers: `.condensed()`, `.expanded()`
    - font-family helpers: `.displayFont()`, `.interfaceFont()`
- Example typography usage:

    - Text("Hello", style = FinsibleTheme.typography.t16.medium())
    - Text("Title", style = FinsibleTheme.typography.t48.displayFont())

- Scaling behavior and device awareness:

    - `FinsibleUiScaler` scales `FinsibleTypes` and `FinsibleDimes` using device width via
      `DeviceInfo` (exposed as `FinsibleTheme.screenWidth` / `screenHeight`). When updating UI
      sizes, prefer the tokenized types & dimes so scaling stays consistent across devices.
    - Use `Dp.adaptedDp()` for one-off scaled Dp values computed at runtime.

- Implementation details and cautions for AI edits:
    - `FinsibleColorResolver` maps `FinsibleColors` to Material colorScheme. Avoid changing the
      resolver without understanding Material color mapping.
    - `PreloadFonts()` is used to ensure all font weights are ready; don't remove it unless you
      understand implications for font rendering.
    - When adding new tokens (colors/dimes/types), update `FinsibleColors`, `FinsibleDimes`, or
      `FinsibleTypes` and prefer adding usage examples in `ui/theme` files.
    - Do not hard-code px/sp colors or sizes in components — reference `FinsibleTheme` tokens to
      remain consistent with scaling and dark/light modes.

---

## Checklist: Adding Support for a New API Call

Follow this step-by-step checklist when implementing a new API feature (e.g., adding transaction
management, budget tracking, etc.):

### 1. Define Remote API Models (`data/remote/model/`)

-   [ ] Create response data model (e.g., `TransactionsData.kt`):
      ```kotlin
      @Serializable
      data class TransactionsData(
          val transactions: List<Transaction>,
          val totalCount: Int
      )
      ```
-   [ ] Wrap in `BaseResponse<T>` for consistency (already handled by API service return type)
-   [ ] Create request models if needed (e.g., `TransactionCreateRequest.kt`):
      ```kotlin
      @Serializable
      data class TransactionCreateRequest(
          val amount: Double,
          val categoryId: Long,
          val note: String?
      )
      ```
-   [ ] Add `@Serializable` annotation to all data classes for kotlinx.serialization

### 2. Create Domain Model (`data/model/`)

-   [ ] Create domain/business model (e.g., `Transaction.kt`):
      ```kotlin
      @Serializable
      data class Transaction(
          val id: Long,
          val amount: Double,
          val categoryId: Long,
          val accountId: Long,
          val date: Long,
          val note: String?
      )
      ```
-   [ ] Add conversion extension functions:
      ```kotlin
      fun Transaction.toEntity(): TransactionEntity { /* ... */ }
      ```

### 3. Create ObjectBox Entity (`data/local/entity/`)

-   [ ] Create entity extending `BaseEntity` (e.g., `TransactionEntity.kt`):
      ```kotlin
      @Entity
      data class TransactionEntity(
          @Id(assignable = true) override var id: Long = 0,
          var amount: Double = 0.0,
          var categoryId: Long = 0,
          var date: Long = 0,
          var note: String? = null
      ) : BaseEntity() {
          lateinit var category: ToOne<CategoryEntity>  // For relationships
          lateinit var account: ToOne<AccountEntity>
      }
      ```
-   [ ] Add `@Entity` annotation and `@Id(assignable = true)` for server-assigned IDs
-   [ ] Define relationships using `ToOne<>` or `ToMany<>` if needed
-   [ ] Add `toDTO()` extension function:
      ```kotlin
      fun TransactionEntity.toDTO(): Transaction { /* ... */ }
      ```

### 4. Update ObjectBox Model Schema (`app/objectbox-models/default.json`)

-   [ ] Run `./gradlew assembleDebug` to auto-generate entity metadata
-   [ ] ObjectBox plugin will update `default.json` automatically
-   [ ] **NEVER manually edit `default.json`** — let the plugin manage it
-   [ ] Commit the updated `default.json` to git

### 5. Create Retrofit API Service (`data/remote/api/`)

-   [ ] Create API interface (e.g., `TransactionApiService.kt`):

      ```kotlin
      interface TransactionApiService {
          @GET("transactions")
          suspend fun getTransactions(
              @Query("startDate") startDate: Long,
              @Query("endDate") endDate: Long
          ): BaseResponse<TransactionsData>
  
          @POST("transactions/create")
          suspend fun createTransaction(
              @Body request: TransactionCreateRequest
          ): BaseResponse<Transaction>
  
          @DELETE("transactions/{id}")
          suspend fun deleteTransaction(
              @Path("id") id: Long
          ): BaseResponse<Unit>
      }
      ```

-   [ ] Use `suspend` functions for coroutine support
-   [ ] Return `BaseResponse<T>` for all endpoints (consistency with caching system)

### 6. Create Local Repository (`data/local/repository/`)

-   [ ] Create repository extending `BaseLocalRepository<DTO, Entity>`:

      ```kotlin
      class TransactionLocalRepository @Inject constructor(
          override val box: Box<TransactionEntity>
      ) : BaseLocalRepository<Transaction, TransactionEntity> {
  
          override fun addAll(data: List<Transaction>, additionalInfo: Any?, ttlMinutes: Long?) {
              val entities = data.map { it.toEntity().apply { updateCacheTime(ttlMinutes) } }
              box.put(entities)
          }
  
          override fun syncToServer(entity: TransactionEntity) {
              // TODO: Implement sync logic if needed
          }
  
          // Add custom query methods
          fun getTransactionsByDateRange(start: Long, end: Long): List<TransactionEntity> {
              return box.query()
                  .greater(TransactionEntity_.date, start)
                  .less(TransactionEntity_.date, end)
                  .build()
                  .find()
          }
      }
      ```

-   [ ] Implement required `addAll()` and `syncToServer()` methods
-   [ ] Add domain-specific query methods using ObjectBox query builder

### 7. Create Remote Repository (`data/repository/`)

-   [ ] Create repository with API service injection:

      ```kotlin
      @Singleton
      class TransactionRepository @Inject constructor(
          private val apiService: TransactionApiService
      ) {
          suspend fun getTransactions(startDate: Long, endDate: Long): BaseResponse<TransactionsData> {
              return apiService.getTransactions(startDate, endDate)
          }
  
          suspend fun createTransaction(request: TransactionCreateRequest): BaseResponse<Transaction> {
              return apiService.createTransaction(request)
          }
  
          suspend fun deleteTransaction(id: Long): BaseResponse<Unit> {
              return apiService.deleteTransaction(id)
          }
      }
      ```

-   [ ] Add `@Singleton` annotation
-   [ ] Keep methods simple — just delegate to API service
-   [ ] Let `ResponseHandler` manage automatic caching via `NetworkModule`

### 8. Create Hilt DI Module (`data/di/`)

-   [ ] Create module for DI setup (e.g., `TransactionModule.kt`):

      ```kotlin
      @Module
      @InstallIn(SingletonComponent::class)
      object TransactionModule {
  
          @Provides
          @Singleton
          fun transactionEntityBox(store: BoxStore): Box<TransactionEntity> {
              return store.boxFor(TransactionEntity::class.java)
          }
  
          @Provides
          @Singleton
          fun transactionLocalRepository(
              box: Box<TransactionEntity>
          ): TransactionLocalRepository {
              return TransactionLocalRepository(box)
          }
  
          @Provides
          fun transactionApiService(retrofit: Retrofit): TransactionApiService {
              return retrofit.create(TransactionApiService::class.java)
          }
      }
      ```

-   [ ] Add `@Module` and `@InstallIn(SingletonComponent::class)`
-   [ ] Provide ObjectBox `Box<Entity>` using `BoxStore`
-   [ ] Provide local repository with `@Singleton`
-   [ ] Provide API service via Retrofit (no singleton needed)

### 9. Create ViewModel (`ui/viewmodel/`)

-   [ ] Create ViewModel with `@HiltViewModel`:

      ```kotlin
      @HiltViewModel
      class TransactionViewModel @Inject constructor(
          private val repository: TransactionRepository,
          private val localRepository: TransactionLocalRepository
      ) : ViewModel() {
  
          private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
          val transactions: StateFlow<List<Transaction>> = _transactions
  
          private val _uiState = MutableStateFlow<TransactionUiState>(TransactionUiState.Idle)
          val uiState: StateFlow<TransactionUiState> = _uiState
  
          fun loadTransactions(startDate: Long, endDate: Long) {
              viewModelScope.launch {
                  _uiState.value = TransactionUiState.Loading
  
                  repository.getTransactions(startDate, endDate)
                      .onSuccess { data ->
                          _transactions.value = data.transactions
                          _uiState.value = TransactionUiState.Success
                      }
                      .onFailure { error ->
                          _uiState.value = TransactionUiState.Error(error.message ?: "Unknown error")
                      }
              }
          }
      }
      ```

-   [ ] Use constructor injection with repositories
-   [ ] Expose `StateFlow<T>` (not `MutableStateFlow`) for UI
-   [ ] Use sealed classes/enums for complex UI states
-   [ ] Launch coroutines in `viewModelScope`
-   [ ] Handle success/failure cases explicitly

### 10. Create UI State Model (`ui/model/`)

-   [ ] Define sealed class for UI states:
      ```kotlin
      sealed class TransactionUiState {
          data object Idle : TransactionUiState()
          data object Loading : TransactionUiState()
          data object Success : TransactionUiState()
          data class Error(val message: String) : TransactionUiState()
      }
      ```

### 11. Implement Compose UI (`ui/screen/`)

-   [ ] Create composable screen:

      ```kotlin
      @Composable
      fun TransactionScreen() {
          val viewModel: TransactionViewModel = hiltViewModel()
          val transactions by viewModel.transactions.collectAsStateWithLifecycle()
          val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  
          LaunchedEffect(Unit) {
              viewModel.loadTransactions(startDate, endDate)
          }
  
          when (uiState) {
              is TransactionUiState.Loading -> LoadingIndicator()
              is TransactionUiState.Success -> TransactionList(transactions)
              is TransactionUiState.Error -> ErrorMessage(uiState.message)
              TransactionUiState.Idle -> { /* Initial state */ }
          }
      }
      ```

-   [ ] Use `hiltViewModel()` for injection
-   [ ] Collect state with `collectAsStateWithLifecycle()`
-   [ ] Use `LaunchedEffect` for initial data loading
-   [ ] Pass lambdas to ViewModel, never mutate state from UI

### 12. Update Navigation (if needed) (`ui/navigation/`)

-   [ ] Add route to `Routes.kt`:
      ```kotlin
      @Serializable
      data class Transactions(val filter: String? = null) : HomeRoutes()
      ```
-   [ ] Add navigation in `NavGraph`:
      ```kotlin
      composable<HomeRoutes.Transactions> { backStackEntry ->
          val args = backStackEntry.toRoute<HomeRoutes.Transactions>()
          TransactionScreen(filter = args.filter)
      }
      ```

### 13. Testing & Validation

-   [ ] Run `./gradlew clean build` to regenerate all code
-   [ ] Verify KSP generated code in `build/generated/ksp/`
-   [ ] Test ObjectBox queries in local repository
-   [ ] Test API calls with mock server or real backend
-   [ ] Add unit tests for ViewModel logic
-   [ ] Test UI state transitions

### Common Patterns to Remember:

- **Caching**: `BaseResponse` + `ResponseHandler` auto-caches responses when `cache: true`
- **Relationships**: Use `ToOne<Entity>` in entities, call `entity.relation.target` to access
- **State Management**: Private `MutableStateFlow` + public `StateFlow` in ViewModels
- **Error Handling**: Use `Result<T>` or sealed classes for operation results
- **Singleton Usage**: Repositories (`@Singleton`), Managers (`@Singleton`), API services (no
  singleton)

---

If anything here is unclear or you want additional examples (e.g., a sample PR template entry, or an
example of how to update `default.json` and regenerate), tell me which part and I will iterate.
