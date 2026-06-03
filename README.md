[![Codemagic build status](https://api.codemagic.io/apps/6614f55815b3f9356c1334e7/prepare_prod_release/status_badge.svg)](https://codemagic.io/apps/6614f55815b3f9356c1334e7/prepare_prod_release/latest_build)

# MVI Compose Architecture Template

An Android project template implementing **MVI (Model-View-Intent)** architecture with **Jetpack Compose** for declarative UI. Provides a production-ready foundation with built-in support for navigation, theming, encrypted storage, networking, and build flavors.

---

## 📚 Table of Contents

- [✨ Features](#-features)
- [🚀 Quickstart](#-quickstart)
- [🧱 Project Structure](#-project-structure)
- [🛠 Gradle Commands](#-gradle-commands)
- [⚙️ Codemagic Integration](#️-codemagic-integration)
- [🔗 Related Projects](#-related-projects)

---

## ✨ Features

- **MVI Architecture** — unidirectional data flow with clear separation of concerns
- **Jetpack Compose** — fully declarative UI with a custom design system
- **Navigation** — type-safe Compose navigation setup
- **Encrypted DataStore** — secure local storage using [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore) and [Tink](https://developers.google.com/tink)
- **Networking** — [Retrofit](https://square.github.io/retrofit/)-based API layer
- **Build Flavors** — pre-configured product flavors for multi-environment builds
- **Static Analysis** — [Detekt](https://detekt.dev/) with Git hooks integration
- **CI/CD** — Codemagic build pipeline

---

## 💾 UI State Persistence

Automatic UI state preservation via `SavedStateHandle`, ensuring seamless recovery from process death, permission-related activity recreation, and system-initiated background termination.

### Overview
- **Automatic state persistence:** `BaseViewModel` persists UI state to `SavedStateHandle` on every state update and restores it transparently on ViewModel recreation
- **Parcelable state classes:** all screen state data classes implement `@Parcelize` / `Parcelable` for serialization into the saved state bundle
- **Process death detection:** `isProcessDeathRestoration` flag enables conditional logic for screens that cannot fully reconstruct runtime state (e.g. PDF renderers, media players)
- **SubViewModel state restoration:** `SubViewModelEntry` provides declarative state extraction, enabling automatic SubViewModel restoration from the parent's persisted state
- **Parcelable StringResource:** `StringResource` implements manual `Parcelable` serialization, preserving localized and formatted strings across process boundaries

### Implementation
1. `BaseViewModel` accepts `SavedStateHandle` and persists `STATE` under a dedicated key on every `updateUiState` call
2. On recreation, the state is restored from `SavedStateHandle` before falling back to the provided `initialState`
3. SubViewModels registered via `SubViewModelEntry` receive their restored state during scope attachment

### Covered Scenarios
- System-initiated process termination (low memory conditions)
- Activity recreation triggered by runtime permission dialogs
- Configuration changes (screen rotation, locale switch, dark mode toggle)
- Developer option "Don't keep activities" enabled

---

## 🚀 Quickstart

1. Clone this repository
2. Remove the boilerplate Git history (`rm -rf .git && git init`)
3. Rename the project and update `applicationId`
4. Configure environment variables and build flavors
5. Install Git hooks:
   ```bash
   ./gradlew installGitHooks
   ```
6. Build and run:
   ```bash
   ./gradlew assembleDebug
   ```
7. *(Optional)* Run static analysis:
   ```bash
   ./gradlew detekt
   ```

---

## 🧱 Project Structure

The boilerplate follows a **single-module, feature-based** structure inspired by Clean Architecture.

### Data Layer
- `data/common/` — shared utilities
- `data/features/` — feature-specific data sources, integrations, and repositories
- `data/local/` — local storage (DataStore)
- `data/remote/` — network helpers (interceptors, loggers)

### Domain Layer
- `domain/base/` — base classes and contracts
- `domain/features/` — feature-specific use cases and business rules

### DI
- `di/` — dependency injection modules organized by feature

### UI Layer
- `ui/base/`, `ui/core/` — base UI components and logic
- `ui/navigation/` — navigation graph and routes
- `ui/theme/` — design system (colors, typography, components)

### Utils
- `utils/` — shared extensions and helpers

---

## 🛠 Gradle Commands

```bash
./gradlew installGitHooks       # Install Git hooks for Detekt
./gradlew deleteGitHooks        # Remove Git hooks
./gradlew detekt                # Run static analysis
```

---

## ⚙️ Codemagic Integration

To update the Gradle wrapper for Codemagic compatibility:

```bash
./gradlew wrapper --gradle-version <version>
chmod +x gradlew
```

Commit and push `gradlew` and `gradle-wrapper.properties`.

---

## 🔗 Related Projects

- **[android-db-encryption](https://github.com/ninetwothree/android-db-encryption)** — Android database encryption implementation. A companion project demonstrating secure local database storage with encryption at rest.

---
