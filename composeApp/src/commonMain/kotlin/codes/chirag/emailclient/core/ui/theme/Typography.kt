package codes.chirag.emailclient.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import emailclient.composeapp.generated.resources.Inter_Regular
import emailclient.composeapp.generated.resources.Inter_Medium
import emailclient.composeapp.generated.resources.Inter_SemiBold
import emailclient.composeapp.generated.resources.Inter_Bold
import emailclient.composeapp.generated.resources.Literata_Regular
import emailclient.composeapp.generated.resources.Literata_Italic
import emailclient.composeapp.generated.resources.JetBrainsMono_Regular
import emailclient.composeapp.generated.resources.Res

@Composable
fun AppFontFamily(): FontFamily = FontFamily(
    Font(Res.font.Inter_Regular, FontWeight.Normal),
    Font(Res.font.Inter_Medium, FontWeight.Medium),
    Font(Res.font.Inter_SemiBold, FontWeight.SemiBold),
    Font(Res.font.Inter_Bold, FontWeight.Bold),
)

@Composable
fun ReadingFontFamily(): FontFamily = FontFamily(
    Font(Res.font.Literata_Regular, FontWeight.Normal, FontStyle.Normal),
    Font(Res.font.Literata_Italic, FontWeight.Normal, FontStyle.Italic),
)

@Composable
fun MonoFontFamily(): FontFamily = FontFamily(
    Font(Res.font.JetBrainsMono_Regular, FontWeight.Normal),
)

@Composable
fun AppTypography(): Typography {
    val appFont = AppFontFamily()
    val readingFont = ReadingFontFamily()
    val monoFont = MonoFontFamily()

    return Typography(
        headlineSmall = TextStyle(
            fontFamily = appFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = appFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.01).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = readingFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 26.sp,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontFamily = appFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        labelMedium = TextStyle(
            fontFamily = appFont,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.02.sp
        ),
        labelSmall = TextStyle(
            fontFamily = monoFont,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        )
    )
}
