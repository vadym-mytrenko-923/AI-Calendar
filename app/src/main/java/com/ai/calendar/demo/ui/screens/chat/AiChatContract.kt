package com.ai.calendar.demo.ui.screens.chat

import android.os.Parcelable
import com.ai.calendar.demo.ui.screens.chat.model.ChatMessageUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AiChatState(
    val input: String = "",
    val messages: List<ChatMessageUiModel> = emptyList(),
    val isLoading: Boolean = false,
) : Parcelable {
    val isSendButtonEnabled: Boolean get() = input.isNotBlank() && !isLoading
}

sealed interface AiChatIntent {
    data class InputChanged(val text: String) : AiChatIntent
    data object SendClicked : AiChatIntent
    data object Reset : AiChatIntent
}

sealed interface AiChatEffect
