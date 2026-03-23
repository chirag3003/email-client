package codes.chirag.emailclient

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.focusable
import androidx.compose.ui.input.key.*
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.core.data.MockData
import codes.chirag.emailclient.core.domain.GlobalState
import codes.chirag.emailclient.core.domain.FolderType
import codes.chirag.emailclient.features.mail.EmailDetailPane
import codes.chirag.emailclient.features.compose.ComposePane
import codes.chirag.emailclient.features.mail.EmailQueuePane
import codes.chirag.emailclient.features.navigation.NavigationRail
import codes.chirag.emailclient.features.navigation.WorkspaceRail
import codes.chirag.emailclient.core.ui.theme.AppTheme
import codes.chirag.emailclient.core.input.KeyboardManager

@Composable
fun App() {
    AppTheme {
        var state by remember { 
            mutableStateOf(
                GlobalState(
                    emails = MockData.MockEmails,
                    activeEmailId = null // Start with nothing selected
                )
            ) 
        }

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        Surface(modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .onKeyEvent { event ->
                val newState = KeyboardManager.handleEvent(event, state)
                if (newState != state) {
                    state = newState
                    true
                } else {
                    false
                }
            }
            .focusable()
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                WorkspaceRail(
                    activeWorkspace = state.activeWorkspace,
                    onWorkspaceSelected = { workspace ->
                        state = state.copy(activeWorkspace = workspace)
                    }
                )
                
                NavigationRail(
                    activeFolder = state.activeFolder,
                    onFolderSelected = { folder ->
                        state = state.copy(activeFolder = folder)
                    }
                )
                
                if (state.isComposing) {
                    val draftEmail = state.emails.find { it.internalId == state.activeEmailId }
                    ComposePane(
                        draftEmail = draftEmail,
                        onClose = { state = state.copy(isComposing = false, activeEmailId = null) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    // Filter emails by the active folder
                    val displayedEmails = state.emails.filter { it.folder == state.activeFolder }
                    
                    // If no email is selected, the queue expands to fill all space.
                    // If an email is selected, the queue shrinks to a fixed width list.
                    val isQueueExpanded = state.activeEmailId == null
                    
                    EmailQueuePane(
                        emails = displayedEmails,
                        activeEmailId = state.activeEmailId,
                        isExpanded = isQueueExpanded,
                        onEmailSelected = { id ->
                            if (state.activeFolder == FolderType.DRAFTS) {
                                // If we are in the Drafts folder, clicking an item opens Compose directly
                                state = state.copy(activeEmailId = id, isComposing = true)
                            } else {
                                // Toggle selection: if clicking the already selected email, unselect it (null)
                                state = state.copy(activeEmailId = if (state.activeEmailId == id) null else id)
                            }
                        },
                        onComposeClicked = { state = state.copy(isComposing = true, activeEmailId = null) },
                        modifier = if (isQueueExpanded) Modifier.weight(1f) else Modifier.width(350.dp)
                    )
                    
                    if (!isQueueExpanded) {
                        val selectedEmail = state.emails.find { it.internalId == state.activeEmailId }
                        EmailDetailPane(
                            email = selectedEmail,
                            onComposeClicked = { state = state.copy(isComposing = true) },
                            onCloseClicked = { state = state.copy(activeEmailId = null) },
                            modifier = Modifier.weight(1f) // Detail takes remaining space when shown
                        )
                    }
                }
            }
        }
    }
}
