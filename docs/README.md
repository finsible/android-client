# Finsible Android Documentation

## Navigation & Architecture

This directory contains comprehensive documentation for the Finsible Android app's navigation architecture and ViewModel lifecycle management.

### Documents

1. **[NAVIGATION_MIGRATION.md](./NAVIGATION_MIGRATION.md)** - Start here for an overview of the navigation architecture migration
   - Summary of all changes
   - Benefits and rationale
   - Migration examples for new features
   - Testing examples

2. **[NAVIGATION_ARCHITECTURE.md](./NAVIGATION_ARCHITECTURE.md)** - Detailed navigation architecture guide
   - NavigationCoordinator pattern
   - Navigation structure (app-level and home-level)
   - Navigation patterns (single-top, clear-and-navigate, modal)
   - Best practices

3. **[VIEWMODEL_SCOPING_GUIDE.md](./VIEWMODEL_SCOPING_GUIDE.md)** - Complete ViewModel scoping guide
   - Screen-scoped ViewModels
   - Graph-scoped ViewModels
   - Activity-scoped ViewModels
   - SavedStateHandle best practices
   - Memory optimization patterns
   - Testing examples

## Quick Start

### For Developers Adding New Features

1. Read [NAVIGATION_MIGRATION.md](./NAVIGATION_MIGRATION.md) for a quick overview
2. Follow the "Migration Path for New Features" section for examples
3. Refer to [NAVIGATION_ARCHITECTURE.md](./NAVIGATION_ARCHITECTURE.md) for detailed navigation patterns
4. Check [VIEWMODEL_SCOPING_GUIDE.md](./VIEWMODEL_SCOPING_GUIDE.md) for proper ViewModel scoping

### For Reviewers

1. Check that navigation uses NavigationCoordinator pattern
2. Verify ViewModels are properly scoped
3. Ensure SavedStateHandle is used for UI state
4. Confirm StateFlow (not MutableStateFlow) is exposed to UI

### For QA/Testing

1. Check [NAVIGATION_ARCHITECTURE.md](./NAVIGATION_ARCHITECTURE.md) for navigation flows
2. Review [VIEWMODEL_SCOPING_GUIDE.md](./VIEWMODEL_SCOPING_GUIDE.md) for state preservation testing
3. Test process death scenarios to verify SavedStateHandle usage

## Architecture Overview

```
Navigation Architecture
├── NavigationCoordinator Pattern
│   ├── AppNavigationCoordinator (app-level navigation)
│   └── HomeNavigationCoordinator (tab navigation)
│
├── ViewModel Scoping
│   ├── Screen-scoped (default)
│   ├── Graph-scoped (shared across graph)
│   └── Activity-scoped (global state)
│
└── Navigation Events
    ├── NavigationEvents (navigation results)
    └── NavigationEventChannel (one-time events)
```

## Code Examples

### Navigation
```kotlin
// Use coordinator for navigation
val coordinator = AppNavigationCoordinator(navController)
coordinator.navigateToHome(AppRoutes.Launch)
```

### ViewModel with SavedStateHandle
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(
        savedStateHandle.get<State>("state") ?: State.Initial
    )
    val state: StateFlow<State> = _state.asStateFlow()
    
    fun updateState(newState: State) {
        _state.value = newState
        savedStateHandle["state"] = newState
    }
}
```

### Graph-Scoped ViewModel
```kotlin
@Composable
fun MyScreen(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry<AppRoutes.Home>()
    }
    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
}
```

## Testing

Unit tests demonstrating best practices:
- `AppNavigationCoordinatorTest.kt` - Navigation coordinator testing
- `HomeViewModelTest.kt` - ViewModel and SavedStateHandle testing

## Key Principles

1. **Separation of Concerns**: Navigation logic separated from UI
2. **Proper Scoping**: ViewModels scoped to their lifecycle
3. **State Preservation**: SavedStateHandle for process death survival
4. **Testability**: Clear patterns for testing navigation and state
5. **Memory Optimization**: Proper ViewModel clearing and state management

## Future Enhancements

- Navigation 3.0 upgrade (when stable)
- Deep linking support
- Navigation analytics
- Type-safe arguments enhancement

## Contributing

When adding new features:
1. Follow the patterns documented here
2. Use NavigationCoordinator for navigation
3. Scope ViewModels appropriately
4. Use SavedStateHandle for state
5. Add tests following the examples
6. Update documentation if adding new patterns

## Questions?

Refer to the detailed documentation files in this directory for comprehensive explanations and examples.
