---
description: "Code review skill for Android Compose projects, enforcing best practices in state management, Compose optimizations, UI fluidity, design system adherence, and architectural structure."
---

## 1. State, Reactive Flow & Concurrency

* **Stream Combination:** Reject multiple state flows exposed to a single screen. Demand stream
  combination (`combine`, `flatMapLatest`) in the ViewModel. The UI must consume exactly one
  `UiState` stream.
* **One-Off Events:** Reject state-based event handling that requires UI-side resets (e.g.,
  `errorMessage = null` after showing a snackbar). Enforce `Channel` -> `receiveAsFlow()` for
  navigation and transient UI events.
* **Immutability:** Reject standard `List`/`Set`/`Map` in Compose state. Demand
  `kotlinx.collections.immutable` (`ImmutableList`, etc.).
* **Threading:** Reject DB/network ops outside `withContext(Dispatchers.IO)`. Demand complex UI data
  transformations (e.g., heavy list filtering/sorting) happen on `Dispatchers.Default` inside the
  ViewModel before reaching the UI State.
* **State Hoisting:** Flag `remember {}` state that should be hoisted to the caller or ViewModel.
* **UDF (Unidirectional Data Flow):** The entire application must follow a reactive, unidirectional
  data flow. Reject direct state mutations or side effects in composables.

## 2. Compose Phase Mastery & Optimizations (Cutting-Edge)

* **Deferred State Reads:** Reject reading rapidly changing state (scroll position, drag offsets)
  directly in the Composition phase. Enforce lambda modifiers (`Modifier.offset { ... }`,
  `Modifier.graphicsLayer { ... }`, `drawBehind { ... }`) to push reads to the Layout or Draw
  phases.
* **Derived State:** Enforce `derivedStateOf` when converting high-frequency state into
  low-frequency state (e.g.,
  `val showButton by remember { derivedStateOf { scrollState.value > 100 } }`).
* **Stability:** Reject unstable classes passed as composable arguments. Demand `@Immutable` or
  `@Stable` annotations on domain models crossing into the UI, or wrap them properly.
* **Lazy List Optimization:** Demand explicit `key` and `contentType` for all `LazyColumn`/`LazyRow`
  items. Reject implicit indexing. Reject heavy initializations inside `items { ... }`.
* **Recomposition Scoping:** Flag massive composables. Enforce breaking down large UI blocks into
  smaller, isolated composables to restrict recomposition scopes.

## 3. Fluidity & Snappy UI

* **Animation by Default:** Flag abrupt or jarring UI changes (e.g., instantly
  appearing/disappearing elements). Enforce `AnimatedVisibility`, `animate*AsState` (e.g.,
  `animateColorAsState`), or `Modifier.animateContentSize()` for state transitions.
* **Touch Feedback:** Ensure all clickable elements possess a proper ripple or visual feedback.
  Reject raw `pointerInput` where `clickable` or `combinedClickable` suffices.
* **Render Blocking:** Reject any synchronous I/O or heavy computation on the Main thread that
  threatens the 16ms frame budget.

## 4. Design System & Theming

* **Modifier Position:** Enforce `modifier: Modifier = Modifier` as the last non-lambda parameter. It follows required params (no defaults), before optional params (with defaults). If all params have defaults, modifier may be first.
* **Theme Enforcement:** Reject any `MaterialTheme` import or usage → enforce `FinsibleTheme`.
* **Hardcoded Values:**
    * Reject explicit strings → enforce `stringResource`.
    * Reject explicit `Color(0xFF...)`, `.dp`, `.sp` in composables → all from `FinsibleTheme.*`.
      Exception: `0.dp`/`0.sp`.
* **Propagation:** Reject theme tokens passed as explicit params to deep template components →
  demand `CompositionLocal` propagation for design system variables.
* **Semantics:** Reject inaccurate usage of semantic tokens.
* **Reusability:** Leverage `/templates/components/`. Reject copy-pasted layout logic.

## 5. Structure & Architecture

* **Separation of Concerns:** Keep UI-related code in `/ui/`, data-related code in `/data/`.
* **Repository Laws:**
    * Local repositories must not depend on other local repositories (can depend on remote).
    * Remote repositories must not depend on local repositories.
    * Local repos = data transformations + business logic.
    * Remote repos = network calls + data fetching.
* **ViewModels:** Strictly UI State preparation. Zero business logic or raw data transformations.
* **UI/ViewModel Interaction:** Use MVVM, MVI, or hybrid depending on complexity, but NEVER mix
  patterns within the same screen.
* **Dependency Injection:** Leverage Hilt. Reject manual instantiation. Ensure strict scoping (
  `@Singleton`, `@ActivityRetainedScoped`).