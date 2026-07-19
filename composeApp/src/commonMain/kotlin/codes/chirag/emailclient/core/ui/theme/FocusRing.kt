package codes.chirag.emailclient.core.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.debugInspectorInfo

/**
 * Draws a visible keyboard focus ring around the element.
 * Only appears on keyboard focus, not mouse click.
 */
fun Modifier.focusRing(
    color: Color = EditorialColors.FocusRing,
    cornerRadius: Float = 8f,
    width: Float = 2f,
    offset: Float = 2f
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "focusRing"
        properties["color"] = color
        properties["cornerRadius"] = cornerRadius
    }
) {
    var isFocused by mutableStateOf(false)

    this
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }
        .drawBehind {
            if (isFocused) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(-offset, -offset),
                    size = Size(size.width + offset * 2, size.height + offset * 2),
                    cornerRadius = CornerRadius(cornerRadius),
                    style = Stroke(width = width)
                )
            }
        }
}
