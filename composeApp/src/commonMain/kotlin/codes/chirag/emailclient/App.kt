package codes.chirag.emailclient

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.focusable
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.BoxWithConstraints
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
import codes.chirag.emailclient.features.commands.Cheatsheet

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
                Command("Go to Inbox", "g i") { state = state.copy(activeFolder = FolderType.INBOX, currentMode = AppMode.QUEUE_NAVIGATION, activeEmailId = null) },
                Command("Go to Sent", "g s") { state = state.copy(activeFolder = FolderType.SENT, currentMode = AppMode.QUEUE_NAVIGATION, activeEmailId = null) },
                Command("Go to Drafts", "g d") { state = state.copy(activeFolder = FolderType.DRAFTS, currentMode = AppMode.QUEUE_NAVIGATION, activeEmailId = null) },
                Command("Go to Archive", "g a") { state = state.copy(activeFolder = FolderType.ARCHIVE, currentMode = AppMode.QUEUE_NAVIGATION, activeEmailId = null) },
                Command("Go to Trash", "g t") { state = state.copy(activeFolder = FolderType.TRASH, currentMode = AppMode.QUEUE_NAVIGATION, activeEmailId = null) },
                Command("Switch to Gmail", "Ctrl+1") { state = state.copy(activeWorkspace = WorkspaceType.GMAIL, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Switch to Work", "Ctrl+2") { state = state.copy(activeWorkspace = WorkspaceType.WORK, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Switch to Personal", "Ctrl+3") { state = state.copy(activeWorkspace = WorkspaceType.PERSONAL, currentMode = AppMode.QUEUE_NAVIGATION) },
                Command("Compose New Email", "c") { state = state.copy(isComposing = true, activeEmailId = null, currentMode = AppMode.COMPOSE_MODE) },
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
        
        // State-driven focus restoration:
        // Whenever we leave an overlay (Command Palette or Cheatsheet)
        // or close a full-screen mode like Compose, ensure the root surface
        // re-acquires focus for global keyboard shortcuts.
        LaunchedEffect(state.currentMode, state.isCheatsheetVisible, state.isComposing) {
            if (state.currentMode == AppMode.QUEUE_NAVIGATION && !state.isCheatsheetVisible && !state.isComposing) {
                focusRequester.requestFocus()
            }
        }

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
                                state = state.copy(activeFolder = folder, activeEmailId = null)
                            }
                        )

                        BoxWithConstraints(modifier = Modifier.weight(1f)) {
                            val containerWidth = maxWidth
                            val isSmallScreen = containerWidth < 1000.dp
                            val isDetailOpen = state.activeEmailId != null

                            if (state.isComposing) {
                                val draftEmail = state.emails.find { it.internalId == state.activeEmailId }
                                ComposePane(
                                    draftEmail = draftEmail,
                                    onClose = { state = state.copy(isComposing = false, activeEmailId = null) },
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Row(modifier = Modifier.fillMaxSize()) {
                                    // Queue Pane with width animation
                                    val queueModifier = if (isSmallScreen) {
                                        if (isDetailOpen) Modifier.size(0.dp) else Modifier.fillMaxSize()
                                    } else {
                                        if (isDetailOpen) Modifier.width(350.dp) else Modifier.fillMaxSize()
                                    }

                                    Box(modifier = queueModifier.animateContentSize(tween(300, easing = FastOutSlowInEasing))) {
                                        val displayedEmails = state.emails.filter { it.folder == state.activeFolder }
                                        EmailQueuePane(
                                            title = state.activeFolder.name.lowercase()
                                                .replaceFirstChar { it.uppercase() },
                                            emails = displayedEmails,
                                            activeEmailId = state.activeEmailId,
                                            selectedEmailIds = state.selectedEmailIds,
                                            isExpanded = !isDetailOpen,
                                            onEmailSelected = { id ->
                                                if (state.activeFolder == FolderType.DRAFTS) {
                                                    state = state.copy(activeEmailId = id, isComposing = true)
                                                } else {
                                                    state = state.copy(
                                                        activeEmailId = if (state.activeEmailId == id) null else id
                                                    )
                                                }
                                            },
                                            onComposeClicked = {
                                                state = state.copy(isComposing = true, activeEmailId = null)
                                            },
                                            onEmptyTrash = if (state.activeFolder == FolderType.TRASH) {
                                                {
                                                    state = state.copy(
                                                        emails = state.emails.filter { it.folder != FolderType.TRASH },
                                                        activeEmailId = null,
                                                        selectedEmailIds = emptySet()
                                                    )
                                                }
                                            } else null,
                                            onTrashSelection = {
                                                val updatedEmails = state.emails.map {
                                                    if (it.internalId in state.selectedEmailIds) it.copy(folder = FolderType.TRASH) else it
                                                }
                                                state = state.copy(
                                                    emails = updatedEmails,
                                                    selectedEmailIds = emptySet(),
                                                    activeEmailId = null
                                                )
                                            },
                                            onClearSelection = {
                                                state = state.copy(selectedEmailIds = emptySet())
                                            },
                                            onArchiveEmail = { id ->
                                                val updated = state.emails.map {
                                                    if (it.internalId == id) it.copy(folder = FolderType.ARCHIVE) else it
                                                }
                                                state = state.copy(emails = updated, activeEmailId = null)
                                            },
                                            onDeleteEmail = { id ->
                                                val updated = state.emails.map {
                                                    if (it.internalId == id) it.copy(folder = FolderType.TRASH) else it
                                                }
                                                state = state.copy(emails = updated, activeEmailId = null)
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    // Detail Pane with Slide Animation
                                    AnimatedVisibility(
                                        visible = isDetailOpen,
                                        enter = slideInHorizontally(
                                            initialOffsetX = { it },
                                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                                        ) + fadeIn(),
                                        exit = slideOutHorizontally(
                                            targetOffsetX = { it },
                                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                                        ) + fadeOut(),
                                        modifier = if (isSmallScreen) Modifier.fillMaxSize() else Modifier.weight(1f)
                                    ) {
                                        val selectedEmail =
                                            state.emails.find { it.internalId == state.activeEmailId }
                                        EmailDetailPane(
                                            email = selectedEmail,
                                            onComposeClicked = { state = state.copy(isComposing = true) },
                                            onCloseClicked = { state = state.copy(activeEmailId = null) },
                                            onArchive = { id ->
                                                val updated = state.emails.map {
                                                    if (it.internalId == id) it.copy(folder = FolderType.ARCHIVE) else it
                                                }
                                                state = state.copy(emails = updated, activeEmailId = null)
                                            },
                                            onDelete = { id ->
                                                val updated = state.emails.map {
                                                    if (it.internalId == id) it.copy(folder = FolderType.TRASH) else it
                                                }
                                                state = state.copy(emails = updated, activeEmailId = null)
                                            },
                                            onRestore = { id ->
                                                val updated = state.emails.map {
                                                    if (it.internalId == id) it.copy(folder = FolderType.INBOX) else it
                                                }
                                                state = state.copy(emails = updated, activeEmailId = null)
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
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

            // Cheatsheet Overlay
            if (state.isCheatsheetVisible) {
                Cheatsheet(
                    onDismiss = { state = state.copy(isCheatsheetVisible = false) }
                )
            }
        }
    }
}
