package com.ai.calendar.demo.domain.features.ai.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseUseCase
import com.ai.calendar.demo.domain.features.ai.LlmClient
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SendAiMessageUseCase @Inject constructor(
    private val llmClient: LlmClient,
) : BaseUseCase<String, Result<String>>() {
    override suspend fun execute(parameters: String): Result<String> = useResultWrapper {
        llmClient.sendMessage(parameters)
    }
}
