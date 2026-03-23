package codes.chirag.emailclient.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EditorialColors.Primary,
    background = EditorialColors.Background,
    surface = EditorialColors.Surface,
    onPrimary = EditorialColors.TextPrimary,
    onBackground = EditorialColors.TextPrimary,
    onSurface = EditorialColors.TextPrimary,
    error = EditorialColors.Error
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
