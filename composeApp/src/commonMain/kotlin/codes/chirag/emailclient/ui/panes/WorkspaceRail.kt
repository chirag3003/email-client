package codes.chirag.emailclient.ui.panes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.models.WorkspaceType
import codes.chirag.emailclient.ui.components.AppIcons
import codes.chirag.emailclient.ui.theme.EditorialColors
import codes.chirag.emailclient.ui.theme.AppTypography

@Composable
fun WorkspaceRail(
    activeWorkspace: WorkspaceType,
    onWorkspaceSelected: (WorkspaceType) -> Unit,
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
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(EditorialColors.SurfaceSelected)
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
        
        Spacer(modifier = Modifier.weight(1f))

        // Settings
        Icon(
            imageVector = AppIcons.Settings,
            contentDescription = "Settings",
            tint = EditorialColors.TextMuted,
            modifier = Modifier
                .size(24.dp)
                .clickable { /* TODO */ }
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Profile Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(EditorialColors.SurfaceSelected)
                .clickable { /* Profile settings */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = AppIcons.Profile,
                contentDescription = "Profile",
                tint = EditorialColors.TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }

    }
}

@Composable
private fun WorkspaceItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected) EditorialColors.SurfaceSelected else EditorialColors.Background)
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
                style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = EditorialColors.Primary
            )
        } else {
            Text(
                text = text,
                style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = EditorialColors.TextMuted
            )
        }
    }
}
