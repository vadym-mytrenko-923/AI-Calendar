package com.ai.calendar.demo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.ai.calendar.demo.R

val NumansFontFamily = FontFamily(
    Font(resId = R.font.numans, weight = FontWeight.Normal),
    Font(resId = R.font.numans, weight = FontWeight.Bold),
    Font(resId = R.font.numans, weight = FontWeight.ExtraBold)
)

fun provideTypography(appColorsScheme: AppColorsScheme) = Typography(
    // Should be uppercased
    headlineLarge = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = textSizeLargest,
        lineHeight = textLineHeightLargest,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    headlineMedium = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = textSizeLarge2X,
        lineHeight = textLineHeightLarge2X,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    headlineSmall = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = textSizeLarge1_5X,
        lineHeight = textLineHeightLarge,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    // Should be uppercased
    titleMedium = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = textSizeLarge,
        lineHeight = textLineHeightLarge,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    bodyLarge = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = textSizeMedium,
        lineHeight = textLineHeightMedium,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    bodyMedium = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = textSizeMedium,
        lineHeight = textLineHeightMedium,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    bodySmall = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = textSizeMedium,
        lineHeight = textLineHeightMedium,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    labelLarge = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = textSizeSmall,
        lineHeight = textLineHeightSmall,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    labelMedium = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = textSizeSmall,
        lineHeight = textLineHeightSmall,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
    // Should be uppercased
    labelSmall = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = textSizeExtraSmall,
        lineHeight = textLineHeightSmall,
        color = appColorsScheme.textPrimary,
        platformStyle = PlatformTextStyle(includeFontPadding = true)
    ),
)
