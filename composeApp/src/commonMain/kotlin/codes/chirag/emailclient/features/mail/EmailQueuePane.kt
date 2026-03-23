package codes.chirag.emailclient.features.mail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import codes.chirag.emailclient.core.domain.NormalizedEmail
import codes.chirag.emailclient.core.ui.AppIcons
import codes.chirag.emailclient.core.ui.theme.EditorialColors
import codes.chirag.emailclient.core.ui.theme.AppTypography
import androidx.compose.ui.draw.clipToBounds
@Composable
fun EmailQueuePane(
    title: String,
    emails: List<NormalizedEmail>,
    activeEmailId: String?,
    selectedEmailIds: Set<String>,
    isExpanded: Boolean,
    onEmailSelected: (String) -> Unit,
    onComposeClicked: () -> Unit,
    onEmptyTrash: (() -> Unit)? = null,
    onTrashSelection: (() -> Unit)? = null,
    onClearSelection: (() -> Unit)? = null,
    onArchiveEmail: (String) -> Unit = {},
    onDeleteEmail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(EditorialColors.Surface)
            .border(width = 1.dp, color = EditorialColors.Border)
    ) {
        // Top Bar Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedEmailIds.isNotEmpty()) {
                // Selection Mode Top Bar
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${selectedEmailIds.size} Selected",
                        style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = EditorialColors.Primary
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    
                    // Trash Selection Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onTrashSelection?.invoke() }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "d", 
                            style = AppTypography.labelSmall, 
                            modifier = Modifier
                                .border(1.dp, EditorialColors.Border, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("trash", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Clear Selection Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onClearSelection?.invoke() }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "esc", 
                            style = AppTypography.labelSmall, 
                            modifier = Modifier
                                .border(1.dp, EditorialColors.Border, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("clear", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                    }
                }
            } else {
                // Normal Mode Top Bar
                Text(
                    text = title,
                    style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = EditorialColors.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                
                if (onEmptyTrash != null && emails.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onEmptyTrash() }
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.Trash,
                            contentDescription = "Empty Trash",
                            tint = EditorialColors.TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Empty Trash", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                
                if (isExpanded) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onComposeClicked() }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "c", 
                            style = AppTypography.labelSmall, 
                            modifier = Modifier
                                .border(1.dp, EditorialColors.Border, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("compose", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                    }
                }
            }
        }
        
        HorizontalDivider(color = EditorialColors.Border, thickness = 1.dp)

        // List
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(emails) { email ->
                EmailListItem(
                    email = email,
                    isSelected = email.internalId == activeEmailId,
                    isMultiSelected = email.internalId in selectedEmailIds,
                    isExpanded = isExpanded,
                    canShowHoverActions = activeEmailId == null && selectedEmailIds.isEmpty(),
                    onClick = { onEmailSelected(email.internalId) },
                    onArchive = { onArchiveEmail(email.internalId) },
                    onDelete = { onDeleteEmail(email.internalId) }
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EmailListItem(
    email: NormalizedEmail,
    isSelected: Boolean,
    isMultiSelected: Boolean,
    isExpanded: Boolean,
    canShowHoverActions: Boolean = false,
    onClick: () -> Unit,
    onArchive: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
            .background(
                when {
                    isMultiSelected -> EditorialColors.SurfaceSelected.copy(alpha = 0.5f)
                    isSelected -> EditorialColors.SurfaceSelected
                    else -> EditorialColors.Surface
                }
            )
            .drawBehind {
                if (isSelected) {
                    drawRect(
                        color = EditorialColors.Primary,
                        topLeft = Offset.Zero,
                        size = Size(3.dp.toPx(), size.height)
                    )
                }
            }
            .border(width = 1.dp, color = EditorialColors.Border, shape = RoundedCornerShape(0.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = if (isExpanded) Alignment.CenterVertically else Alignment.Top
        ) {
            // Multi-Selection "x" indicator or Unread Indicator
            Box(
                modifier = Modifier
                    .padding(top = if (isExpanded) 0.dp else 4.dp, end = 12.dp)
                    .width(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isMultiSelected) {
                    Text(
                        text = "x",
                        style = AppTypography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EditorialColors.Primary
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (!email.isRead) EditorialColors.UnreadDot else EditorialColors.Background)
                    )
                }
            }
            
            if (isExpanded) {
                // Wide, single-row layout
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = email.senderName,
                        style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = EditorialColors.TextPrimary,
                        modifier = Modifier.width(180.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = email.subject,
                        style = AppTypography.bodyLarge,
                        color = EditorialColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "  —  ${email.snippet}",
                        style = AppTypography.bodyLarge,
                        color = EditorialColors.TextMuted,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    if (isHovered && canShowHoverActions) {
                        HoverActions(onArchive = onArchive, onDelete = onDelete)
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Text(
                        text = email.timestampStr,
                        style = AppTypography.labelMedium,
                        color = EditorialColors.TextMuted
                    )
                }
            } else {
                // Compact, multi-row layout
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
                        
                        if (isHovered && canShowHoverActions) {
                            HoverActions(onArchive = onArchive, onDelete = onDelete)
                        } else {
                            Text(
                                text = email.timestampStr,
                                style = AppTypography.labelMedium,
                                color = EditorialColors.TextMuted
                            )
                        }
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
    }
}

@Composable
private fun HoverActions(onArchive: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HoverActionItem(icon = AppIcons.Archive, shortcut = "e", onClick = onArchive)
        HoverActionItem(icon = AppIcons.Trash, shortcut = "d", onClick = onDelete)
    }
}

@Composable
private fun HoverActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, shortcut: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EditorialColors.TextMuted,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = shortcut,
            style = AppTypography.labelSmall,
            color = EditorialColors.TextMuted,
            modifier = Modifier
                .border(1.dp, EditorialColors.Border, RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        )
    }
}
