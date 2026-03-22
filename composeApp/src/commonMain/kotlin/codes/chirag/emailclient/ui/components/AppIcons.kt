package codes.chirag.emailclient.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Abstracted icon definitions. 
 * Currently using standard Compose Material icons as placeholders.
 * Easily swap these to `painterResource` (SVGs) when actual assets are provided.
 */
object AppIcons {
    // Workspace Rail
    val WorkspaceGoogle: ImageVector = Icons.Default.Email // Placeholder for 'G'
    val WorkspaceWork: ImageVector = Icons.Default.Build // Placeholder for 'W'
    val WorkspacePersonal: ImageVector = Icons.Default.Person // Placeholder for 'P'
    val AddWorkspace: ImageVector = Icons.Default.Add
    val Settings: ImageVector = Icons.Default.Settings

    // Navigation Rail
    val Profile: ImageVector = Icons.Default.AccountCircle
    val Inbox: ImageVector = Icons.Default.Email
    val Document: ImageVector = Icons.Default.Create // Drafts/Docs
    val PaperPlane: ImageVector = Icons.AutoMirrored.Filled.Send

    // Action Bar (Detail Pane)
    val Archive: ImageVector = Icons.Default.CheckCircle // Archive alternative
    val Trash: ImageVector = Icons.Default.Delete
    val Reply: ImageVector = Icons.AutoMirrored.Filled.Send // Reply alternative
    val Close: ImageVector = Icons.Default.Close
    
    // Search
    val Search: ImageVector = Icons.Default.Search
}
