package com.ai.calendar.demo.ui.core.composable.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    contentPadding: PaddingValues = PaddingValues(horizontal = marginPrimary, vertical = marginPrimary),
) {
    BaseTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        isEnabled = isEnabled,
        contentPadding = contentPadding,
        defaultColor = MaterialTheme.appColorsScheme.textPrimary,
        pressedColor = MaterialTheme.appColorsScheme.textSecondary,
        disabledColor = MaterialTheme.appColorsScheme.textPrimaryDisabled
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun PrimaryTextButtonPreview() {
    AiCalendarTheme {
        PrimaryTextButton(text = "Test button", onClick = {})
    }
}
