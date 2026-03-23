package codes.chirag.emailclient.core.ui

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.*

/**
 * Abstracted icon definitions using Feather Icons.
 * Provides a minimalist, consistent editorial aesthetic.
 */
object AppIcons {
    // Workspace Rail
    val WorkspaceGoogle: ImageVector = FeatherIcons.Mail
    val WorkspaceWork: ImageVector = FeatherIcons.Briefcase
    val WorkspacePersonal: ImageVector = FeatherIcons.User
    val AddWorkspace: ImageVector = FeatherIcons.Plus
    val Settings: ImageVector = FeatherIcons.Settings

    // Navigation Rail
    val Profile: ImageVector = FeatherIcons.User
    val Inbox: ImageVector = FeatherIcons.Inbox
    val Sent: ImageVector = FeatherIcons.Send
    val Drafts: ImageVector = FeatherIcons.Edit // Drafts/Docs
    val Archive: ImageVector = FeatherIcons.Archive
    val Trash: ImageVector = FeatherIcons.Trash2
    val PaperPlane: ImageVector = FeatherIcons.Send
    val Reply: ImageVector = FeatherIcons.CornerUpLeft
    val Close: ImageVector = FeatherIcons.X
    
    // Search
    val Search: ImageVector = FeatherIcons.Search
}
