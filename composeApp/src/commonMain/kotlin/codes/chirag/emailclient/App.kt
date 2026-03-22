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
import androidx.compose.ui.tooling.preview.Preview
import codes.chirag.emailclient.data.MockData
import codes.chirag.emailclient.state.GlobalState
import codes.chirag.emailclient.ui.panes.EmailDetailPane
import codes.chirag.emailclient.ui.panes.ComposePane
import codes.chirag.emailclient.ui.panes.EmailQueuePane
import codes.chirag.emailclient.ui.panes.NavigationRail
import codes.chirag.emailclient.ui.panes.WorkspaceRail
import codes.chirag.emailclient.ui.theme.AppTheme

@Composable
@Preview
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
                if (event.type == KeyEventType.KeyDown) {
                    val displayedEmails = state.emails.filter { it.folder == state.activeFolder }
                    when (event.key) {
                        Key.J -> {
                            if (!state.isComposing) {
                                val currentIndex = displayedEmails.indexOfFirst { it.internalId == state.activeEmailId }
                                val nextIndex = if (currentIndex < displayedEmails.size - 1) currentIndex + 1 else currentIndex
                                val nextEmailId = if (currentIndex == -1 && displayedEmails.isNotEmpty()) displayedEmails[0].internalId else displayedEmails.getOrNull(nextIndex)?.internalId
                                if (nextEmailId != null) state = state.copy(activeEmailId = nextEmailId)
                                return@onKeyEvent true
                            }
                        }
                        Key.K -> {
                            if (!state.isComposing) {
                                val currentIndex = displayedEmails.indexOfFirst { it.internalId == state.activeEmailId }
                                val prevIndex = if (currentIndex > 0) currentIndex - 1 else 0
                                val prevEmailId = if (currentIndex == -1 && displayedEmails.isNotEmpty()) displayedEmails[0].internalId else displayedEmails.getOrNull(prevIndex)?.internalId
                                if (prevEmailId != null) state = state.copy(activeEmailId = prevEmailId)
                                return@onKeyEvent true
                            }
                        }
                        Key.C -> {
                            if (!state.isComposing) {
                                state = state.copy(isComposing = true, activeEmailId = null)
                                return@onKeyEvent true
                            }
                        }
                        Key.Escape -> {
                            if (state.isComposing) {
                                state = state.copy(isComposing = false, activeEmailId = null)
                                return@onKeyEvent true
                            } else if (state.activeEmailId != null) {
                                state = state.copy(activeEmailId = null)
                                return@onKeyEvent true
                            }
                        }
                    }
                }
                false
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
                            if (state.activeFolder == codes.chirag.emailclient.models.FolderType.DRAFTS) {
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
