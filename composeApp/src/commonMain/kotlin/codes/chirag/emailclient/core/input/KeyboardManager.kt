package codes.chirag.emailclient.core.input

import androidx.compose.ui.input.key.KeyEvent
import codes.chirag.emailclient.core.domain.GlobalState

/**
 * Result of a keyboard event handling attempt.
 */
sealed class KeyResult {
    data class Handled(val newState: GlobalState) : KeyResult()
    object Ignored : KeyResult()
}

/**
 * Interface for feature-specific keyboard handlers.
 */
interface ShortcutHandler {
    fun handle(event: KeyEvent, state: GlobalState): KeyResult
}

/**
 * Orchestrates keyboard shortcuts across different features based on app state.
 */
object KeyboardManager {
    private val mailHandler = codes.chirag.emailclient.features.mail.MailShortcutHandler()
    private val composeHandler = codes.chirag.emailclient.features.compose.ComposeShortcutHandler()

    fun handleEvent(event: KeyEvent, state: GlobalState): GlobalState {
        // Order of precedence matters:
        // 1. Compose shortcuts if in compose mode
        // 2. Mail navigation shortcuts
        
        val composeResult = composeHandler.handle(event, state)
        if (composeResult is KeyResult.Handled) return composeResult.newState
        
        val mailResult = mailHandler.handle(event, state)
        if (mailResult is KeyResult.Handled) return mailResult.newState
        
        return state
    }
}
