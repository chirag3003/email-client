package codes.chirag.emailclient

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

fun main() = application {
    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized,
        position = WindowPosition(Alignment.Center)
    )
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "Email Client",
        state = windowState,
        undecorated = true
    ) {
        App(
            onMinimize = { windowState.isMinimized = true },
            onMaximize = { 
                windowState.placement = if (windowState.placement == WindowPlacement.Maximized) {
                    WindowPlacement.Floating
                } else {
                    WindowPlacement.Maximized
                }
            },
            onClose = { exitApplication() },
            draggableArea = { modifier, content ->
                WindowDraggableArea(modifier) {
                    content()
                }
            }
        )
    }
}
