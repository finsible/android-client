# Finsible — Project Context & Skills

## Project

- **Type:** Android fintech app, single-module monorepo
- **Package:** `com.itsjeel01.finsiblefrontend`
- **Stack:** Kotlin · Jetpack Compose · Hilt · ObjectBox · Coroutines/Flow · Retrofit · Nav3
- **Source sets:** `main` · `debug` (playground, mocks, debug DI) · `release` (prod DI, nav
  resolvers)

---

## Context Graph

Graph lives at `.pi/context-graph.json`. **Never read it in full — look up only what you need.**

| What you need                              | Where to look                                              |
|--------------------------------------------|------------------------------------------------------------|
| File path for any class                    | `graph.symbols["ClassName"]`                               |
| Component params, constraints, token usage | `graph.templateLibrary.components["FinsibleXxx"]`          |
| Theme token names and values               | `graph.theme.colorTokens / typographyTokens / dimesTokens` |
| Entity → LocalRepo → DI chain              | `graph.data.entities / localRepositories / di`             |
| ViewModel injected deps + StateFlows       | `graph.ui.viewModels["XxxViewModel"]`                      |
| Dependency version or import pattern       | `graph.dependencies.{section}`                             |
| Architectural rules                        | `graph.rules`                                              |

Run `/graph-status` to check if the graph is current before starting a task.

---

## Coding Conventions

### Theme & Tokens

- Access tokens via `FinsibleTheme.colors.<token>` / `.typography.<token>` / `.spacing.<token>` /
  `.sizes.<token>`
- Look up exact token names in `graph.theme.colorTokens` before using — never guess
- Never pass tokens as parameters — CompositionLocal propagation only
- Never reference `MaterialTheme` directly — `FinsibleTheme` wraps it

### Template Component Structure

```
component/Finsible{Name}.kt         stateful composable, no hardcoded values
core/                               zero-state utils, DrawScope extensions, shared enums
default/Finsible{Name}Defaults.kt   @Composable fns resolving tokens from FinsibleTheme
model/Finsible{Name}Models.kt       @Immutable data classes
model/variant/Finsible{Name}Variant.kt
preview/Finsible{Name}Preview.kt    @Preview only — never shipped
util/                               Host/Manager pairs, runtime-only utilities
```

- Packages are flat — no per-component subfolders under `ui/component/templates/`
- `@Preview` only in `preview/` — never in `component/` or `default/`
- Naming: `Finsible{Name}` · `Default{Name}Content` · `{Name}State / Config / Slots`

### Composable Rules

- `Modifier` follows required params — before optional params. If all params have defaults, modifier may be first.
- State is hoisted — no `remember {}` for state that callers need to own
- No hardcoded colors, sizes, or spacing — always `FinsibleTheme.*`

### ViewModel Pattern

```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(
    private val repo: XxxRepository,
    // resolve other deps from graph.ui.viewModels["XxxViewModel"].injected
) : ViewModel() {
    private val _state = MutableStateFlow(XxxState())
    val state: StateFlow<XxxState> = _state.asStateFlow()

    // IO: viewModelScope.launch { withContext(Dispatchers.IO) { ... }
    //       withContext(Dispatchers.Main) { _state.update { ... } } }
}
```

- All collections in `StateFlow` state → `ImmutableList` / `ImmutableMap`
- DB and network calls → `withContext(Dispatchers.IO)`
- State updates → `withContext(Dispatchers.Main)`

### DI Pattern

```
BoxStore → Box<XxxEntity>  (provided in data/di/{Domain}Module.kt)
         → XxxLocalRepository
         → XxxRepository   (consumed by ViewModels)
All bindings: @Provides @Singleton inside @Module @InstallIn(SingletonComponent)
```

### Icon Usage

- Check `graph.dependencies.compose.icons.availableSets` before picking a set
- Import pattern: `graph.dependencies.compose.icons.importPattern`
- Never invent a set name

---

## Graph Update Rules

After every major change, determine the change scope and run the matching command **in the same
session — never defer**:

```
New template component               → /update-graph {Name}
Modified component params/tokens     → /update-graph {Name}
New symbol in an existing file       → /update-graph {Name}
New color / dimes / typography token → /build-graph
New entity or local repository       → /build-graph
New DI module                        → /build-graph
New ViewModel                        → /build-graph
File moved or renamed                → /build-graph
Dependency version changed           → /build-graph
```

If in doubt between the two — use `/build-graph`.