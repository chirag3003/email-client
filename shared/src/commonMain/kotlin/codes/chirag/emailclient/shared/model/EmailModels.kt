package codes.chirag.emailclient.shared.model

import kotlinx.serialization.Serializable

@Serializable
enum class WorkspaceType {
    GMAIL, WORK, PERSONAL
}

@Serializable
enum class FolderType {
    INBOX, DRAFTS, SENT, TRASH, ARCHIVE
}

@Serializable
data class NormalizedEmail(
    val internalId: String,
    val workspace: WorkspaceType,
    val folder: FolderType,
    val senderName: String,
    val senderEmail: String,
    val subject: String,
    val snippet: String,
    val bodyText: String,
    val timestampStr: String,
    val timestamp: Long,
    val isRead: Boolean,
    val isThreadSelected: Boolean = false
)
