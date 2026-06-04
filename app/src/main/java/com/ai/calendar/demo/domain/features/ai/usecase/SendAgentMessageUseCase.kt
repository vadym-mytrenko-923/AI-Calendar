package com.ai.calendar.demo.domain.features.ai.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseUseCase
import com.ai.calendar.demo.domain.features.ai.LlmClient
import dagger.Reusable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@Reusable
class SendAgentMessageUseCase @Inject constructor(
    private val llmClient: LlmClient,
) : BaseUseCase<String, Result<String>>() {

    override suspend fun execute(parameters: String): Result<String> = useResultWrapper {
        val fullPrompt = "${buildSystemPrompt()}\n\nUser: $parameters"
        llmClient.sendMessage(fullPrompt)
    }

    private fun buildSystemPrompt(): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault())
        return "You are a helpful calendar assistant. Today is ${today.format(formatter)}. " +
            "Use the available tools to answer questions about the user's calendar events."
    }
}
