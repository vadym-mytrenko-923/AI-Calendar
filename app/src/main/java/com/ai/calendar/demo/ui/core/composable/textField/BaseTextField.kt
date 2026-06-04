package com.ai.calendar.demo.ui.core.composable.textField

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.ai.calendar.demo.ui.theme.AppIcons
import com.ai.calendar.demo.ui.theme.appColorsScheme
import com.ai.calendar.demo.ui.theme.borderWidthDefault
import com.ai.calendar.demo.ui.theme.borderWidthThick
import com.ai.calendar.demo.ui.theme.inputHeight
import com.ai.calendar.demo.ui.theme.inputIconSize
import com.ai.calendar.demo.ui.theme.marginPrimary1_25X
import com.ai.calendar.demo.ui.theme.marginPrimary2X
import com.ai.calendar.demo.ui.theme.marginPrimary3X
import com.ai.calendar.demo.ui.theme.marginZero
import com.ai.calendar.demo.ui.theme.textFieldShapeDefault

@Composable
fun BaseTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    label: String = "",
    focusedContainerColor: Color = MaterialTheme.appColorsScheme.backgroundTertiary,
    unfocusedContainerColor: Color = MaterialTheme.appColorsScheme.backgroundTertiary,
    disabledContainerColor: Color = MaterialTheme.appColorsScheme.backgroundTertiary,
    errorContainerColor: Color = MaterialTheme.appColorsScheme.backgroundTertiary,
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    inputTransformation: InputTransformation? = null,
    onFocusChange: (Boolean) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val hasText = state.text.isNotEmpty()
    val hasLabel = label.isNotEmpty()
    val showLabelMinimized = isFocused || hasText

    LaunchedEffect(isFocused) {
        onFocusChange(isFocused)
    }

    // Border colors per state
    val borderColor = when {
        !isEnabled -> MaterialTheme.appColorsScheme.borderInputDisabled
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.appColorsScheme.borderInputFocused
        else -> MaterialTheme.appColorsScheme.borderInputDefault
    }

    val colors = colors(
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        disabledContainerColor = disabledContainerColor,
        errorContainerColor = errorContainerColor,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.primary,

        unfocusedTrailingIconColor = MaterialTheme.appColorsScheme.iconPrimary,
        focusedTrailingIconColor = MaterialTheme.appColorsScheme.iconPrimary,
        errorTrailingIconColor = MaterialTheme.appColorsScheme.iconPrimary,
        disabledTrailingIconColor = MaterialTheme.appColorsScheme.textTertiary,

        // Remove the default bottom indicator
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    )

    // Password visibility toggle
    val isPasswordField = keyboardOptions.keyboardType == KeyboardType.Password
    var passwordVisible by rememberSaveable { mutableStateOf(!isPasswordField) }
    val textObfuscationMode = if (passwordVisible) {
        TextObfuscationMode.Visible
    } else {
        TextObfuscationMode.RevealLastTyped
    }
    val hasTrailingIcon = isPasswordField || trailingIcon != null

    Box(
        modifier = modifier
            .height(inputHeight)
            .clip(textFieldShapeDefault)
            .border(
                width = if (isError) borderWidthThick else borderWidthDefault,
                color = borderColor,
                shape = textFieldShapeDefault
            )
    ) {
        SecureTextField(
            modifier = Modifier.fillMaxSize(),
            state = state,
            enabled = isEnabled,
            isError = isError,
            textObfuscationMode = textObfuscationMode,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = if (isEnabled) MaterialTheme.appColorsScheme.textPrimary else MaterialTheme.appColorsScheme.textPrimaryDisabled
            ),
            inputTransformation = inputTransformation,
            interactionSource = interactionSource,
            shape = textFieldShapeDefault,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            label = if (hasLabel) {
                {
                    Text(
                        text = if (showLabelMinimized) label.uppercase() else label,
                        style = if (showLabelMinimized) {
                            MaterialTheme.typography.labelSmall
                        } else {
                            MaterialTheme.typography.bodySmall
                        },
                        color = if (showLabelMinimized) {
                            MaterialTheme.appColorsScheme.textTertiary
                        } else {
                            if (isEnabled) MaterialTheme.appColorsScheme.textSecondary else MaterialTheme.appColorsScheme.textTertiary
                        },
                        maxLines = 1
                    )
                }
            } else {
                null
            },
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isEnabled) MaterialTheme.appColorsScheme.textSecondary else MaterialTheme.appColorsScheme.textTertiary,
                )
            },
            colors = colors,
            contentPadding = PaddingValues(
                start = marginPrimary3X,
                end = if (hasTrailingIcon) marginZero else marginPrimary2X,
                top = if (hasLabel && showLabelMinimized) marginPrimary1_25X else marginZero
            ),
            trailingIcon = if (isPasswordField) {
                {
                    IconButton(
                        modifier = Modifier.padding(end = marginPrimary2X),
                        onClick = { passwordVisible = !passwordVisible },
                        enabled = isEnabled
                    ) {
                        Icon(
                            modifier = Modifier.size(inputIconSize),
                            painter = painterResource(
                                id = if (passwordVisible) {
                                    AppIcons.PasswordVisible
                                } else {
                                    AppIcons.PasswordNotVisible
                                }
                            ),
                            contentDescription = null
                        )
                    }
                }
            } else {
                trailingIcon
            },
        )
    }
}
