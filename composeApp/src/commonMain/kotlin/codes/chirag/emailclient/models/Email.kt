package codes.chirag.emailclient.models

data class NormalizedEmail(
    val internalId: String,
    val workspace: WorkspaceType,
    val folder: FolderType,
    val senderName: String,
    val senderEmail: String,
    val subject: String,
    val snippet: String, // 1-line truncation for the Queue
    val bodyText: String, // Full text for detail pane
    val timestampStr: String, // e.g., "10:42 AM", "Yesterday"
    val timestamp: Long,
    val isRead: Boolean,
    val isThreadSelected: Boolean = false // Track if this specific email is currently open
)

enum class FolderType {
    INBOX, DRAFTS, SENT, TRASH, ARCHIVE
}
