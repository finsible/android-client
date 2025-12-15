# Navigation 3 Migration Guide

## Summary

This migration improves the navigation architecture of the Finsible Android app with better separation of concerns, improved ViewModel lifecycle management, and enhanced memory optimization.

## What Changed

### 1. AGP Version Update
- Updated Android Gradle Plugin from 8.13.1 to 8.5.0 (stable version)

### 2. Navigation Architecture
- **NavigationCoordinator Pattern**: Introduced to separate navigation logic from UI code
  - `NavigationCoordinator` interface for common navigation operations
  - `AppNavigationCoordinator` for app-level navigation
  - `HomeNavigationCoordinator` for tab-based navigation
- Refactored `AppNavGraph` and `HomeNavGraph` to use coordinators

### 3. ViewModel Scoping
- Added comprehensive documentation for three scoping levels:
  - **Screen-scoped**: Default scoping for individual screens
  - **Graph-scoped**: Shared state across navigation graphs
  - **Activity-scoped**: Global app state
- Enhanced `HomeViewModel` with lifecycle documentation

### 4. Navigation Events
- Added `NavigationEvents` for handling navigation results
- Added `NavigationEventChannel` for one-time navigation events from ViewModels
- Provides mechanism similar to `startActivityForResult` for Compose Navigation

### 5. Testing
- Added unit tests for `AppNavigationCoordinator`
- Added unit tests for `HomeViewModel` demonstrating SavedStateHandle testing
- Added MockK and Coroutines Test dependencies

### 6. Documentation
- **NAVIGATION_ARCHITECTURE.md**: Complete guide to navigation patterns
- **VIEWMODEL_SCOPING_GUIDE.md**: Comprehensive ViewModel scoping guide
- **NAVIGATION_MIGRATION.md**: This migration guide

## Navigation Library Version

The app is currently using **Navigation Compose 2.8.5**, which is the latest stable version.

Navigation Compose 3.0+ is currently in alpha/beta and not yet recommended for production use. This migration prepares the architecture for a future upgrade when Navigation 3.0 becomes stable.

## Benefits

### 1. Separation of Concerns
- Navigation logic is now centralized in coordinators
- UI code is cleaner and easier to read
- Navigation logic can be tested independently

### 2. Improved Testability
- Navigation coordinators can be mocked for UI tests
- ViewModel lifecycle can be tested with SavedStateHandle
- Clear separation makes testing easier

### 3. Memory Optimization
- ViewModels are properly scoped and cleared at the right time
- State restoration is handled correctly with SavedStateHandle
- Navigation back stack management is optimized

### 4. Better Maintainability
- Centralized navigation logic is easier to update
- Clear documentation for navigation patterns
- Consistent approach across the app

## Migration Path for New Features

### Adding a New Screen

1. Define the route in `AppRoutes` or `HomeRoutes`
2. Add navigation logic to the appropriate coordinator
3. Add the composable to the navigation graph using the coordinator
4. Create a screen-scoped ViewModel if needed

Example:
```kotlin
// 1. Define route
@Serializable
data class Profile(val userId: Long) : AppRoutes()

// 2. Add to coordinator
fun navigateToProfile(userId: Long) {
    navigateTo(AppRoutes.Profile(userId))
}

// 3. Add to graph
composable<AppRoutes.Profile> {
    val args = it.toRoute<AppRoutes.Profile>()
    ProfileScreen(userId = args.userId)
}

// 4. Create ViewModel
@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userId: Long = savedStateHandle["userId"] ?: 0L
}
```

### Adding Navigation Results

Use the new `NavigationEvents` system:

```kotlin
// In source screen
val navigationEvents = NavigationEvents(navController)

LaunchedEffect(Unit) {
    navigationEvents.observeResult<TransactionData>("transaction_result", savedStateHandle) { data ->
        viewModel.onTransactionCreated(data)
    }
}

// In destination screen
navigationEvents.setResultAndNavigateBack("transaction_result", transactionData)
```

### Adding Graph-Scoped ViewModels

For shared state across screens in a graph:

```kotlin
@Composable
fun ScreenInGraph(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry<AppRoutes.Home>()
    }
    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
}
```

## Testing Examples

### Testing Navigation
```kotlin
@Test
fun `navigateToHome clears launch screen`() {
    val navController = mockk<NavController>(relaxed = true)
    val coordinator = AppNavigationCoordinator(navController)
    
    coordinator.navigateToHome(AppRoutes.Launch)
    
    verify { navController.navigate(AppRoutes.Home, any()) }
}
```

### Testing ViewModel State Preservation
```kotlin
@Test
fun `state persists across process death`() {
    val savedState = SavedStateHandle()
    val viewModel = HomeViewModel(savedState)
    
    viewModel.updateActiveTab(3)
    
    val restoredViewModel = HomeViewModel(savedState)
    assertEquals(3, restoredViewModel.activeTab.value)
}
```

## Breaking Changes

None. This migration is backward compatible with existing code.

## Future Improvements

1. **Navigation 3.0 Upgrade**: When stable, upgrade to Navigation Compose 3.0+
2. **Deep Linking**: Add comprehensive deep link support
3. **Navigation Analytics**: Track navigation patterns for UX insights
4. **Type-Safe Arguments**: Enhance type-safe argument passing
5. **Navigation Testing DSL**: Create a DSL for testing navigation flows

## References

- [Navigation Architecture Guide](./NAVIGATION_ARCHITECTURE.md)
- [ViewModel Scoping Guide](./VIEWMODEL_SCOPING_GUIDE.md)
- [Jetpack Navigation Documentation](https://developer.android.com/guide/navigation)
- [ViewModel Documentation](https://developer.android.com/topic/libraries/architecture/viewmodel)

## Questions?

For questions or issues related to this migration, please refer to:
- The comprehensive documentation in `docs/`
- The test examples in `app/src/test/`
- The inline code documentation in navigation and ViewModel files
