# Navigation Architecture Guide

## Overview

This document describes the navigation architecture of the Finsible Android app, including navigation patterns, ViewModel lifecycle management, and best practices for memory optimization.

## Architecture Principles

### 1. Separation of Concerns

Navigation logic is separated from UI code using the **NavigationCoordinator** pattern:

- **NavigationCoordinator Interface**: Defines navigation operations
- **AppNavigationCoordinator**: Handles app-level navigation (Launch → Onboarding → Home)
- **HomeNavigationCoordinator**: Handles tab-based navigation within the Home screen

### 2. ViewModel Scoping

ViewModels are scoped based on their lifecycle requirements:

#### Screen-Scoped ViewModels
Used for state that should be cleared when navigating away from a screen.

#### Graph-Scoped ViewModels
Used for state that should be shared across screens within a navigation graph.

#### Activity-Scoped ViewModels
Used for state that should persist across the entire app lifecycle.

### 3. Memory Optimization

Navigation uses `saveState` and `restoreState` for efficient memory usage.

## Navigation Structure

### App-Level Navigation

Launch Screen → Onboarding Screen → Home Screen (with nested navigation)

### Home-Level Navigation

Dashboard Tab, Analytics Tab, New Transaction Tab (modal), Balance Tab, Settings Tab

## Best Practices

1. Always Use SavedStateHandle for UI State
2. Expose StateFlow, Not MutableStateFlow
3. Use NavigationCoordinator for Navigation
4. Scope ViewModels Appropriately

## References

- [Jetpack Navigation Documentation](https://developer.android.com/guide/navigation)
- [ViewModel Scoping](https://developer.android.com/topic/libraries/architecture/viewmodel#sharing)
- [SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate)
