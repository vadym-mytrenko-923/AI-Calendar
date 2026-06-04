package com.ai.calendar.demo.ui.screens.chat

import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.ai.calendar.demo.domain.features.ai.usecase.SendAgentMessageUseCase
import com.ai.calendar.demo.ui.base.BaseSubViewModel
import com.ai.calendar.demo.ui.screens.chat.model.ChatMessageUiModel
import javax.inject.Inject

class AiChatSvm @Inject constructor(
    private val sendAgentMessageUseCase: SendAgentMessageUseCase,
    private val llmClient: LlmClient,
) : BaseSubViewModel<AiChatState, AiChatIntent, AiChatEffect>(
    initialState = AiChatState(),
) {
    override fun reduceIntent(intent: AiChatIntent) {
        when (intent) {
            is AiChatIntent.InputChanged -> updateUiState { it.copy(input = intent.text) }
            is AiChatIntent.SendClicked -> sendMessage()
            is AiChatIntent.Reset -> reset()
        }
    }

    private fun sendMessage() {
        val question = uiState.input.trim()
        if (question.isBlank()) return

        val userMessage = ChatMessageUiModel(text = question, isUser = true)
        updateUiState {
            it.copy(
                input = "",
                messages = it.messages + userMessage,
                isLoading = true,
            )
        }

        launchSvmScope {
            sendAgentMessageUseCase(question)
                .onSuccess { response ->
                    val aiMessage = ChatMessageUiModel(text = response, isUser = false)
                    updateUiState {
                        it.copy(
                            messages = it.messages + aiMessage,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = ChatMessageUiModel(
                        text = error.message.orEmpty(),
                        isUser = false,
                    )

                    updateUiState {
                        it.copy(
                            messages = it.messages + errorMessage,
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun reset() {
        llmClient.resetChat()
        updateUiState { AiChatState() }
    }
}
