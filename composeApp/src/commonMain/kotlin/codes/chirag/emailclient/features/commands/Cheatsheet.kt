package codes.chirag.emailclient.features.commands

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codes.chirag.emailclient.core.ui.theme.AppTypography
import codes.chirag.emailclient.core.ui.theme.EditorialColors

data class ShortcutInfo(val label: String, val keys: String)

@Composable
fun Cheatsheet(
    onDismiss: () -> Unit
) {
    val navigationShortcuts = listOf(
        ShortcutInfo("Next Email", "j"),
        ShortcutInfo("Previous Email", "k"),
        ShortcutInfo("Go to Inbox", "g i"),
        ShortcutInfo("Go to Sent", "g s"),
        ShortcutInfo("Go to Drafts", "g d"),
        ShortcutInfo("Go to Archive", "g a"),
        ShortcutInfo("Go to Trash", "g t"),
    )

    val actionShortcuts = listOf(
        ShortcutInfo("Compose", "c"),
        ShortcutInfo("Archive", "e"),
        ShortcutInfo("Delete / Move to Trash", "d"),
        ShortcutInfo("Restore (from Trash)", "r"),
        ShortcutInfo("Command Palette", "Ctrl + K"),
        ShortcutInfo("Cheatsheet", "?"),
        ShortcutInfo("Back / Deselect", "Esc"),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EditorialColors.Background.copy(alpha = 0.85f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(700.dp)
                .clickable(enabled = false) {},
            shape = RoundedCornerShape(12.dp),
            color = EditorialColors.Surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialColors.Border)
        ) {
            Column(modifier = Modifier.padding(32.dp)) {
                Text(
                    text = "Keyboard Shortcuts",
                    style = AppTypography.bodyLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    color = EditorialColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Column(modifier = Modifier.weight(1f).border(1.dp, EditorialColors.Border)) {
                        ShortcutTableHeader("Navigation")
                        navigationShortcuts.forEach { ShortcutTableRow(it) }
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column(modifier = Modifier.weight(1f).border(1.dp, EditorialColors.Border)) {
                        ShortcutTableHeader("Actions")
                        actionShortcuts.forEach { ShortcutTableRow(it) }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Press Esc to close",
                    style = AppTypography.labelSmall,
                    color = EditorialColors.TextMuted,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun ShortcutTableHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(EditorialColors.SurfaceSelected)
            .padding(12.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = AppTypography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = EditorialColors.TextMuted
        )
    }
    HorizontalDivider(color = EditorialColors.Border, thickness = 1.dp)
}

@Composable
private fun ShortcutTableRow(shortcut: ShortcutInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = shortcut.label,
            style = AppTypography.bodyMedium,
            color = EditorialColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = shortcut.keys,
            style = AppTypography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = EditorialColors.Primary,
            modifier = Modifier
                .border(1.dp, EditorialColors.Border, RoundedCornerShape(4.dp))
                .background(EditorialColors.Background)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
    HorizontalDivider(color = EditorialColors.Border, thickness = 1.dp)
}
