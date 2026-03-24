package codes.chirag.emailclient.features.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codes.chirag.emailclient.core.ui.theme.EditorialColors
import codes.chirag.emailclient.core.ui.theme.AppTypography
import codes.chirag.emailclient.shared.model.User

@Composable
fun ProfileMenu(
    user: User?,
    onLogout: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (user == null) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss)
    ) {
        Column(
            modifier = modifier
                .align(Alignment.BottomStart)
                .padding(start = 72.dp, bottom = 16.dp)
                .width(240.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EditorialColors.SurfaceSelected)
                .border(1.dp, EditorialColors.Border, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = EditorialColors.TextPrimary
            )
            Text(
                text = user.email,
                style = AppTypography.bodyLarge.copy(fontSize = 14.sp),
                color = EditorialColors.TextMuted,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider(color = EditorialColors.Border, modifier = Modifier.padding(vertical = 4.dp))

            Text(
                text = "Log Out",
                style = AppTypography.bodyLarge,
                color = EditorialColors.Error,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        onLogout()
                        onDismiss()
                    }
                    .padding(vertical = 12.dp)
            )
        }
    }
}
