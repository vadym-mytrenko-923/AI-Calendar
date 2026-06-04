package com.ai.calendar.demo.agent.client

import com.ai.calendar.demo.agent.tool.ToolRegistry
import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.FunctionResponsePart
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

private const val MODEL_NAME = "gemini-2.5-flash"
private const val MAX_TOOL_ROUNDS = 3

class FirebaseLlmClient @Inject constructor(
    private val toolRegistry: ToolRegistry,
) : LlmClient {
    override suspend fun sendMessage(prompt: String): String {
        val llmModel = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = MODEL_NAME,
            tools = toolRegistry.toFirebaseTools()
        )

        val chat = llmModel.startChat()
        var response = chat.sendMessage(prompt)

        repeat(MAX_TOOL_ROUNDS) {
            val functionCalls = response.functionCalls
            if (functionCalls.isEmpty()) return response.text.orEmpty()

            val functionResponses = functionCalls.map { call ->
                val result = toolRegistry.executeTool(call.name, call.args)
                FunctionResponsePart(call.name, JsonObject(mapOf("result" to JsonPrimitive(result))))
            }

            response = chat.sendMessage(
                content("function") { functionResponses.forEach { part(it) } },
            )
        }

        return response.text.orEmpty()
    }
}
