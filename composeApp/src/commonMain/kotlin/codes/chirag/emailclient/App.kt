package codes.chirag.emailclient

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.focusable
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.core.data.MockData
import codes.chirag.emailclient.core.domain.*
import codes.chirag.emailclient.features.mail.EmailDetailPane
import codes.chirag.emailclient.features.compose.ComposePane
import codes.chirag.emailclient.features.mail.EmailQueuePane
import codes.chirag.emailclient.features.navigation.NavigationRail
import codes.chirag.emailclient.features.navigation.WorkspaceRail
import codes.chirag.emailclient.core.ui.theme.AppTheme
import codes.chirag.emailclient.core.input.KeyboardManager
import codes.chirag.emailclient.core.ui.components.TitleBar
import codes.chirag.emailclient.features.commands.CommandPalette

@Composable
fun App(
    onMinimize: () -> Unit = {},
    onMaximize: () -> Unit = {},
    onClose: () -> Unit = {},
    draggableArea: @Composable (Modifier, @Composable () -> Unit) -> Unit = { modifier, content -> 
        Box(modifier) { content() } 
    }
) {
    val platform = getPlatform()
    AppTheme {
        var state by remember { 
            mutableStateOf(
                GlobalState(
                    emails = MockData.MockEmails,
                    activeEmailId = null
                )
            ) 
        }

        val commands = remember(state) {
            listOf(
                Command("Go to Inbox", "g i") { state = state.copy(activeFolder = FolderType.INBOX, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Go to Drafts", "g d") { state = state.copy(activeFolder = FolderType.DRAFTS, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Go to Sent", "g s") { state = state.copy(activeFolder = FolderType.SENT, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Switch to Gmail", "Ctrl+1") { state = state.copy(activeWorkspace = WorkspaceType.GMAIL, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Switch to Work", "Ctrl+2") { state = state.copy(activeWorkspace = WorkspaceType.WORK, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Switch to Personal", "Ctrl+3") { state = state.copy(activeWorkspace = WorkspaceType.PERSONAL, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Compose New Email", "c") { state = state.copy(isComposing = true, activeEmailId = null, currentMode = AppMode.COMPOSE_MODE) },
                Command("Toggle Agenda", "v") { state = state.copy(isAgendaVisible = !state.isAgendaVisible, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Empty Trash", null) { /* TODO */ state = state.copy(currentMode = AppMode.QUEUE_NAVIGATION) }
            )
        }

        val filteredCommands = remember(state.commandQuery, commands) {
            if (state.commandQuery.isEmpty()) commands
            else commands.filter { it.label.contains(state.commandQuery, ignoreCase = true) }
        }

        val keyboardManager = remember {
            KeyboardManager(
                onExecuteCommand = { currentState ->
                    val finalFiltered = if (currentState.commandQuery.isEmpty()) commands
                    else commands.filter { it.label.contains(currentState.commandQuery, ignoreCase = true) }
                    
                    val selectedCommand = finalFiltered.getOrNull(currentState.selectedCommandIndex)
                    selectedCommand?.action?.invoke()
                    
                    currentState.copy(currentMode = AppMode.QUEUE_NAVIGATION, commandQuery = "")
                }
            )
        }

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (platform.isDesktop) {
                    TitleBar(
                        onMinimize = onMinimize,
                        onMaximize = onMaximize,
                        onClose = onClose,
                        draggableArea = draggableArea
                    )
                }
                
                Surface(modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onKeyEvent { event ->
                        val newState = keyboardManager.handleEvent(event, state)
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
                            val displayedEmails = state.emails.filter { it.folder == state.activeFolder }
                            val isQueueExpanded = state.activeEmailId == null
                            
                            EmailQueuePane(
                                emails = displayedEmails,
                                activeEmailId = state.activeEmailId,
                                isExpanded = isQueueExpanded,
                                onEmailSelected = { id ->
                                    if (state.activeFolder == FolderType.DRAFTS) {
                                        state = state.copy(activeEmailId = id, isComposing = true)
                                    } else {
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
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Command Palette Overlay
            if (state.currentMode == AppMode.COMMAND_PALETTE) {
                CommandPalette(
                    query = state.commandQuery,
                    onQueryChange = { state = state.copy(commandQuery = it, selectedCommandIndex = 0) },
                    commands = filteredCommands,
                    selectedIndex = state.selectedCommandIndex,
                    onCommandClick = { command ->
                        command.action()
                        state = state.copy(currentMode = AppMode.QUEUE_NAVIGATION, commandQuery = "")
                    },
                    onDismiss = { state = state.copy(currentMode = AppMode.QUEUE_NAVIGATION, commandQuery = "") }
                )
            }
        }
    }
}
