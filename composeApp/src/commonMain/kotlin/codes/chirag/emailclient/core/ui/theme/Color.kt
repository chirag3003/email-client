package codes.chirag.emailclient.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Refined Editorial - Orange Accent Color Palette
 * A sharp, minimal dark-mode palette with a high-contrast editorial aesthetic.
 */
object EditorialColors {
    // Primary & Accent: Used for focus outlines, active indicators, and primary actions
    val Primary = Color(0xFFFF5A00)
    val Accent = Color(0xFFFF5A00) 
    
    // Backgrounds & Surfaces: Flat hierarchy separated by thin borders
    val Background = Color(0xFF121212)
    val Surface = Color(0xFF121212)
    val SurfaceSelected = Color(0xFF1A1A1A) // Used for active list items 
    
    // Typography
    val TextPrimary = Color(0xFFEAEAEA) // Primary reading text, active items
    val TextMuted = Color(0xFF888888)    // Timestamps, placeholders, unselected items 
    
    // UI Elements
    val Border = Color(0xFF2A2A2A)      // Ultra-thin 1px pane separators
    val UnreadDot = Color(0xFFFF5A00) 
    
    // Functional Colors
    val Error = Color(0xFFFF3333)
}
