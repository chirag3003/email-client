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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.shared.model.WorkspaceType
import codes.chirag.emailclient.core.ui.AppIcons
import codes.chirag.emailclient.core.ui.theme.EditorialColors
import codes.chirag.emailclient.core.ui.theme.focusRing

@Composable
fun WorkspaceRail(
    activeWorkspace: WorkspaceType,
    onWorkspaceSelected: (WorkspaceType) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(64.dp)
            .background(EditorialColors.Background)
            .border(width = 1.dp, color = EditorialColors.Border)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WorkspaceItem(
            text = "G",
            isSelected = activeWorkspace == WorkspaceType.GMAIL,
            onClick = { onWorkspaceSelected(WorkspaceType.GMAIL) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        WorkspaceItem(
            text = "W",
            isSelected = activeWorkspace == WorkspaceType.WORK,
            onClick = { onWorkspaceSelected(WorkspaceType.WORK) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        WorkspaceItem(
            text = "P",
            isSelected = activeWorkspace == WorkspaceType.PERSONAL,
            onClick = { onWorkspaceSelected(WorkspaceType.PERSONAL) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Add new workspace
        AddWorkspaceButton()

        Spacer(modifier = Modifier.weight(1f))

        // Settings
        SettingsButton()
        Spacer(modifier = Modifier.height(32.dp))
        // Profile Avatar
        ProfileButton(onProfileClick = onProfileClick)
    }
}

@Composable
private fun WorkspaceItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor = when {
        isSelected -> EditorialColors.SurfaceSelected
        isHovered -> EditorialColors.SurfaceHover
        else -> EditorialColors.Background
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
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(3.dp)
                    .background(EditorialColors.Primary)
                    .align(Alignment.CenterStart)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = EditorialColors.Primary
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = if (isHovered) EditorialColors.TextPrimary else EditorialColors.TextMuted
            )
        }
    }
}

@Composable
private fun AddWorkspaceButton() {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isHovered) EditorialColors.SurfaceHover else EditorialColors.SurfaceSelected)
            .hoverable(interactionSource)
            .focusRing()
            .clickable { /* TODO */ },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = AppIcons.AddWorkspace,
            contentDescription = "Add Workspace",
            tint = EditorialColors.TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SettingsButton() {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isHovered) EditorialColors.SurfaceHover else EditorialColors.Background)
            .hoverable(interactionSource)
            .focusRing()
            .clickable { /* TODO */ },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = AppIcons.Settings,
            contentDescription = "Settings",
            tint = if (isHovered) EditorialColors.TextPrimary else EditorialColors.TextMuted,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun ProfileButton(onProfileClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isHovered) EditorialColors.SurfaceHover else EditorialColors.SurfaceSelected)
            .hoverable(interactionSource)
            .focusRing()
            .clickable { onProfileClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = AppIcons.Profile,
            contentDescription = "Profile",
            tint = if (isHovered) EditorialColors.TextPrimary else EditorialColors.TextMuted,
            modifier = Modifier.size(24.dp)
        )
    }
}
