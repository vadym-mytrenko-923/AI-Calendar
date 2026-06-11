package com.ai.calendar.demo.ui.screens.chat

import com.ai.calendar.demo.agent.model.LocalModelManager
import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.ai.calendar.demo.domain.features.ai.usecase.SendAgentMessageUseCase
import com.ai.calendar.demo.ui.base.BaseSubViewModel
import com.ai.calendar.demo.ui.screens.chat.model.ChatMessageUiModel
import kotlinx.coroutines.Job
import javax.inject.Inject

private const val MILLIS_PER_SECOND = 1000.0

class AiChatSvm @Inject constructor(
    private val sendAgentMessageUseCase: SendAgentMessageUseCase,
    private val llmClient: LlmClient,
    private val modelManager: LocalModelManager,
) : BaseSubViewModel<AiChatState, AiChatIntent, AiChatEffect>(
    initialState = AiChatState(),
) {
    private var messageJob: Job? = null

    override fun onAttached() {
        observeModelState()
    }

    override fun reduceIntent(intent: AiChatIntent) {
        when (intent) {
            is AiChatIntent.InputChanged -> updateUiState { it.copy(input = intent.text) }
            is AiChatIntent.SendClicked -> sendMessage()
            is AiChatIntent.Reset -> reset()
        }
    }

    private fun observeModelState() {
        launchSvmScope {
            modelManager.modelState.collect { state ->
                when (state) {
                    is LocalModelManager.ModelState.Downloading -> {
                        updateUiState { it.copy(downloadProgress = state.progressPercent) }
                    }
                    is LocalModelManager.ModelState.Ready -> {
                        updateUiState { it.copy(downloadProgress = null) }
                    }
                    else -> Unit
                }
            }
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

        messageJob?.cancel()
        messageJob = launchSvmScope {
            val startTime = System.currentTimeMillis()
            sendAgentMessageUseCase(question)
                .onSuccess { response ->
                    val elapsed = formatElapsed(System.currentTimeMillis() - startTime)
                    val aiMessage = ChatMessageUiModel(
                        text = response,
                        isUser = false,
                        executionTime = elapsed,
                    )
                    updateUiState {
                        it.copy(
                            messages = it.messages + aiMessage,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    val elapsed = formatElapsed(System.currentTimeMillis() - startTime)
                    val errorMessage = ChatMessageUiModel(
                        text = error.message.orEmpty(),
                        isUser = false,
                        executionTime = elapsed,
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

    private fun formatElapsed(millis: Long): String {
        val seconds = millis / MILLIS_PER_SECOND
        return if (seconds < 1) "${millis}ms" else "%.1fs".format(seconds)
    }

    private fun reset() {
        messageJob?.cancel()
        messageJob = null
        llmClient.resetChat()
        updateUiState { AiChatState() }
    }
}
