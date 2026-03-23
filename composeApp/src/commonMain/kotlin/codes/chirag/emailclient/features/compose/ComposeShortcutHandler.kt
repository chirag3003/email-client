package codes.chirag.emailclient.features.compose

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import codes.chirag.emailclient.core.domain.GlobalState
import codes.chirag.emailclient.core.input.KeyResult
import codes.chirag.emailclient.core.input.ShortcutHandler

class ComposeShortcutHandler : ShortcutHandler {
    override fun handle(event: KeyEvent, state: GlobalState): KeyResult {
        if (!state.isComposing) return KeyResult.Ignored
        if (event.type != KeyEventType.KeyDown) return KeyResult.Ignored
        
        return when (event.key) {
            Key.Escape -> {
                // In COMPOSE_MODE, Esc closes the editor
                KeyResult.Handled(state.copy(isComposing = false, activeEmailId = null))
            }
            // Add Ctrl + Enter (Send) in next iteration when focus management refined for fields
            else -> KeyResult.Ignored
        }
    }
}
