Keyboard-First Email Client
1. Product Vision

A hyper-focused, keyboard-driven desktop email client designed to eliminate UI bloat. It treats emails as tasks to be triaged quickly, supporting multi-account management (Gmail & Zoho) without merging them into a confusing unified inbox.

2. UI/UX & Global Layout

    Aesthetic: Ultra-minimalist, typography-centric, high-contrast dark theme. Zero floating action buttons, toolbars, or ecosystem banners.

    Layout: A rigid 3-pane desktop architecture.

        Workspace Bar (Left): Vertical strip for context switching between accounts.

        The Queue (Middle): The list of active email tasks (Sender, Subject, 1-line snippet).

        The Content Pane (Right): The primary area for reading or composing.

    The Command Palette: A centered modal triggered via shortcut to handle all secondary actions (e.g., "Empty Trash", "Toggle Dark Mode"), replacing traditional settings menus.

3. Keyboard Navigation Paradigm (Vim-Inspired)

The application relies on strict UI focus management to intercept keystrokes without requiring a mouse.

    Movement: j (down), k (up), Enter/l (open email), Esc/h (return to Queue).

    Triage (Auto-advances to next item): e (Archive/Done), d (Trash), s (Snooze/Defer).

    Composing: c (New), r (Reply), a (Reply All), Ctrl + Enter (Send).

    Global Control: Ctrl + K (Command Palette), Ctrl + 1/Ctrl + 2 (Switch Workspaces), ? (Shortcut Cheat Sheet).

4. Core Integrations & Implementation Architecture

To ensure the desktop client remains blazing fast and the offline experience is seamless, the architecture is split between a high-performance frontend and a robust Kotlin-based synchronization engine.

A. The Backend Synchronization Engine

Instead of the desktop client directly polling third-party APIs constantly, a dedicated backend handles the heavy lifting.

    Implementation: The backend will be written in Kotlin (leveraging Ktor or similar frameworks). Using Kotlin allows for shared data models between the client and the synchronization engine.

    Data Normalization: The service will fetch raw JSON payloads from the Gmail API and Zoho Mail API, parse complex thread histories, and normalize them into a single, clean internal data structure.

    Database: The normalized email data, user preferences, and metadata will be stored in a SQLDelight or Postgres database. This acts as the single source of truth and allows for complex querying before data ever reaches the client.

B. The Frontend Client (Kotlin Multiplatform)

The desktop application acts as a high-performance, offline-first viewer for the synchronized data.

    Implementation: Built using Kotlin Multiplatform (KMP) and Compose Desktop to ensure native performance across macOS, Windows, and Linux.

    State Management: The app will use a centralized finite state machine powered by Kotlin Coroutines and StateFlow. This ensures the UI instantly reacts to keyboard commands without focus-trapping bugs.

    Local Caching: A local database (SQLDelight) will cache the normalized data. When the user hits j or k, the UI reads directly from this local cache, guaranteeing zero latency.

C. Email Rendering (WebView Integration)

Rendering arbitrary HTML safely while maintaining the minimalist dark theme requires strict controls.

    Implementation: Using the expect/actual KMP pattern, the app will bind to the OS-native browser engines (WKWebView for macOS, WebView2 for Windows).

    Sanitization: * JavaScript execution must be strictly disabled within the WebView to prevent tracking pixels and UI lockups.

        The client will intercept the HTML payload and inject a custom CSS stylesheet to force dark mode, standardize typography, and strip out overly complex layouts before rendering it on the screen.

    Link Handling: External links clicked within an email will be intercepted and delegated to the user's default system browser.

D. Authentication Flow

    Implementation: The client will utilize standard OAuth 2.0 flows. A secure browser window will open to authenticate with Google or Zoho.

    Token Management: Tokens will be securely encrypted and stored locally via the OS's native credential manager and securely transmitted to the synchronization engine.
