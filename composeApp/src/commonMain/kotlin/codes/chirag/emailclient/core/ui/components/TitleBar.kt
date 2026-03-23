package codes.chirag.emailclient.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.core.ui.theme.EditorialColors
import codes.chirag.emailclient.core.ui.theme.AppTypography

@Composable
fun TitleBar(
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    draggableArea: @Composable (Modifier, @Composable () -> Unit) -> Unit = { childModifier, content -> 
        Box(childModifier) { content() } 
    }
) {
    draggableArea(
        Modifier.fillMaxWidth()
            .height(32.dp)
            .background(EditorialColors.Background)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Email Client",
                style = AppTypography.labelSmall,
                color = EditorialColors.TextMuted
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Window Controls
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WindowControlSymbol(
                    symbol = "—",
                    onClick = onMinimize
                )
                WindowControlSymbol(
                    symbol = "□",
                    onClick = onMaximize
                )
                WindowControlSymbol(
                    symbol = "✕",
                    onClick = onClose,
                    isClose = true
                )
            }
        }
    }
}

@Composable
private fun WindowControlSymbol(
    symbol: String,
    onClick: () -> Unit,
    isClose: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(44.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            style = AppTypography.labelSmall,
            color = if (isClose) EditorialColors.Error else EditorialColors.TextMuted
        )
    }
}
