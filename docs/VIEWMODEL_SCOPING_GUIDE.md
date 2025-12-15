# ViewModel Scoping Guide

## Overview

This guide explains how to properly scope ViewModels in the Finsible app to ensure optimal memory management, state preservation, and separation of concerns.

## ViewModel Scoping Levels

### 1. Screen-Scoped ViewModels (Default)

**When to use:**
- Screen-specific state that should be cleared when navigating away
- Temporary form data that doesn't need to persist
- UI state for a single destination

**How to implement:**
```kotlin
@Composable
fun MyScreen() {
    val viewModel: MyViewModel = hiltViewModel()
    // ViewModel lifecycle is tied to this composable
}
```

**Examples in Finsible:**
- `HomeViewModel` - Tab navigation state (scoped to HomeScreen)
- `NewTransactionViewModel` - Transaction entry state (scoped to NewTransactionTab)
- `OnboardingViewModel` - Login/signup state (scoped to OnboardingScreen)

**Lifecycle:**
- Created when composable first enters composition
- Cleared when destination is removed from back stack
- State can be preserved using SavedStateHandle for process death

### 2. Graph-Scoped ViewModels

**When to use:**
- Shared state between multiple destinations in a navigation graph
- Multi-step flows where data needs to persist across screens
- Parent-child screen communication

**How to implement:**
```kotlin
@Composable
fun MyScreen(navController: NavController) {
    // Get the parent NavBackStackEntry for the graph
    val parentEntry = remember(navController) {
        navController.getBackStackEntry<MyGraphRoute>()
    }
    
    // ViewModel is scoped to the graph, shared across all destinations
    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
}
```

**Example use case:**
```kotlin
// Shared across all tabs in HomeScreen
@Composable
fun DashboardTab(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry<AppRoutes.Home>()
    }
    val homeSharedViewModel: HomeSharedViewModel = hiltViewModel(parentEntry)
    
    // Access shared state across all home tabs
    val sharedData by homeSharedViewModel.sharedData.collectAsStateWithLifecycle()
}

@Composable
fun AnalyticsTab(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry<AppRoutes.Home>()
    }
    val homeSharedViewModel: HomeSharedViewModel = hiltViewModel(parentEntry)
    
    // Same ViewModel instance, same state
    val sharedData by homeSharedViewModel.sharedData.collectAsStateWithLifecycle()
}
```

**Lifecycle:**
- Created when first screen in the graph is navigated to
- Survives navigation between destinations within the graph
- Cleared when the graph's root destination is removed from back stack

### 3. Activity-Scoped ViewModels

**When to use:**
- Global app state (authentication, user preferences)
- State that must survive all navigation changes
- Cross-feature communication

**How to implement:**
```kotlin
@Composable
fun MyScreen() {
    val activity = LocalContext.current as ComponentActivity
    val appViewModel: AppViewModel = hiltViewModel(activity)
}
```

**Example use case:**
```kotlin
// AuthViewModel scoped to MainActivity
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
}

// Usage in any screen
@Composable
fun AnyScreen() {
    val activity = LocalContext.current as ComponentActivity
    val authViewModel: AuthViewModel = hiltViewModel(activity)
    
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    
    // Auth state is shared across entire app
}
```

**Lifecycle:**
- Created with the Activity
- Survives all navigation changes
- Cleared only when Activity is destroyed

## SavedStateHandle Best Practices

### 1. Always Use SavedStateHandle for UI State

SavedStateHandle ensures state survives process death (e.g., low memory, configuration changes):

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val STATE_KEY = "ui_state"
    }
    
    private val _uiState = MutableStateFlow(
        savedStateHandle.get<UiState>(STATE_KEY) ?: UiState.Initial
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun updateState(newState: UiState) {
        _uiState.value = newState
        savedStateHandle[STATE_KEY] = newState
    }
}
```

### 2. Save State on Every Update

Don't wait for onCleared() - save state immediately:

```kotlin
// ✓ Good - State saved immediately
fun selectCategory(categoryId: Long) {
    _selectedCategory.value = categoryId
    savedStateHandle[SELECTED_CATEGORY_KEY] = categoryId
}

// ✗ Bad - State lost if process dies before onCleared()
override fun onCleared() {
    super.onCleared()
    savedStateHandle[SELECTED_CATEGORY_KEY] = _selectedCategory.value
}
```

### 3. Use Type-Safe Keys

```kotlin
companion object {
    private const val SELECTED_TAB_KEY = "selected_tab"
    private const val USER_NAME_KEY = "user_name"
    private const val IS_LOADING_KEY = "is_loading"
}
```

## Memory Optimization Patterns

### 1. Proper ViewModel Clearing

ViewModels are cleared automatically when their scope is removed:

```kotlin
// Navigation that clears ViewModels
navController.navigate(AppRoutes.Home) {
    popUpTo<AppRoutes.Launch> { 
        inclusive = true  // Launch and its ViewModel are cleared
    }
}
```

### 2. State Preservation with launchSingleTop

Reuse existing destinations to preserve state:

```kotlin
// Tab navigation - reuses existing tab instances
navController.navigate(HomeRoutes.Dashboard) {
    popUpTo(navController.graph.startDestinationId) {
        saveState = true  // Save ViewModel state
        inclusive = false
    }
    launchSingleTop = true  // Reuse existing destination
    restoreState = true     // Restore ViewModel state
}
```

### 3. Avoid Memory Leaks

```kotlin
// ✓ Good - viewModelScope automatically cancelled
@HiltViewModel
class MyViewModel @Inject constructor() : ViewModel() {
    init {
        viewModelScope.launch {
            // Coroutine cancelled when ViewModel cleared
        }
    }
}

