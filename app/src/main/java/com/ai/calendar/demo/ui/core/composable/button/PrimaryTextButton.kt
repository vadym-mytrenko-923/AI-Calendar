package com.ai.calendar.demo.ui.core.composable.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.appColorsScheme
import com.ai.calendar.demo.ui.theme.marginPrimary

@Composable
fun PrimaryTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    defaultColor: Color = MaterialTheme.appColorsScheme.textPrimary,
    pressedColor: Color = MaterialTheme.appColorsScheme.textSecondary,
    disabledColor: Color = MaterialTheme.appColorsScheme.textPrimaryDisabled,
    contentPadding: PaddingValues = PaddingValues(horizontal = marginPrimary, vertical = marginPrimary),
) {
    BaseTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        isEnabled = isEnabled,
        contentPadding = contentPadding,
        defaultColor = defaultColor,
        pressedColor = pressedColor,
        disabledColor = disabledColor,
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun PrimaryTextButtonPreview() {
    AiCalendarTheme {
        PrimaryTextButton(text = "Test button", onClick = {})
    }
}
