---
mode: agent
description: Generate a new Finsible composable component
---

Generate a Compose component for Finsible. Determine if this is a **design-system component** (
`Finsible*`) or a **feature component** and apply the correct template.

## Design System Component (`ui/component/templates/`)

```kotlin
@Composable
fun FinsibleComponentName(
    // Required params first, then optional with defaults
    requiredParam: Type,
    modifier: Modifier = Modifier,
    size: FinsibleSize = FinsibleSize.Medium,
    variant: FinsibleComponentVariant = FinsibleComponentVariant.Default,
    // ... style params from FinsibleComponentDefaults
): Unit
```

- Stateless — all state hoisted.
- Defaults object: `object FinsibleComponentNameDefaults { val size = FinsibleSize.Medium; … }`.
- Models in `ui/component/templates/model/FinsibleComponentNameModels.kt`.
- Variants in `ui/component/templates/model/variant/FinsibleComponentNameVariant.kt`.
- Preview in `ui/component/templates/preview/FinsibleComponentNamePreview.kt`.

## Feature Component (`ui/component/<feature>/`)

```kotlin
@Composable
fun FeatureComponentName(
    // Resolved data values — no ViewModel, no StateFlow
    data: RelevantUIModel,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
)
```

- Receives only the data it renders.
- Lambdas for all interactions.
- Local ephemeral state (`expanded`, `selected`) via `remember { mutableStateOf(…) }`.

## Both Types — Hard Rules

- `FinsibleTheme.dimes.*` for all spacing — no hardcoded `dp`.
- `FinsibleTheme.colors.*` for all colors — no `MaterialTheme.colorScheme.*`.
- `ImmutableList<T>` for list parameters.
- `key(item.id)` in any `LazyColumn`/`LazyRow` items.
- No side effects inside the composable body — use `LaunchedEffect` or callbacks.
- Generate a `@Preview` function at the bottom of the file.