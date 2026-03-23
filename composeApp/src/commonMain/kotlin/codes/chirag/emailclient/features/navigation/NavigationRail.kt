package codes.chirag.emailclient.features.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.core.domain.FolderType
import codes.chirag.emailclient.core.ui.AppIcons
import codes.chirag.emailclient.core.ui.theme.EditorialColors

@Composable
fun NavigationRail(
    activeFolder: FolderType,
    onFolderSelected: (FolderType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(64.dp)
            .background(EditorialColors.Surface)
            .border(width = 1.dp, color = EditorialColors.Border)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Folders
        NavFolderItem(
            icon = AppIcons.Inbox,
            isSelected = activeFolder == FolderType.INBOX,
            onClick = { onFolderSelected(FolderType.INBOX) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        NavFolderItem(
            icon = AppIcons.Sent,
            isSelected = activeFolder == FolderType.SENT,
            onClick = { onFolderSelected(FolderType.SENT) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        NavFolderItem(
            icon = AppIcons.Drafts,
            isSelected = activeFolder == FolderType.DRAFTS,
            onClick = { onFolderSelected(FolderType.DRAFTS) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        NavFolderItem(
            icon = AppIcons.Archive,
            isSelected = activeFolder == FolderType.ARCHIVE,
            onClick = { onFolderSelected(FolderType.ARCHIVE) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        NavFolderItem(
            icon = AppIcons.Trash,
            isSelected = activeFolder == FolderType.TRASH,
            onClick = { onFolderSelected(FolderType.TRASH) }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Settings or extra at bottom
    }
}

@Composable
private fun NavFolderItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected) EditorialColors.SurfaceSelected else EditorialColors.Surface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Folder",
            tint = if (isSelected) EditorialColors.TextPrimary else EditorialColors.TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
