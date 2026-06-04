package com.ai.calendar.demo.ui.core.composable.textField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.appColorsScheme

@Composable
fun TextFieldPrimary(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    label: String = "",
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    inputTransformation: InputTransformation? = null,
    onFocusChange: (Boolean) -> Unit = {},
) {
    BaseTextField(
        state = state,
        modifier = modifier,
        isError = isError,
        isEnabled = isEnabled,
        label = label,
        placeholder = placeholder,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.appColorsScheme.backgroundSecondary,
        errorContainerColor = MaterialTheme.colorScheme.background,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        trailingIcon = trailingIcon,
        inputTransformation = inputTransformation,
        onFocusChange = onFocusChange,
    )
}

@Preview
@Composable
private fun TextFieldPrimaryPreview() {
    AiCalendarTheme {
        TextFieldPrimary(
            state = TextFieldState(),
            label = "Label",
            placeholder = "Placeholder",
        )
    }
}

@Preview
@Composable
private fun TextFieldPrimaryTypedPreview() {
    AiCalendarTheme {
        TextFieldPrimary(
            state = TextFieldState("test text"),
            label = "Label",
            placeholder = "Placeholder",
        )
    }
}

@Preview
@Composable
private fun TextFieldPrimaryTypedErrorPreview() {
    AiCalendarTheme {
        TextFieldPrimary(
            state = TextFieldState("test text"),
            label = "Label",
            placeholder = "Placeholder",
            isError = true
        )
    }
}
