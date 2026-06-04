package com.agents.app.demo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AppColorsDark.Primary,
    secondary = AppColorsDark.PrimaryDark,
    background = AppColorsDark.Background,
)

private val LightColorScheme = lightColorScheme(
    primary = AppColorsLight.Primary,
    secondary = AppColorsLight.PrimaryActive,
    background = AppColorsLight.Background,
)

private val LightAppColorsScheme = AppColorsScheme(
    error = AppColorsLight.Error,
    background = AppColorsLight.Background,
    backgroundSuccess = AppColorsLight.BackgroundSuccess,
    textPrimary = AppColorsLight.TextPrimary,
    textSecondary = AppColorsLight.TextSecondary,
    textTertiary = AppColorsLight.TextTertiary,
    textPrimaryDisabled = AppColorsLight.TextPrimaryDisabled,
    textDark = AppColorsLight.TextDark,
    textPrimaryOnDark = AppColorsLight.TextPrimaryOnDark,
    textPrimaryOnDarkActive = AppColorsLight.TextPrimaryOnDarkActive,
    textPrimaryOnDarkDisabled = AppColorsLight.TextPrimaryOnDarkDisabled,
    btnPrimaryText = AppColorsLight.TextPrimaryOnDark,
    btnPrimaryTextDisabled = AppColorsLight.TextPrimaryOnDarkActive,
    btnPrimary = AppColorsLight.ButtonPrimary,
    btnPrimaryPressed = AppColorsLight.ButtonPrimaryActive,
    btnPrimaryDisabled = AppColorsLight.ButtonPrimaryDisabled,
    btnPrimaryShadow = AppColorsLight.ButtonPrimaryShadow,
    btnPrimaryOutline = AppColorsLight.ButtonPrimaryOutline,
    btnPrimaryOutlinePressed = AppColorsLight.ButtonPrimaryOutlineActive,
    btnPrimaryOutlineDisabled = AppColorsLight.ButtonPrimaryOutlineDisabled,
    btnPrimaryOutlineBorder = AppColorsLight.ButtonPrimaryOutlineBorder,
    backgroundSecondary = AppColorsLight.BackgroundSecondary,
    backgroundTertiary = AppColorsLight.BackgroundTertiary,
    borderInputDefault = AppColorsLight.BorderInputDefault,
    borderInputFocused = AppColorsLight.BorderInputFocused,
    borderInputDisabled = AppColorsLight.BorderInputDisabled,
    iconPrimary = AppColorsLight.IconPrimary,
    iconOnDark = AppColorsLight.IconOnDark,
    cardShadow = AppColorsLight.CardShadow,
    borderIconBtn = AppColorsLight.BorderIconButton,
    calendarTodayAccent = AppColorsLight.CalendarTodayAccent,
)

private val DarkAppColorsScheme = AppColorsScheme(
    error = AppColorsDark.Error,
    background = AppColorsDark.Background,
    textPrimary = AppColorsDark.TextPrimary,
    textSecondary = AppColorsDark.TextSecondary,
    textDark = AppColorsDark.TextDark,
    btnPrimaryText = AppColorsDark.TextPrimary,
    btnPrimaryDisabled = AppColorsDark.ButtonPrimaryDisabled,
    calendarTodayAccent = AppColorsDark.CalendarTodayAccent,
)

private val LocalAppColorsScheme = staticCompositionLocalOf { AppColorsScheme() }

@Composable
fun AiAgenticAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+, use it carefully and only when customer requires it, since dynamic theme overrides colors that are described above
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val appColorsScheme = if (darkTheme) DarkAppColorsScheme else LightAppColorsScheme

    CompositionLocalProvider(LocalAppColorsScheme provides appColorsScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = provideTypography(appColorsScheme),
            content = content,
        )
    }
}

val MaterialTheme.appColorsScheme: AppColorsScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColorsScheme.current
