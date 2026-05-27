# Keyboard-First Email Client

A hyper-focused, keyboard-driven desktop email client designed to eliminate UI bloat. It treats emails as tasks to be triaged quickly, supporting multi-account management (Gmail & Zoho) without merging them into a confusing unified inbox.

## 🚀 Product Vision

- **Minimalist Design**: Ultra-minimalist, typography-centric, high-contrast dark theme. Zero floating action buttons or toolbars.
- **Vim-Inspired Navigation**: Movement and triage are handled entirely via the keyboard, keeping your hands on the home row.
- **Offline-First**: Local caching via SQLDelight ensures zero-latency interactions.
- **Multi-Account**: Context switching between workspaces (Gmail, Work, Personal) is seamless.

## 🏗️ Project Architecture

This is a **Kotlin Multiplatform (KMP)** project targeting Android, iOS, Desktop (JVM), and a synchronization backend.

### Modules
- **`composeApp`**: Shared UI code using **Compose Multiplatform**.
    - `commonMain`: Shared UI and logic for all platforms.
    - `jvmMain`: Desktop-specific implementation.
    - `androidMain`: Android-specific implementation.
    - `iosMain`: iOS-specific entry points.
- **`shared`**: Shared data models and business logic (e.g., `NormalizedEmail`).
- **`server`**: A **Ktor**-based synchronization engine that polls third-party APIs (Gmail/Zoho), normalizes data, and serves it to the client.
- **`iosApp`**: The iOS application entry point (SwiftUI).

## ⌨️ Keyboard Navigation Paradigm

The application relies on strict UI focus management to intercept keystrokes without requiring a mouse.

### Movement & Triage
- **j / k**: Move Down / Up in the queue.
- **Enter / l**: Open selected email.
- **Esc / h**: Return to the Queue.
- **e**: Archive / Done (auto-advances).
- **d**: Trash / Delete (auto-advances).
- **s**: Snooze / Defer (auto-advances).

### Composing & Global Actions
- **c**: New Message.
- **r**: Reply.
- **a**: Reply All.
- **Ctrl + Enter**: Send.
- **Ctrl + K**: Open **Command Palette** (Settings, secondary actions).
- **Ctrl + 1/2/3**: Switch Workspaces.
- **?**: Shortcut Cheat Sheet.

## 🛠️ Tech Stack

- **Frontend**: Compose Multiplatform, Kotlin Coroutines, StateFlow.
- **Backend**: Ktor, Koin (DI), SQLDelight/PostgreSQL.
- **Data**: Normalized internal schema for cross-provider compatibility.
- **Security**: OAuth 2.0 flows, OS-native credential management.

## 🚀 Build and Run

### Desktop (JVM)
```shell
./gradlew :composeApp:run
```

### Server (Synchronization Engine)
```shell
./gradlew :server:run
```

### Android
```shell
./gradlew :composeApp:assembleDebug
```

### iOS
1. Open the `/iosApp` directory in Xcode.
2. Build and run from Xcode.

---
Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).
