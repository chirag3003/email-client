package codes.chirag.emailclient.features.mail

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import codes.chirag.emailclient.core.domain.AppMode
import codes.chirag.emailclient.core.domain.GlobalState
import codes.chirag.emailclient.core.input.KeyResult
import codes.chirag.emailclient.core.input.ShortcutHandler

class MailShortcutHandler : ShortcutHandler {
    override fun handle(event: KeyEvent, state: GlobalState): KeyResult {
        if (event.type != KeyEventType.KeyDown) return KeyResult.Ignored
        
        // Don't intercept global shortcuts if we're in compose or command mode
        if (state.isComposing || state.currentMode == AppMode.COMMAND_PALETTE) return KeyResult.Ignored

        val displayedEmails = state.emails.filter { it.folder == state.activeFolder }
        
        return when (event.key) {
            Key.J -> {
                val currentIndex = displayedEmails.indexOfFirst { it.internalId == state.activeEmailId }
                val nextIndex = if (currentIndex < displayedEmails.size - 1) currentIndex + 1 else currentIndex
                val nextEmailId = if (currentIndex == -1 && displayedEmails.isNotEmpty()) {
                    displayedEmails[0].internalId
                } else {
                    displayedEmails.getOrNull(nextIndex)?.internalId
                }
                
                if (nextEmailId != null) {
                    KeyResult.Handled(state.copy(activeEmailId = nextEmailId))
                } else {
                    KeyResult.Ignored
                }
            }
            Key.K -> {
                val currentIndex = displayedEmails.indexOfFirst { it.internalId == state.activeEmailId }
                val prevIndex = if (currentIndex > 0) currentIndex - 1 else 0
                val prevEmailId = if (currentIndex == -1 && displayedEmails.isNotEmpty()) {
                    displayedEmails[0].internalId
                } else {
                    displayedEmails.getOrNull(prevIndex)?.internalId
                }
                
                if (prevEmailId != null) {
                    KeyResult.Handled(state.copy(activeEmailId = prevEmailId))
                } else {
                    KeyResult.Ignored
                }
            }
            Key.C -> {
                KeyResult.Handled(state.copy(isComposing = true, activeEmailId = null))
            }
            Key.Escape -> {
                if (state.activeEmailId != null) {
                    KeyResult.Handled(state.copy(activeEmailId = null))
                } else {
                    KeyResult.Ignored
                }
            }
            else -> KeyResult.Ignored
        }
    }
}
