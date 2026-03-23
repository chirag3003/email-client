package codes.chirag.emailclient.features.mail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codes.chirag.emailclient.core.domain.FolderType
import codes.chirag.emailclient.core.domain.NormalizedEmail
import codes.chirag.emailclient.core.ui.AppIcons
import codes.chirag.emailclient.core.ui.theme.EditorialColors
import codes.chirag.emailclient.core.ui.theme.AppTypography

@Composable
fun EmailDetailPane(
    email: NormalizedEmail?,
    onComposeClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onArchive: (String) -> Unit = {},
    onDelete: (String) -> Unit = {},
    onRestore: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(EditorialColors.Background)
            .border(width = 1.dp, color = EditorialColors.Border)
    ) {
        // Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = AppIcons.Close,
                    contentDescription = "Close",
                    tint = EditorialColors.TextMuted,
                    modifier = Modifier.size(20.dp).clickable { onCloseClicked() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                
                if (email?.folder == FolderType.TRASH) {
                    Icon(
                        imageVector = AppIcons.Inbox,
                        contentDescription = "Restore",
                        tint = EditorialColors.TextMuted,
                        modifier = Modifier.size(20.dp).clickable { email?.let { onRestore(it.internalId) } }
                    )
                } else {
                    Icon(
                        imageVector = AppIcons.Archive,
                        contentDescription = "Archive",
                        tint = EditorialColors.TextMuted,
                        modifier = Modifier.size(20.dp).clickable { email?.let { onArchive(it.internalId) } }
                    )
                    Icon(
                        imageVector = AppIcons.Trash,
                        contentDescription = "Trash",
                        tint = EditorialColors.TextMuted,
                        modifier = Modifier.size(20.dp).clickable { email?.let { onDelete(it.internalId) } }
                    )
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (email?.folder == FolderType.TRASH) {
                    Text("r", style = AppTypography.labelSmall, modifier = shortcutModifier())
                    Text("restore", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                } else {
                    Text("e", style = AppTypography.labelSmall, modifier = shortcutModifier())
                    Text("archive", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                    
                    Text("d", style = AppTypography.labelSmall, modifier = shortcutModifier())
                    Text("delete", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text("k", style = AppTypography.labelSmall, modifier = shortcutModifier())
                Text("up", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                
                Text("j", style = AppTypography.labelSmall, modifier = shortcutModifier())
                Text("down", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onComposeClicked() }
                        .padding(4.dp)
                ) {
                    Text("c", style = AppTypography.labelSmall, modifier = shortcutModifier())
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("compose", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                }
            }
        }
        
        HorizontalDivider(color = EditorialColors.Border, thickness = 1.dp)

        // Email Content
        if (email != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(48.dp)
            ) {
                Text(
                    text = email.subject,
                    style = AppTypography.bodyLarge.copy(fontSize = 32.sp, fontWeight = FontWeight.SemiBold),
                    color = EditorialColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Sender Avatar (Initials)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, EditorialColors.Border, RoundedCornerShape(8.dp))
                                .background(EditorialColors.SurfaceSelected),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = email.senderName.take(2).uppercase(),
                                color = EditorialColors.TextPrimary,
                                style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = email.senderName,
                                    color = EditorialColors.TextPrimary,
                                    style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "<${email.senderEmail}>",
                                    color = EditorialColors.TextMuted,
                                    style = AppTypography.bodyLarge
                                )
                            }
                            Text(
                                text = "to me",
                                color = EditorialColors.TextMuted,
                                style = AppTypography.labelMedium
                            )
                        }
                    }
                    
                    Text(
                        text = "${email.timestampStr}, ${email.timestampStr}", // Temporary fallback
                        color = EditorialColors.TextMuted,
                        style = AppTypography.labelMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                HorizontalDivider(color = EditorialColors.Border, thickness = 1.dp)
                Spacer(modifier = Modifier.height(32.dp))
                
                // Email Body text
                Text(
                    text = email.bodyText,
                    style = AppTypography.bodyMedium.copy(lineHeight = 28.sp, fontSize = 16.sp),
                    color = EditorialColors.TextPrimary
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No email selected",
                    color = EditorialColors.TextMuted,
                    style = AppTypography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun shortcutModifier() = Modifier
    .border(1.dp, EditorialColors.Border, RoundedCornerShape(4.dp))
    .padding(horizontal = 6.dp, vertical = 2.dp)
