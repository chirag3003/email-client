package codes.chirag.emailclient.features.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.shared.model.FolderType
import codes.chirag.emailclient.core.ui.AppIcons
import codes.chirag.emailclient.core.ui.theme.EditorialColors
import codes.chirag.emailclient.core.ui.theme.focusRing

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
    }
}

@Composable
private fun NavFolderItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor = when {
        isSelected -> EditorialColors.SurfaceSelected
        isHovered -> EditorialColors.SurfaceHover
        else -> EditorialColors.Surface
    }

    val iconTint = when {
        isSelected -> EditorialColors.TextPrimary
        isHovered -> EditorialColors.TextPrimary
        else -> EditorialColors.TextMuted
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .hoverable(interactionSource)
            .focusRing()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Folder",
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
    }
}
