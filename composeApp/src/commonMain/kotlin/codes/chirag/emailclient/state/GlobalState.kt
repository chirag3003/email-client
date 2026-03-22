package codes.chirag.emailclient.state

import codes.chirag.emailclient.models.WorkspaceType
import codes.chirag.emailclient.models.FolderType
import codes.chirag.emailclient.models.NormalizedEmail

data class GlobalState(
    val currentMode: AppMode = AppMode.QUEUE_NAVIGATION,
    val activeWorkspace: WorkspaceType = WorkspaceType.GMAIL,
    val activeFolder: FolderType = FolderType.INBOX,
    val selectedQueueIndex: Int = 0,
    val isAgendaVisible: Boolean = false,
    
    // Derived/Selected state for display
    val activeEmailId: String? = null,
    val emails: List<NormalizedEmail> = emptyList(), // Store the local list
    val searchQuery: String = ""
)
