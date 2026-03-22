package codes.chirag.emailclient.ui.panes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.models.NormalizedEmail
import codes.chirag.emailclient.ui.components.AppIcons
import codes.chirag.emailclient.ui.theme.EditorialColors
import codes.chirag.emailclient.ui.theme.AppTypography

@Composable
fun EmailQueuePane(
    emails: List<NormalizedEmail>,
    activeEmailId: String?,
    onEmailSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(350.dp)
            .background(EditorialColors.Surface)
            .border(width = 1.dp, color = EditorialColors.Border)
    ) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(EditorialColors.SurfaceSelected, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = AppIcons.Search,
                contentDescription = "Search",
                tint = EditorialColors.TextMuted,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Search...",
                style = AppTypography.bodyLarge,
                color = EditorialColors.TextMuted,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "⌘K",
                style = AppTypography.labelSmall,
                color = EditorialColors.TextMuted,
                modifier = Modifier
                    .border(1.dp, EditorialColors.Border, RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        // List
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(emails) { email ->
                EmailListItem(
                    email = email,
                    isSelected = email.internalId == activeEmailId,
                    onClick = { onEmailSelected(email.internalId) }
                )
            }
        }

        // Status Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = EditorialColors.Border)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val unreadCount = emails.count { !it.isRead }
            Text(
                text = "$unreadCount Unread",
                style = AppTypography.labelMedium,
                color = EditorialColors.TextMuted
            )
            Text(
                text = "☁ Syncing",
                style = AppTypography.labelMedium,
                color = EditorialColors.TextMuted
            )
        }
    }
}

@Composable
private fun EmailListItem(
    email: NormalizedEmail,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) EditorialColors.SurfaceSelected else EditorialColors.Surface)
            .let { if (isSelected) it.border(1.dp, EditorialColors.Border) else it } // Orange border in mockup? It shows left orange border but let's keep it simple with surface selection.
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        // Unread Indicator
        Box(
            modifier = Modifier
                .padding(top = 4.dp, end = 8.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(if (!email.isRead) EditorialColors.UnreadDot else EditorialColors.Background)
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = email.senderName,
                    style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = EditorialColors.TextPrimary
                )
                Text(
                    text = email.timestampStr,
                    style = AppTypography.labelMedium,
                    color = EditorialColors.TextMuted
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email.subject,
                style = AppTypography.bodyLarge,
                color = EditorialColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
