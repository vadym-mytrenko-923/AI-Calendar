package com.agents.app.demo.ui.core.composable.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.R
import com.agents.app.demo.ui.theme.AppIcons
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.appColorsScheme
import com.agents.app.demo.ui.theme.borderWidthDefault
import com.agents.app.demo.ui.theme.buttonIconSize
import com.agents.app.demo.ui.theme.buttonShapeDefault
import com.agents.app.demo.ui.theme.textSizeExtraSmall
import com.agents.app.demo.ui.theme.textSizeMedium

@Composable
fun BtnPrimaryOutline(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    text: String,
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    tintLeadingContent: Boolean = true,
    tintTrailingContent: Boolean = true,
    isTextBold: Boolean = true
) {
    BaseButton(
        modifier = modifier.border(
            width = borderWidthDefault,
            color = if (isEnabled) {
                MaterialTheme.appColorsScheme.btnPrimaryOutlineBorder
            } else {
                MaterialTheme.appColorsScheme.btnPrimaryOutlineDisabled
            },
            shape = buttonShapeDefault
        ),
        containerColor = MaterialTheme.appColorsScheme.btnPrimaryOutline,
        contentColor = MaterialTheme.appColorsScheme.textPrimary,
        disabledContentColor = MaterialTheme.appColorsScheme.textPrimaryDisabled,
        pressedContainerColor = MaterialTheme.appColorsScheme.btnPrimaryOutlinePressed,
        disabledContainerColor = MaterialTheme.appColorsScheme.btnPrimaryOutlineDisabled,
        isEnabled = isEnabled,
        onClick = onClick,
        textComponent = {
            Text(
                text = text,
                color = if (isEnabled) MaterialTheme.appColorsScheme.textPrimary else MaterialTheme.appColorsScheme.textPrimaryDisabled,
                style = if (isTextBold) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodySmall,
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(minFontSize = textSizeExtraSmall, maxFontSize = textSizeMedium),
            )
        },
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        tintLeadingContent = tintLeadingContent,
        tintTrailingContent = tintTrailingContent,
    )
}

@Preview
@Composable
private fun BtnPrimaryOutlinePreview() {
    AiAgenticAppTheme {
        BtnPrimaryOutline(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = stringResource(R.string.done)
        )
    }
}

@Preview
@Composable
private fun BtnPrimaryOutlineTrailingIcon() {
    AiAgenticAppTheme {
        BtnPrimaryOutline(
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
