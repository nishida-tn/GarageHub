# Project Overview
Android project named **GarageHub**.
Native app built with **Kotlin**, **Jetpack Compose**, adhering to **Clean Architecture**, **Clean Code**, and **TDD**.
Monolith in the `app` module. Separation is enforced through package structure.

# Tech Stack
- UI: Jetpack Compose, Material Design 3
- Architecture Pattern: MVI (Model-View-Intent)
- Navigation: Jetpack Navigation Compose
- Async: Coroutines & Flow
- DI: Hilt
- Testing: JUnit, MockK (Test-Driven approach required)

# Project Structure
app/src/main/java/com/hsgaragepecas/garagehub/
├── di/           # Hilt modules
├── domain/       # UseCases, Models, Repository Interfaces (NO ANDROID IMPORTS)
├── data/         # Repository Impls, DTOs, Local/Remote Data Sources
└── presentation/ # Compose Screens, ViewModels (MVI), Contracts (State/Intent/Effect), Navigation Routes

# Code Standards & AI Rules

## 1. TDD & Testing
- Write or suggest unit tests BEFORE the implementation.
- High test coverage for `domain` layer and ViewModels.

## 2. Jetpack Compose & MVI (UI)
- Use ONLY Jetpack Compose. NO XML.
- Strictly follow the **MVI (Model-View-Intent)** pattern for the Presentation layer:
    - **State:** ViewModels must expose a single UI state via `StateFlow`.
    - **Intent (Event):** All user actions from the UI must be sent to the ViewModel as explicit Intents/Events.
    - **Effect (Side Effect):** One-time actions (like navigation, snackbars, or toasts) must be handled via `Channel` or `SharedFlow`.
- Use **Navigation Compose** for all app routing. Ensure routes are clearly defined and separated from the UI components.
- For navigation, always follow the pattern used in the `LoginNavigation.kt` file, which includes passing the `navController` to the screen's composable in the navigation graph.
- Follow State Hoisting. Keep UI components stateless where possible.
- Include `@Preview` for all new UI components.

## 3. Clean Code & Kotlin
- Favor immutability (`val`) and clear naming.
- Use `Result<T>` or `sealed interface` for error handling in the Domain layer.
- Do not leave commented-out code.

## 4. Documentation
- All new files, classes, and public methods must have KDoc documentation.
