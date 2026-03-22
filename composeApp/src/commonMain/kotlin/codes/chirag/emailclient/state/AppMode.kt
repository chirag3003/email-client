package codes.chirag.emailclient.state

enum class AppMode {
    QUEUE_NAVIGATION, // j, k, Enter active
    READING_VIEW,     // Esc, r, f active
    COMPOSE_MODE,     // Typing active, shortcuts disabled
    COMMAND_PALETTE   // Overlay active, fuzzy search active
}