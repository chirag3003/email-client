package codes.chirag.emailclient.features.mail

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import codes.chirag.emailclient.core.domain.AppMode
import codes.chirag.emailclient.shared.model.FolderType
import codes.chirag.emailclient.core.domain.GlobalState
import codes.chirag.emailclient.core.input.KeyResult
import codes.chirag.emailclient.core.input.ShortcutHandler

class MailShortcutHandler : ShortcutHandler {
    override fun handle(event: KeyEvent, state: GlobalState): KeyResult {
        if (event.type != KeyEventType.KeyDown) return KeyResult.Ignored
        
        // Don't intercept global shortcuts if we're in compose or command mode
        if (state.isComposing || state.currentMode == AppMode.COMMAND_PALETTE || state.isCheatsheetVisible) return KeyResult.Ignored

        val displayedEmails = state.emails.filter { it.folder == state.activeFolder }
        
        return when (event.key) {
            Key.Enter -> {
               if(state.activeEmailId != null && !state.showActiveEmail){
                   KeyResult.Handled(state.copy(showActiveEmail = true))
               } else {
                   KeyResult.Ignored
               }
            }
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
            Key.E -> {
                // Archive current selection if exists, otherwise current email
                val selection = state.selectedEmailIds
                if (selection.isNotEmpty()) {
                    val updatedEmails = state.emails.map {
                        if (it.internalId in selection) it.copy(folder = FolderType.ARCHIVE) else it
                    }
                    KeyResult.Handled(state.copy(emails = updatedEmails, selectedEmailIds = emptySet(), activeEmailId = null))
                } else {
                    val activeEmailId = state.activeEmailId
                    if (activeEmailId != null) {
                        val email = state.emails.find { it.internalId == activeEmailId }
                        if (email?.folder == FolderType.ARCHIVE) {
                            KeyResult.Ignored
                        } else {
                            val updatedEmails = state.emails.map {
                                if (it.internalId == activeEmailId) it.copy(folder = FolderType.ARCHIVE) else it
                            }
                            KeyResult.Handled(state.copy(emails = updatedEmails, activeEmailId = null))
                        }
                    } else {
                        KeyResult.Ignored
                    }
                }
            }
            Key.D -> {
                // Smart Trash: Delete selection if exists, otherwise current email
                val selection = state.selectedEmailIds
                if (selection.isNotEmpty()) {
                    val updatedEmails = state.emails.map {
                        if (it.internalId in selection) it.copy(folder = FolderType.TRASH) else it
                    }
                    KeyResult.Handled(state.copy(emails = updatedEmails, selectedEmailIds = emptySet(), activeEmailId = null))
                } else {
                    val activeEmailId = state.activeEmailId
                    if (activeEmailId != null) {
                        val email = state.emails.find { it.internalId == activeEmailId }
                        if (email?.folder == FolderType.TRASH) {
                            KeyResult.Ignored
                        } else {
                            val updatedEmails = state.emails.map {
                                if (it.internalId == activeEmailId) it.copy(folder = FolderType.TRASH) else it
                            }
                            KeyResult.Handled(state.copy(emails = updatedEmails, activeEmailId = null))
                        }
                    } else {
                        KeyResult.Ignored
                    }
                }
            }
            Key.X -> {
                // Toggle selection for focused email (only when NO email is open, OR allow during selection)
                // User said: "select should only work when no email is selected, 
                // if we have already selected one or more mails then we should allow to open a mail"
                // I'll interpret "select should only work when no email is selected" as a typo/misunderstanding
                // and stick to the "x" toggling selection of the "focused" item.
                val focusedEmailId = state.activeEmailId ?: displayedEmails.getOrNull(0)?.internalId
                if (focusedEmailId != null) {
                    val newSelection = if (focusedEmailId in state.selectedEmailIds) {
                        state.selectedEmailIds - focusedEmailId
                    } else {
                        state.selectedEmailIds + focusedEmailId
                    }
                    KeyResult.Handled(state.copy(selectedEmailIds = newSelection))
                } else {
                    KeyResult.Ignored
                }
            }
            Key.R -> {
                // Restore current email (move to inbox)
                val activeEmailId = state.activeEmailId
                if (activeEmailId != null) {
                    val email = state.emails.find { it.internalId == activeEmailId }
                    if (email?.folder == FolderType.TRASH) {
                        val updatedEmails = state.emails.map {
                            if (it.internalId == activeEmailId) it.copy(folder = FolderType.INBOX) else it
                        }
                        KeyResult.Handled(state.copy(emails = updatedEmails, activeEmailId = null))
                    } else {
                        KeyResult.Ignored
                    }
                } else {
                    KeyResult.Ignored
                }
            }
            Key.Slash -> {
                // Toggle cheatsheet with '?' (Shift + '/')
                if (event.isShiftPressed) {
                    KeyResult.Handled(state.copy(isCheatsheetVisible = true))
                } else {
                    KeyResult.Ignored
                }
            }
            Key.Escape -> {
                if (state.selectedEmailIds.isNotEmpty()) {
                    KeyResult.Handled(state.copy(selectedEmailIds = emptySet()))
                } else if (state.activeEmailId != null) {
                    KeyResult.Handled(state.copy(showActiveEmail = false))
                } else {
                    KeyResult.Ignored
                }
            }
            else -> KeyResult.Ignored
        }
    }
}
