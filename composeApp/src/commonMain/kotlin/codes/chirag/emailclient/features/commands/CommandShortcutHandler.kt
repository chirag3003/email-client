package codes.chirag.emailclient.features.commands

import androidx.compose.ui.input.key.*
import codes.chirag.emailclient.core.domain.AppMode
import codes.chirag.emailclient.core.domain.GlobalState
import codes.chirag.emailclient.core.input.KeyResult
import codes.chirag.emailclient.core.input.ShortcutHandler

class CommandShortcutHandler(
    private val onExecuteCommand: (GlobalState) -> GlobalState
) : ShortcutHandler {
    override fun handle(event: KeyEvent, state: GlobalState): KeyResult {
        // Global trigger for Ctrl+K
        if (event.type == KeyEventType.KeyDown && 
            event.key == Key.K && 
            (event.isCtrlPressed || event.isMetaPressed)) {
            return if (state.currentMode == AppMode.COMMAND_PALETTE) {
                KeyResult.Handled(state.copy(currentMode = AppMode.QUEUE_NAVIGATION, commandQuery = "", selectedCommandIndex = 0))
            } else {
                KeyResult.Handled(state.copy(currentMode = AppMode.COMMAND_PALETTE, commandQuery = "", selectedCommandIndex = 0, isCheatsheetVisible = false))
            }
        }

        if (state.isCheatsheetVisible && event.type == KeyEventType.KeyDown && event.key == Key.Escape) {
            return KeyResult.Handled(state.copy(isCheatsheetVisible = false))
        }

        if (state.currentMode != AppMode.COMMAND_PALETTE) return KeyResult.Ignored
        if (event.type != KeyEventType.KeyDown) return KeyResult.Ignored

        return when (event.key) {
            Key.Escape -> {
                if (state.isCheatsheetVisible) {
                    KeyResult.Handled(state.copy(isCheatsheetVisible = false))
                } else if (state.currentMode == AppMode.COMMAND_PALETTE) {
                    KeyResult.Handled(state.copy(currentMode = AppMode.QUEUE_NAVIGATION, commandQuery = "", selectedCommandIndex = 0))
                } else {
                    KeyResult.Ignored
                }
            }
            Key.DirectionDown -> {
                KeyResult.Handled(state.copy(selectedCommandIndex = state.selectedCommandIndex + 1))
            }
            Key.DirectionUp -> {
                KeyResult.Handled(state.copy(selectedCommandIndex = (state.selectedCommandIndex - 1).coerceAtLeast(0)))
            }
            Key.Enter -> {
                KeyResult.Handled(onExecuteCommand(state))
            }
            else -> KeyResult.Ignored
        }
    }
}
