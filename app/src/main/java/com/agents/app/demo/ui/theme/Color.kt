package com.agents.app.demo.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object AppColorsLight {
    // Default Colors
    val White = Color(0xFFFFFFFF)
    val Error = Color(0xFFE53935)
    val Warning = Color(0xFFF6BC2B)
    val Success = Color(0xFF61D782)

    // Primary
    val Primary = Color(0xFF4CAF50)
    val PrimaryActive = Color(0xFF388E3C)
    val PrimaryDisabled = Color(0xFF388E3C)

    // Backgrounds & Surfaces
    val Background = Color(0xFFF9FAFB)
    val BackgroundSecondary = Color(0xFFFCF8FC)
    val BackgroundTertiary = Color(0xFFF5F5FF)
    val BackgroundSuccess = Success

    // Borders
    val BorderInputDefault = Color(0xFFD7D6FF)
    val BorderInputFocused = Primary
    val BorderInputDisabled = Color(0xFFF5F5FF)
    val BorderCardDefault = Color(0x1A3831FF)
    val BorderIconButton = Color(0xFFD7D6FF)

    // Text
    val TextPrimary = Color(0xFF212121)
    val TextPrimaryDisabled = Color(0x4D202020)
    val TextSecondary = Color(0xFF616161)
    val TextTertiary = Color(0xFFB8BABC)
    val TextDark = Color(0xFF141414)
    val TextPrimaryOnDark = Color(0xFFFFFFFF)
    val TextPrimaryOnDarkActive = Color(0x80FFFFFF)
    val TextPrimaryOnDarkDisabled = Color(0x80FFFFFF)

    // Buttons
    val ButtonPrimary = Primary
    val ButtonPrimaryActive = PrimaryActive
    val ButtonPrimaryDisabled = PrimaryDisabled
    val ButtonPrimaryShadow = Color(0xFF2E7D32)
    val ButtonPrimaryOutline = Color(0xFFFFFFFF)
    val ButtonPrimaryOutlineActive = Color(0xFFB8BABC)
    val ButtonPrimaryOutlineDisabled = Color(0xFF898C8F)
    val ButtonPrimaryOutlineBorder = Color(0x1A202020)

    // Icons
    val IconPrimary = Color(0xFF06051A)

    // Icons
    val IconOnDark = Color(0xFFFFFFFFF)

    // Misc
    val CardShadow = Color(0x0F000000)
}

object AppColorsDark {
    // Default Colors
    val White = Color(0xFFFFFFFF)
    val Error = Color(0xFFE53935)

    // Primary
    val Primary = Color(0xFF36883A)
    val PrimaryDark = Color(0xFF216B26)

    // Backgrounds & Surfaces
    val Background = Color(0xFF494949)
    val ItemBackground = White

    // Text
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFAFAFAF)
    val TextDark = Color(0xFF141414)

    // Buttons
    val ButtonPrimaryDisabled = Color(0x33607D8B)
}

@Immutable
data class AppColorsScheme(
    val transparent: Color = Color.Transparent,
    val error: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val backgroundSecondary: Color = Color.Unspecified,
    val backgroundTertiary: Color = Color.Unspecified,
    val backgroundSuccess: Color = Color.Unspecified,
    val textPrimary: Color = Color.Unspecified,
    val textPrimaryDisabled: Color = Color.Unspecified,
    val textSecondary: Color = Color.Unspecified,
    val textTertiary: Color = Color.Unspecified,
    val textDark: Color = Color.Unspecified,
    val textPrimaryOnDark: Color = Color.Unspecified,
    val textPrimaryOnDarkActive: Color = Color.Unspecified,
    val textPrimaryOnDarkDisabled: Color = Color.Unspecified,
    val btnPrimaryText: Color = Color.Unspecified,
    val btnPrimaryTextPressed: Color = Color.Unspecified,
    val btnPrimaryTextDisabled: Color = Color.Unspecified,
    val btnPrimary: Color = Color.Unspecified,
    val btnPrimaryPressed: Color = Color.Unspecified,
    val btnPrimaryDisabled: Color = Color.Unspecified,
    val btnPrimaryShadow: Color = Color.Unspecified,
    val btnPrimaryOutline: Color = Color.Unspecified,
    val btnPrimaryOutlinePressed: Color = Color.Unspecified,
    val btnPrimaryOutlineDisabled: Color = Color.Unspecified,
    val btnPrimaryOutlineBorder: Color = Color.Unspecified,
    val borderIconBtn: Color = Color.Unspecified,
    val borderInputDefault: Color = Color.Unspecified,
    val borderInputFocused: Color = Color.Unspecified,
    val borderInputDisabled: Color = Color.Unspecified,
    val iconPrimary: Color = Color.Unspecified,
    val iconOnDark: Color = Color.Unspecified,
    val cardShadow: Color = Color.Unspecified,
)
