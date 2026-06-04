package com.ai.calendar.demo.ui.core.composable.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ai.calendar.demo.R
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.AppIcons
import com.ai.calendar.demo.ui.theme.appColorsScheme
import com.ai.calendar.demo.ui.theme.buttonIconSize
import com.ai.calendar.demo.ui.theme.textSizeExtraSmall
import com.ai.calendar.demo.ui.theme.textSizeMedium
import com.ai.calendar.demo.ui.theme.textSizeSmall
import com.ai.calendar.demo.ui.theme.textSizeSmallest

@Composable
fun BtnPrimary(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    text: String,
    hintText: String? = null,
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    tintLeadingContent: Boolean = true,
    tintTrailingContent: Boolean = true,
) {
    BaseButton(
        containerColor = MaterialTheme.appColorsScheme.btnPrimary,
        contentColor = MaterialTheme.appColorsScheme.btnPrimaryText,
        disabledContentColor = MaterialTheme.appColorsScheme.btnPrimaryTextDisabled,
        pressedContainerColor = MaterialTheme.appColorsScheme.btnPrimaryPressed,
        disabledContainerColor = MaterialTheme.appColorsScheme.btnPrimaryDisabled,
        modifier = modifier,
        isEnabled = isEnabled,
        applyShadow = true,
        onClick = onClick,
        textComponent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = text,
                    color = if (isEnabled) {
                        MaterialTheme.appColorsScheme.btnPrimaryText
                    } else {
                        MaterialTheme.appColorsScheme.btnPrimaryTextDisabled
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    autoSize = TextAutoSize.StepBased(minFontSize = textSizeExtraSmall, maxFontSize = textSizeMedium),
                )
                hintText?.let {
                    Text(
                        text = hintText,
                        color = if (isEnabled) {
                            MaterialTheme.appColorsScheme.btnPrimaryText.copy(alpha = 0.6f)
                        } else {
                            MaterialTheme.appColorsScheme.btnPrimaryTextDisabled
                        },
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(minFontSize = textSizeSmallest, maxFontSize = textSizeSmall)
                    )
                }
            }
        },
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        tintLeadingContent = tintLeadingContent,
        tintTrailingContent = tintTrailingContent,
    )
}

@Preview
@Composable
private fun BtnPrimaryPreview() {
    AiCalendarTheme {
        BtnPrimary(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = stringResource(R.string.done)
        )
    }
}

@Preview
@Composable
private fun BtnPrimaryPreviewTrailingIcon() {
    AiCalendarTheme {
        BtnPrimary(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = stringResource(R.string.done),
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
