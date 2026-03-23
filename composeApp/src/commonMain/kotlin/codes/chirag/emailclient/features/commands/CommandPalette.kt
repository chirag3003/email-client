package codes.chirag.emailclient.features.commands

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codes.chirag.emailclient.core.domain.Command
import codes.chirag.emailclient.core.ui.theme.AppTypography
import codes.chirag.emailclient.core.ui.theme.EditorialColors

@Composable
fun CommandPalette(
    query: String,
    onQueryChange: (String) -> Unit,
    commands: List<Command>,
    selectedIndex: Int,
    onCommandClick: (Command) -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EditorialColors.Background.copy(alpha = 0.7f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier
                .padding(top = 100.dp)
                .width(600.dp)
                .clickable(enabled = false) {}, // Prevent clicks from dismissing
            shape = RoundedCornerShape(8.dp),
            color = EditorialColors.Surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialColors.Border)
        ) {
            Column {
                // Input Field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Type a command...",
                            style = AppTypography.bodyLarge,
                            color = EditorialColors.TextMuted
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        textStyle = AppTypography.bodyLarge.copy(color = EditorialColors.TextPrimary),
                        cursorBrush = SolidColor(EditorialColors.Primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true
                    )
                }

                if (commands.isNotEmpty()) {
                    androidx.compose.material3.HorizontalDivider(color = EditorialColors.Border)
                    
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 400.dp)
                    ) {
                        itemsIndexed(commands) { index, command ->
                            CommandItem(
                                command = command,
                                isSelected = index == selectedIndex,
                                onClick = { onCommandClick(command) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommandItem(
    command: Command,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) EditorialColors.SurfaceSelected else EditorialColors.Surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = command.label,
            style = AppTypography.bodyLarge,
            color = if (isSelected) EditorialColors.Primary else EditorialColors.TextPrimary
        )
        
        if (command.shortcut != null) {
            Text(
                text = command.shortcut,
                style = AppTypography.labelSmall,
                color = EditorialColors.TextMuted,
                modifier = Modifier
                    .border(1.dp, EditorialColors.Border, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
