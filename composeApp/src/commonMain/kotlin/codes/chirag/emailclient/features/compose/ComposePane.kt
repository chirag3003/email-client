package codes.chirag.emailclient.features.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codes.chirag.emailclient.shared.model.NormalizedEmail
import codes.chirag.emailclient.core.ui.AppIcons
import codes.chirag.emailclient.core.ui.theme.EditorialColors
import codes.chirag.emailclient.core.ui.theme.AppTypography
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ComposePane(
    draftEmail: NormalizedEmail? = null,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var toText by remember { mutableStateOf(draftEmail?.senderEmail ?: "jane@example.com, john@example.com") }
    var ccText by remember { mutableStateOf("") }
    var bccText by remember { mutableStateOf("") }
    var subjectText by remember { mutableStateOf(draftEmail?.subject ?: "What's this about?") }
    var bodyText by remember { mutableStateOf(draftEmail?.bodyText ?: "") }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(EditorialColors.Background)
            .border(width = 1.dp, color = EditorialColors.Border)
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = AppIcons.Close,
                contentDescription = "Close",
                tint = EditorialColors.TextMuted,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onClose() }
            )
        }

        HorizontalDivider(color = EditorialColors.Border, thickness = 1.dp)

        // Main Compose Area
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 64.dp, vertical = 48.dp)
        ) {
            ComposeField(label = "TO", value = toText, onValueChange = { toText = it })
            Spacer(modifier = Modifier.height(24.dp))
            
            ComposeField(label = "CC", value = ccText, onValueChange = { ccText = it })
            Spacer(modifier = Modifier.height(24.dp))
            
            ComposeField(label = "BCC", value = bccText, onValueChange = { bccText = it })
            Spacer(modifier = Modifier.height(24.dp))
            
            ComposeField(label = "SUBJECT", value = subjectText, onValueChange = { subjectText = it })
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Body Text
            Box(modifier = Modifier.weight(1f)) {
                if (bodyText.isEmpty()) {
                    Text(
                        text = "Start typing your message here...",
                        style = AppTypography.bodyLarge.copy(fontSize = 18.sp),
                        color = EditorialColors.TextMuted
                    )
                }
                BasicTextField(
                    value = bodyText,
                    onValueChange = { bodyText = it },
                    textStyle = AppTypography.bodyMedium.copy(
                        color = EditorialColors.TextPrimary,
                        fontSize = 18.sp,
                        lineHeight = 28.sp
                    ),
                    cursorBrush = SolidColor(EditorialColors.Primary),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        HorizontalDivider(color = EditorialColors.Border, thickness = 1.dp)

        // Footer Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Formatting Shortcuts
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                FormattingShortcut("B", "Cmd + B")
                FormattingShortcut("I", "Cmd + I", isItalic = true)
                FormattingShortcut("🔗", "Cmd + K") // Placeholder for link icon
            }

            // Right Actions
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = AppIcons.Trash,
                    contentDescription = "Discard Draft",
                    tint = EditorialColors.TextMuted,
                    modifier = Modifier.size(20.dp).clickable { onClose() }
                )
                
                // Send Button Combo
                Row(
                    modifier = Modifier
                        .height(36.dp)
                        .background(EditorialColors.Primary, androidx.compose.ui.graphics.RectangleShape)
                        .clickable { onClose() } // In a real app this would trigger send
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Send",
                            color = EditorialColors.Background,
                            style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(EditorialColors.Background.copy(alpha = 0.3f))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cmd+Enter",
                            color = EditorialColors.Background.copy(alpha = 0.8f),
                            style = AppTypography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComposeField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            color = EditorialColors.TextMuted,
            style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.width(80.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = AppTypography.bodyLarge.copy(color = EditorialColors.TextPrimary),
            cursorBrush = SolidColor(EditorialColors.Primary),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FormattingShortcut(icon: String, shortcut: String, isItalic: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = icon,
            color = EditorialColors.TextMuted,
            style = AppTypography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontStyle = if (isItalic) androidx.compose.ui.text.font.FontStyle.Italic else null
            )
        )
        
        // Render Cmd + X pills
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            val parts = shortcut.split(" + ")
            parts.forEachIndexed { index, part ->
                Text(
                    text = part,
                    color = EditorialColors.TextMuted,
                    style = AppTypography.labelSmall,
                    modifier = Modifier
                        .background(EditorialColors.SurfaceSelected, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
                if (index < parts.size - 1) {
                    Text("+", color = EditorialColors.TextMuted, style = AppTypography.labelSmall)
                }
            }
        }
    }
}
