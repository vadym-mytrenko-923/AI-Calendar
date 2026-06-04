package com.ai.calendar.demo.domain.features.ai

interface LlmClient {
    suspend fun sendMessage(prompt: String): String
    fun resetChat()
}
