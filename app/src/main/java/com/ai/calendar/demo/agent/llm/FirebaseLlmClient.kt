package com.ai.calendar.demo.agent.llm

import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import javax.inject.Inject

private const val MODEL_NAME = "gemini-2.5-flash"

class FirebaseLlmClient @Inject constructor() : LlmClient {
    private val model by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(MODEL_NAME)
    }

    override suspend fun sendMessage(prompt: String): String =
        model.generateContent(prompt).text.orEmpty()
}
