package com.ai.calendar.demo.ui.core.composable.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ai.calendar.demo.ui.theme.textSizeExtraSmall
import com.ai.calendar.demo.ui.theme.textSizeMedium

@Composable
fun BaseTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    contentPadding: PaddingValues,
    defaultColor: Color,
    pressedColor: Color,
    disabledColor: Color,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentTextColor by remember(isPressed, isEnabled, defaultColor, pressedColor, disabledColor) {
        derivedStateOf {
            when {
                !isEnabled -> disabledColor
                isPressed -> pressedColor
                else -> defaultColor
            }
        }
    }

    TextButton(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClick,
        interactionSource = interactionSource,
        contentPadding = contentPadding,
        colors = ButtonDefaults.textButtonColors(
            contentColor = currentTextColor,
            disabledContentColor = disabledColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = currentTextColor,
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(minFontSize = textSizeExtraSmall, maxFontSize = textSizeMedium),
        )
    }
}
