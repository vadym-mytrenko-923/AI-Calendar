package com.ai.calendar.demo.ui.core.composable.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ai.calendar.demo.R
import com.ai.calendar.demo.ui.core.modifier.modifyIf
import com.ai.calendar.demo.ui.core.modifier.shadowGlow
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.AppIcons
import com.ai.calendar.demo.ui.theme.appColorsScheme
import com.ai.calendar.demo.ui.theme.btnCornerRadius
import com.ai.calendar.demo.ui.theme.buttonHeight
import com.ai.calendar.demo.ui.theme.buttonIconSize
import com.ai.calendar.demo.ui.theme.buttonShapeDefault
import com.ai.calendar.demo.ui.theme.marginPrimary
import com.ai.calendar.demo.ui.theme.marginPrimary2X
import com.ai.calendar.demo.ui.theme.marginPrimaryHalf1_75X

@Composable
fun BaseButton(
    modifier: Modifier,
    isEnabled: Boolean,
    containerColor: Color,
    contentColor: Color,
    pressedContainerColor: Color,
    disabledContainerColor: Color,
    disabledContentColor: Color,
    onClick: () -> Unit,
    textComponent: @Composable () -> Unit = {},
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    tintLeadingContent: Boolean = true,
    tintTrailingContent: Boolean = true,
    applyShadow: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentContainerColor by remember(isPressed, isEnabled, containerColor, pressedContainerColor) {
        derivedStateOf {
            if (isEnabled && isPressed) {
                pressedContainerColor
            } else {
                containerColor
            }
        }
    }

    Button(
        modifier = modifier
            .height(buttonHeight)
            .modifyIf(applyShadow && isEnabled && !isPressed) {
                shadowGlow(
                    color = MaterialTheme.appColorsScheme.btnPrimaryShadow,
                    blurRadius = marginPrimary2X,
                    offsetY = marginPrimaryHalf1_75X,
                    borderRadius = btnCornerRadius
                )
            },
        enabled = isEnabled,
        shape = buttonShapeDefault,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = currentContainerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(marginPrimary)
        ) {
            if (tintLeadingContent) {
                leadingContent()
            } else {
                CompositionLocalProvider(LocalContentColor provides Color.Unspecified) {
                    leadingContent()
                }
            }

            textComponent()

            if (tintTrailingContent) {
                trailingContent()
            } else {
                CompositionLocalProvider(LocalContentColor provides Color.Unspecified) {
                    trailingContent()
                }
            }
        }
    }
}

@Composable
@Preview
private fun ButtonPreview() {
    AiCalendarTheme {
        BaseButton(
            modifier = Modifier,
            isEnabled = true,
            applyShadow = true,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.appColorsScheme.textPrimary,
            disabledContentColor = MaterialTheme.appColorsScheme.textPrimaryDisabled,
            pressedContainerColor = MaterialTheme.appColorsScheme.btnPrimaryPressed,
            disabledContainerColor = MaterialTheme.appColorsScheme.btnPrimaryDisabled,
            onClick = {},
            textComponent = {
                Text(
                    text = stringResource(id = R.string.done),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
        )
    }
}

@Composable
@Preview
private fun ButtonPreviewDisabled() {
    AiCalendarTheme {
        BaseButton(
            modifier = Modifier,
            isEnabled = false,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.appColorsScheme.textPrimary,
            disabledContentColor = MaterialTheme.appColorsScheme.textPrimaryDisabled,
            pressedContainerColor = MaterialTheme.appColorsScheme.btnPrimaryPressed,
            disabledContainerColor = MaterialTheme.appColorsScheme.btnPrimaryDisabled,
            onClick = {},
            textComponent = {
                Text(
                    text = stringResource(id = R.string.done),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        )
    }
}

@Composable
@Preview
private fun ButtonWithIconPreview() {
    AiCalendarTheme {
        BaseButton(
            modifier = Modifier,
            isEnabled = true,
            applyShadow = true,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.appColorsScheme.textPrimary,
            disabledContentColor = MaterialTheme.appColorsScheme.textPrimaryDisabled,
            pressedContainerColor = MaterialTheme.appColorsScheme.btnPrimaryPressed,
            disabledContainerColor = MaterialTheme.appColorsScheme.btnPrimaryDisabled,
            onClick = {},
            textComponent = {
                Text(
                    text = stringResource(id = R.string.done),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            trailingContent = {
                Icon(
                    modifier = Modifier.size(buttonIconSize),
                    painter = painterResource(id = AppIcons.Retry),
                    contentDescription = stringResource(id = R.string.done)
                )
            }
        )
    }
}
