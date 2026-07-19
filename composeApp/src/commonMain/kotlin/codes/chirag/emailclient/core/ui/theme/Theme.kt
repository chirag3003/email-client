package codes.chirag.emailclient.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EditorialColors.Primary,
    background = EditorialColors.Background,
    surface = EditorialColors.Surface,
    surfaceVariant = EditorialColors.SurfaceSelected,
    surfaceTint = EditorialColors.SurfaceElevated,
    onPrimary = EditorialColors.Background,
    onBackground = EditorialColors.TextPrimary,
    onSurface = EditorialColors.TextPrimary,
    onSurfaceVariant = EditorialColors.TextSecondary,
    error = EditorialColors.Error,
    outline = EditorialColors.Border,
    outlineVariant = EditorialColors.Border
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography(),
        content = content
    )
}
