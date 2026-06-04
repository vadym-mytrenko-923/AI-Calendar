package com.ai.calendar.demo.ui.screens.chat.composable.preview

import com.ai.calendar.demo.ui.screens.chat.AiChatState
import com.ai.calendar.demo.ui.screens.chat.model.ChatMessageUiModel

object AiChatPreviewData {
    val messages = listOf(
        ChatMessageUiModel("What's the weather like today?", isUser = true),
        ChatMessageUiModel(
            "I don't have access to real-time weather data, but I can help you with calendar events!",
            isUser = false,
        ),
        ChatMessageUiModel("Create a meeting for tomorrow at 3pm", isUser = true),
    )

    val emptyState = AiChatState()

    val withMessagesState = AiChatState(messages = messages)

    val loadingState = AiChatState(messages = messages, isLoading = true)
}
