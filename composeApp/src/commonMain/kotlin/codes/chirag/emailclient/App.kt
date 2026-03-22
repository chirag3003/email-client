package codes.chirag.emailclient

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import codes.chirag.emailclient.data.MockData
import codes.chirag.emailclient.state.GlobalState
import codes.chirag.emailclient.ui.panes.EmailDetailPane
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

        Surface(modifier = Modifier.fillMaxSize()) {
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
                
                // If no email is selected, the queue expands to fill all space.
                // If an email is selected, the queue shrinks to a fixed width list.
                val isQueueExpanded = state.activeEmailId == null
                
                EmailQueuePane(
                    emails = state.emails,
                    activeEmailId = state.activeEmailId,
                    isExpanded = isQueueExpanded,
                    onEmailSelected = { id ->
                        // Toggle selection: if clicking the already selected email, unselect it (null)
                        state = state.copy(activeEmailId = if (state.activeEmailId == id) null else id)
                    },
                    modifier = if (isQueueExpanded) Modifier.weight(1f) else Modifier.width(350.dp)
                )
                
                if (!isQueueExpanded) {
                    val selectedEmail = state.emails.find { it.internalId == state.activeEmailId }
                    EmailDetailPane(
                        email = selectedEmail,
                        modifier = Modifier.weight(1f) // Detail takes remaining space when shown
                    )
                }
            }
        }
    }
}
