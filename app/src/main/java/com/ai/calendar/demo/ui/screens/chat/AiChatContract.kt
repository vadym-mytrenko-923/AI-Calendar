package com.ai.calendar.demo.ui.screens.chat

import android.os.Parcelable
import com.ai.calendar.demo.ui.screens.chat.model.ChatMessageUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AiChatState(
    val input: String = "",
    val messages: List<ChatMessageUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val downloadProgress: Int? = null,
) : Parcelable {
    val isSendButtonEnabled: Boolean get() = input.isNotBlank() && !isLoading
    val isDownloading: Boolean get() = downloadProgress != null
}

sealed interface AiChatIntent {
    data class InputChanged(val text: String) : AiChatIntent
    data object SendClicked : AiChatIntent
    data object Reset : AiChatIntent
}

sealed interface AiChatEffect
