package com.ai.calendar.demo.ui.screens.chat.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.ai.calendar.demo.ui.screens.chat.model.ChatMessageUiModel
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.chatBubbleMaxWidth
import com.ai.calendar.demo.ui.theme.containerShapeSmall
import com.ai.calendar.demo.ui.theme.marginPrimary
import com.ai.calendar.demo.ui.theme.marginPrimary1_5X

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    message: ChatMessageUiModel,
) {
    val backgroundColor = if (message.isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (message.isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val alignment = if (message.isUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = alignment,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = chatBubbleMaxWidth)
                .clip(containerShapeSmall)
                .background(backgroundColor)
                .padding(horizontal = marginPrimary1_5X, vertical = marginPrimary),
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatBubbleUserPreview() {
    AiCalendarTheme {
        ChatBubble(message = ChatMessageUiModel("Hello!", isUser = true))
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatBubbleAiPreview() {
    AiCalendarTheme {
        ChatBubble(message = ChatMessageUiModel("Hi! How can I help you?", isUser = false))
    }
}
