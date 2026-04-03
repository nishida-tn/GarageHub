# GarageHub

GarageHub is a native Android application designed for workshop management, specifically tailored for the HS Garage ecosystem. Built with modern Android development practices, it focuses on performance, scalability, and maintainability.

## 🚀 Architecture

The project follows **Clean Architecture** principles combined with the **MVI (Model-View-Intent)** pattern in the presentation layer. This ensures a clear separation of concerns:

-   **Domain Layer:** Contains business logic, UseCases, and Repository interfaces. It is pure Kotlin and has no dependencies on the Android framework.
-   **Data Layer:** Handles data retrieval from remote (Retrofit) and local (Room/DataStore) sources, and provides implementation for Domain repositories.
-   **Presentation Layer:** Built entirely with **Jetpack Compose**. It uses ViewModels to manage UI state via `StateFlow` and handles user actions as explicit `Intents`.

## 🛠 Tech Stack

-   **Language:** Kotlin
-   **UI Framework:** Jetpack Compose with Material Design 3
-   **Dependency Injection:** Hilt
-   **Networking:** Retrofit with Kotlinx Serialization
-   **Local Storage:** Room Database and DataStore Preferences
-   **Concurrency:** Kotlin Coroutines & Flow
-   **Navigation:** Jetpack Navigation Compose
-   **Testing:** JUnit 4, MockK, and KotlinX Test Coroutines (TDD approach)

## ✨ Key Features

-   **Authentication:** Complete flow including Login, Signup, Forgot Password, and Reset Password.
-   **Estimate Management:** Create, view, edit, and delete workshop estimates.
-   **Workflow Integration:** Convert estimates into orders and create service demands.
-   **Workshop Settings:** Manage workshop profile, hourly rates, and security settings.
-   **MVI State Management:** Predictable UI state and side-effect handling.

## 📁 Project Structure

```text
app/src/main/java/com/hsgaragepecas/garagehub/
├── di/             # Hilt Dependency Injection modules
├── domain/         # Business logic (UseCases, Domain Models, Repository Interfaces)
├── data/           # Data layer (DTOs, Repository Impls, API Services, Local DB)
└── ui/             # Presentation layer (Compose Screens, ViewModels, MVI Contracts)
    ├── account/    # Login, Signup, Password recovery
    ├── estimate/   # Estimate listing and editing
    ├── settings/   # Workshop settings
    └── common/     # Reusable UI components
```

## 🧪 Testing

The project follows a **Test-Driven Development (TDD)** approach. High test coverage is maintained for the Domain layer and ViewModels.

To run the unit tests:
```bash
./gradlew test
```

## 🛠 Getting Started

1.  Clone the repository.
2.  Open the project in **Android Studio Ladybug** (or newer).
3.  Sync the project with Gradle files.
4.  Run the application on an emulator or physical device.

---
*Developed by Nishida - HS Garage Hub*