// ✗ Bad - GlobalScope leaks
class MyViewModel @Inject constructor() : ViewModel() {
    init {
        GlobalScope.launch {
            // Coroutine never cancelled!
        }
    }
}
```

## Common Patterns

### Pattern 1: Tab Navigation State

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _activeTab = MutableStateFlow(
        savedStateHandle.get<Int>("active_tab") ?: 0
    )
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()
    
    fun updateActiveTab(index: Int) {
        _activeTab.value = index
        savedStateHandle["active_tab"] = index
    }
}
```

### Pattern 2: Multi-Step Form

```kotlin
@HiltViewModel
class NewTransactionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _currentStep = MutableStateFlow(
        savedStateHandle.get<TransactionStep>("current_step") ?: TransactionStep.AMOUNT
    )
    val currentStep: StateFlow<TransactionStep> = _currentStep.asStateFlow()
    
    private val _transactionData = MutableStateFlow(
        savedStateHandle.get<TransactionData>("transaction_data") ?: TransactionData()
    )
    val transactionData: StateFlow<TransactionData> = _transactionData.asStateFlow()
    
    fun nextStep() {
        val next = when (_currentStep.value) {
            TransactionStep.AMOUNT -> TransactionStep.CATEGORY
            TransactionStep.CATEGORY -> TransactionStep.DETAILS
            TransactionStep.DETAILS -> TransactionStep.CONFIRM
            TransactionStep.CONFIRM -> return
        }
        _currentStep.value = next
        savedStateHandle["current_step"] = next
    }
    
    fun updateAmount(amount: Double) {
        _transactionData.value = _transactionData.value.copy(amount = amount)
        savedStateHandle["transaction_data"] = _transactionData.value
    }
}
```

### Pattern 3: Shared State Between Tabs

```kotlin
@HiltViewModel
class HomeSharedViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount: StateFlow<Account?> = _selectedAccount.asStateFlow()
    
    fun selectAccount(account: Account) {
        _selectedAccount.value = account
    }
}

// Usage in DashboardTab
@Composable
fun DashboardTab(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry<AppRoutes.Home>()
    }
    val sharedViewModel: HomeSharedViewModel = hiltViewModel(parentEntry)
    
    // Can update selected account
    val accounts by sharedViewModel.accounts.collectAsStateWithLifecycle()
}

// Usage in BalanceTab
@Composable
fun BalanceTab(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry<AppRoutes.Home>()
    }
    val sharedViewModel: HomeSharedViewModel = hiltViewModel(parentEntry)
    
    // Sees the same selected account
    val selectedAccount by sharedViewModel.selectedAccount.collectAsStateWithLifecycle()
}
```

## Testing ViewModel Scoping

### Test 1: State Preservation

```kotlin
@Test
fun `state persists across process death`() {
    val savedState = SavedStateHandle()
    val viewModel = HomeViewModel(savedState)
    
    viewModel.updateActiveTab(3)
    
    // Simulate process death
    val restoredViewModel = HomeViewModel(savedState)
    
    assertEquals(3, restoredViewModel.activeTab.value)
}
```

### Test 2: ViewModel Clearing

```kotlin
@Test
fun `ViewModel cleared when destination removed`() {
    val navController = TestNavHostController(context)
    navController.setGraph(R.navigation.nav_graph)
    
    // Navigate to screen with ViewModel
    navController.navigate("screen_a")
    val viewModel = navController.getViewModelStoreOwner("screen_a")
        .viewModelStore.get("key")
    
    // Navigate away with popUpTo inclusive
    navController.navigate("screen_b") {
        popUpTo("screen_a") { inclusive = true }
    }
    
    // ViewModel should be cleared
    assertTrue(viewModel.isCleared)
}
```

## Troubleshooting

### Problem: State Lost on Process Death

**Solution:** Use SavedStateHandle for all UI state

### Problem: ViewModel Surviving Too Long

**Solution:** Check navigation popUpTo configuration - ensure `inclusive = true` when clearing

### Problem: Shared State Not Working

**Solution:** Ensure both composables use the same NavBackStackEntry

### Problem: Memory Leak

**Solution:** Use viewModelScope, not GlobalScope or manual coroutines

## Checklist for New ViewModels

- [ ] Inject SavedStateHandle in constructor
- [ ] Use SavedStateHandle for all UI state
- [ ] Expose StateFlow, not MutableStateFlow
- [ ] Use viewModelScope for coroutines
- [ ] Add lifecycle scope documentation in KDoc
- [ ] Consider appropriate scoping (screen/graph/activity)
- [ ] Write tests for state preservation
- [ ] Verify ViewModel is cleared at appropriate time

## References

- [ViewModel Overview](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate)
- [ViewModel Scoping](https://developer.android.com/topic/libraries/architecture/viewmodel#sharing)
