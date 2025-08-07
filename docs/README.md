# Finsible Frontend

A modern Android financial management application built with Jetpack Compose and MVVM architecture. Track your income, expenses, and transfers with intelligent categorization, secure authentication, and real-time synchronization.

## Table of Contents

-   [Features](#features)
-   [Tech Stack](#tech-stack)
-   [Prerequisites](#prerequisites)
-   [Development Setup](#development-setup)
-   [Branching Policy](#branching-policy)
-   [Mock Server Setup](#mock-server-setup)
-   [Project Architecture](#project-architecture)
-   [Contributing](#contributing)
-   [Code Quality Standards](#code-quality-standards)
-   [License](#license)

---

## Features

### Core Functionality

-   **Transaction Management**: Create, edit, and categorize income, expenses, and transfers
-   **Smart Categories**: Color-coded categorization with automatic filtering by transaction type
-   **Secure Authentication**: Google Sign-In integration with JWT token management
-   **Offline-First**: Local ObjectBox database with intelligent server synchronization
-   **Real-time Notifications**: In-app notification system for user feedback

### User Experience

-   **Modern UI**: Clean, intuitive interface built with Jetpack Compose
-   **Dark/Light Theme**: Adaptive theming with custom color system
-   **Responsive Design**: Optimized layouts for different screen sizes
-   **Smooth Animations**: Fluid transitions and micro-interactions

---

## Tech Stack

### Core Framework

-   **Language**: Kotlin 100%
-   **UI Framework**: Jetpack Compose
-   **Architecture**: MVVM with Repository Pattern
-   **Min SDK**: 26 (Android 8.0)
-   **Target SDK**: 34
-   **Compile SDK**: 35

### Key Dependencies

| Category           | Library                    | Version | Purpose               |
| ------------------ | -------------------------- | ------- | --------------------- |
| **DI**             | Dagger Hilt                | Latest  | Dependency injection  |
| **Database**       | ObjectBox                  | Latest  | Local storage         |
| **Networking**     | Retrofit + OkHttp          | Latest  | API communication     |
| **Serialization**  | Kotlinx Serialization      | Latest  | JSON parsing          |
| **Authentication** | Google Credentials API     | Latest  | OAuth integration     |
| **Security**       | EncryptedSharedPreferences | Latest  | Secure storage        |
| **Build**          | KSP                        | Latest  | Annotation processing |

---

## Prerequisites

Before setting up the project, ensure you have:

-   **Android Studio**: Narwhal (2025.1.2) or later
-   **JDK**: 17 or higher
-   **Git**: Latest version
-   **Android SDK**: API levels 26-35
-   **Google Cloud Console**: Access for OAuth setup

## Development Setup

### 1. Clone Repository

```bash
git clone https://github.com/finsible/android-client.git
cd FinsibleFrontend
```

### 2. Google OAuth Setup

-   Create a project in Google Cloud Console
-   Enable Google Sign-In API
-   Configure OAuth consent screen
-   Create OAuth 2.0 credentials
-   Add your debug/release SHA-1 fingerprints

### 3. Configure Secrets

Create `secrets.properties` in the root directory:

```properties
BASE_URL="https://finsible.app"
SERVER_CLIENT_ID="your-google-oauth-client-id"
```

### 4. Build Project

```bash
./gradlew assembleDebug
```

### 5. Run Application

-   Open project in Android Studio
-   Sync Gradle files
-   Run on device/emulator (API 26+)

---

## Branching Policy

### Branch Structure

-   `main`: Production-ready releases only

### Branch Naming Conventions

-   Features: `feature/feature-title`
-   Bug Fixes: `bugfix/bug-title`
-   Enhancements: `enhancement/enhancement-title`

---

## Mock Server Setup

> ⚠️ **Important:** The backend server is not deployed. Use mock responses for development.

### Mock Data Location

All mock JSON responses are stored in:

```
app/src/main/assets/mock_responses/
├── auth/
│   ├── google_signin_success.json
│   └── google_signin_error.json
├── categories/
│   ├── categories_list.json
│   ├── category_add_success.json
│   └── category_rename_success.json
└── transactions/
    ├── transactions_list.json
    └── transaction_create_success.json
```

### Mocking Responses with App Inspection Rules

You can also use **App Inspection Rules** in Android Studio to mock API responses at runtime without modifying your code:

1. **Open App Inspection**: Go to `View > Tool Windows > App Inspection` in Android Studio.
2. **Select Network Inspector**: Choose your running app and open the Network Inspector tab.
3. **Add Rule**: Click the "Rules" tab and add a new rule.
4. **Configure Rule**:
    - Set the URL pattern to match the API endpoint you want to mock.
    - Choose the HTTP method (GET, POST, etc.).
    - Set the response body to your desired mock JSON.
    - Optionally, set the response code and headers.
5. **Enable Rule**: Save and enable the rule. All matching requests will now return your mock response.

This approach allows you to quickly test different scenarios and error cases without changing your backend or app code.

---

## Project Architecture

### Architecture Pattern: MVVM + Repository

```
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│   UI Layer    │   │ Domain Layer  │   │  Data Layer   │
│ • Composables │──▶│ • ViewModels  │──▶│ • Repositories│
│ • Navigation  │   │ • Use Cases   │   │ • DataSources │
│ • Themes      │   │ • Models      │   │ • Entities    │
└───────────────┘   └───────────────┘   └───────────────┘
```

### Directory Structure

```bash
app/src/main/java/com/itsjeel01/finsiblefrontend/
├── common/             # Shared utilities and helpers
├── data/               # Data layer (local, remote, repositories, sync)
│   ├── di/                 # Dependency injection modules
│   ├── local/              # Local data sources
│   │   ├── entity/             # Database entities
│   │   └── repository/         # Local repositories
│   ├── remote/             # Remote data sources
│   │   ├── api/                # API interfaces
│   │   ├── converter/           # Data converters/parsers
│   │   ├── interceptor/         # Network interceptors
│   │   └── model/               # Network models
│   ├── repository/          # Repository implementations
│   └── sync/                # Data synchronization logic
└── ui/                  # Presentation layer (UI)
    ├── component/           # Reusable UI components
    │   └── base/                # Base UI components
    ├── data/                  # UI-specific data classes
    ├── navigation/            # Navigation logic
    ├── screen/                # Screens and feature modules
    ├── theme/                 # Theming and styles
    │   └── dime/                  # Custom color system
    ├── util/                  # UI utilities
    └── viewmodel/             # ViewModels for screens/features
```

### Key Architectural Decisions

#### Data Layer

The data layer uses **ObjectBox** for fast, reliable local storage and follows the **Repository Pattern** to keep data sources cleanly separated. A **cache-first strategy** ensures the app works offline, with background sync keeping everything up to date.

#### Presentation Layer

UI is built with **Jetpack Compose** for a modern, declarative experience. **StateFlow** powers reactive state management in ViewModels, while **Navigation Compose** provides type-safe navigation between screens.

#### Dependency Injection

**Hilt** is used for compile-time dependency injection, improving performance and maintainability. Modules are organized by concern for clarity and scalability.

---

## Contributing

Before you contribute:

-   Search existing issues to avoid duplicates.
-   Check the roadmap for planned features.
-   Review the contribution guidelines.

When submitting issues, please use the right template:

-   **Bug Report**: Include steps to reproduce, expected behavior, and screenshots.
-   **Feature Request**: Describe the use case, acceptance criteria, and provide mockups if possible.
-   **Question**: For architecture, setup, or general queries.

### Pull Requests

To submit a PR:

1. Create a feature branch using the naming conventions.
2. Implement your changes and add tests.
3. Update documentation if needed.
4. Run all tests and ensure checks pass.
5. Submit your PR with a clear, detailed description.

**Checklist for PRs:**

-   [ ] Code follows the style guide.
-   [ ] Documentation is updated.
-   [ ] PR description explains the changes.
-   [ ] Self-review is complete.

---

## Code Quality Standards

-   **Code Style**: Follow official Kotlin conventions and MVVM separation of concerns. Use Hilt for DI and implement robust error handling.
-   **Documentation**: Use KDoc for public APIs, keep the README up to date, and record major architectural decisions.
-   **Testing**: Write unit tests for ViewModels and repositories, integration tests for data flows, and UI tests for critical user journeys.
-   **Commits**: Use the [Conventional Commits](https://www.conventionalcommits.org/) format, write in the imperative mood, and provide detailed commit bodies.

**Example:**

```
Add transaction categorization feature

- Implement category selection dropdown in transaction form
- Add category color coding system
- Update transaction entity with category relationship
- Add category validation in transaction creation flow
```

---

## License

This project is licensed under the GNU General Public License v3.0. See the LICENSE file for details. For more information about GPL v3.0, visit [https://www.gnu.org/licenses/gpl-3.0.html](https://www.gnu.org/licenses/gpl-3.0.html)

## Support

-   **Issues**: GitHub Issues
-   **Questions**: Raise an issue with the "question" label
-   **Maintainer**: [@alph-a07](https://github.com/alph-a07)
