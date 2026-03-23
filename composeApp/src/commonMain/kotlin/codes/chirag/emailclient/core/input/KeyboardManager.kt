package codes.chirag.emailclient.core.input

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import codes.chirag.emailclient.shared.model.FolderType
import codes.chirag.emailclient.core.domain.GlobalState
import codes.chirag.emailclient.features.commands.CommandShortcutHandler
import codes.chirag.emailclient.features.compose.ComposeShortcutHandler
import codes.chirag.emailclient.features.mail.MailShortcutHandler

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
class KeyboardManager(
    private val onExecuteCommand: (GlobalState) -> GlobalState
) {
    private val commandHandler = CommandShortcutHandler(onExecuteCommand)
    private val mailHandler = MailShortcutHandler()
    private val composeHandler = ComposeShortcutHandler()

    private var activePrefixKey: Key? = null

    fun handleEvent(event: KeyEvent, state: GlobalState): GlobalState {
        if (event.type != KeyEventType.KeyDown) return state

        // Prefix logic
        if (activePrefixKey == null) {
            if (event.key == Key.G) {
                activePrefixKey = Key.G
                return state // Wait for next key
            }
        } else {
            val prefix = activePrefixKey
            activePrefixKey = null // Clear for next try
            
            if (prefix == Key.G) {
                // Handle 'g' sequences
                return when (event.key) {
                    Key.I -> state.copy(activeFolder = FolderType.INBOX, activeEmailId = null)
                    Key.S -> state.copy(activeFolder = FolderType.SENT, activeEmailId = null)
                    Key.D -> state.copy(activeFolder = FolderType.DRAFTS, activeEmailId = null)
                    Key.T -> state.copy(activeFolder = FolderType.TRASH, activeEmailId = null)
                    Key.A -> state.copy(activeFolder = FolderType.ARCHIVE, activeEmailId = null)
                    else -> state // Invalid sequence, ignore
                }
            }
        }

        // Standard single-key handlers
        val commandResult = commandHandler.handle(event, state)
        if (commandResult is KeyResult.Handled) return commandResult.newState

        val composeResult = composeHandler.handle(event, state)
        if (composeResult is KeyResult.Handled) return composeResult.newState
        
        val mailResult = mailHandler.handle(event, state)
        if (mailResult is KeyResult.Handled) return mailResult.newState
        
        return state
    }
}
