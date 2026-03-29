package codes.chirag.emailclient.core.domain

import codes.chirag.emailclient.shared.model.*

data class GlobalState(
    val currentMode: AppMode = AppMode.QUEUE_NAVIGATION,
    val activeWorkspace: WorkspaceType = WorkspaceType.GMAIL,
    val activeFolder: FolderType = FolderType.INBOX,
    val selectedQueueIndex: Int = 0,
    
    // Derived/Selected state for display
    val activeEmailId: String? = null,
    val showActiveEmail: Boolean = false,
    val isComposing: Boolean = false, // Track if we are in full-screen compose mode
    val emails: List<NormalizedEmail> = emptyList(), // Store the local list
    val commandQuery: String = "",
    val selectedCommandIndex: Int = 0,
    val isCheatsheetVisible: Boolean = false,
    val isProfileMenuVisible: Boolean = false,
    val selectedEmailIds: Set<String> = emptySet(),
    val currentUser: User? = null
)
