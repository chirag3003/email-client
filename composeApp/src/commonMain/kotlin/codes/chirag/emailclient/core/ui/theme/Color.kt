package codes.chirag.emailclient.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Softer Dark - Eye-Comfort Color Palette
 * A reduced-contrast dark-mode palette designed for long reading sessions.
 * Shifted from pure black to dark grey to reduce pupil dilation strain.
 */
object EditorialColors {
    // Primary & Accent: Used for focus outlines, active indicators, and primary actions
    val Primary = Color(0xFFE8742A)
    val Accent = Color(0xFFD4824A)

    // Backgrounds & Surfaces: Elevation hierarchy separated by subtle borders
    val Background = Color(0xFF1A1B1E)
    val Surface = Color(0xFF1E1F22)
    val SurfaceHover = Color(0xFF242528)
    val SurfaceSelected = Color(0xFF26272B)
    val SurfaceElevated = Color(0xFF2A2B30)

    // Typography: Three-tier hierarchy for clear content differentiation
    val TextPrimary = Color(0xFFE6E6E6)
    val TextSecondary = Color(0xFFA0A0A0)
    val TextMuted = Color(0xFF6E7076)

    // UI Elements
    val Border = Color(0xFF313235)
    val UnreadDot = Color(0xFFE8742A)

    // Functional Colors
    val Error = Color(0xFFE85555)

    // Focus: Keyboard focus indicator (40% opacity of Primary)
    val FocusRing = Color(0x66E8742A)
}
