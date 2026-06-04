package com.ai.calendar.demo.ui.screens.chat.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.ai.calendar.demo.ui.theme.marginPrimary
import com.ai.calendar.demo.ui.theme.marginPrimary2X

@Composable
fun ChatInputBar(
    modifier: Modifier = Modifier,
    input: String,
    isSendEnabled: Boolean,
    onInputChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = marginPrimary2X),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(marginPrimary),
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = input,
            onValueChange = onInputChanged,
            placeholder = { Text(stringResource(R.string.aiChatPromptPlaceholder)) },
            singleLine = true,
        )

        IconButton(
            onClick = onSendClicked,
            enabled = isSendEnabled,
        ) {
            Icon(
                painter = painterResource(AppIcons.Add),
                contentDescription = stringResource(R.string.aiChatBtnSend),
                tint = if (isSendEnabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatInputBarPreview() {
    AiCalendarTheme {
        ChatInputBar(
            input = "",
            isSendEnabled = false,
            onInputChanged = {},
            onSendClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatInputBarWithTextPreview() {
    AiCalendarTheme {
        ChatInputBar(
            input = "Hello",
            isSendEnabled = true,
            onInputChanged = {},
            onSendClicked = {},
        )
    }
}
