package com.ai.calendar.demo.ui.screens.chat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatMessageUiModel(
    val text: String,
    val isUser: Boolean,
    val executionTime: String = "",
) : Parcelable
