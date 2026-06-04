package com.ai.calendar.demo.agent.client

import com.ai.calendar.demo.agent.tool.ToolRegistry
import com.ai.calendar.demo.di.calendar.CALENDAR_FULL_DATE_FORMATTER
import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.ai.calendar.demo.utils.date.DateFormatter
import com.ai.calendar.demo.utils.time.TimeFormatter
import com.google.firebase.Firebase
import com.google.firebase.ai.Chat
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.FunctionResponsePart
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

private const val MODEL_NAME = "gemini-2.5-flash"
private const val MAX_TOOL_ROUNDS = 10

class FirebaseLlmClient @Inject constructor(
    private val toolRegistry: ToolRegistry,
    @param:Named(CALENDAR_FULL_DATE_FORMATTER) private val fullDateFormatter: DateFormatter,
    private val timeFormatter: TimeFormatter,
) : LlmClient {
    private var chat: Chat? = null

    private fun createModel(): GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = MODEL_NAME,
            tools = toolRegistry.toFirebaseTools(),
            systemInstruction = content { text(buildSystemInstruction()) },
        )

    private fun buildSystemInstruction(): String {
        val today = fullDateFormatter.format(LocalDate.now())
        val time = timeFormatter.format(LocalTime.now())
        return "You are a helpful calendar assistant. Today is $today, current time is $time. " +
            "Use the available tools to answer questions about the user's calendar events. " +
            "You can also create new events when asked. " +
            "When naming events, use natural sentence-style names without quotes. " +
            "Never create events in the past — if the requested time has already passed today, " +
            "suggest the next available time or tomorrow instead."
    }

    override suspend fun sendMessage(prompt: String): String {
        val activeChat = chat ?: createModel().startChat().also { chat = it }
        var response = activeChat.sendMessage(prompt)

        repeat(MAX_TOOL_ROUNDS) {
            val functionCalls = response.functionCalls
            if (functionCalls.isEmpty()) return response.text.orEmpty()

            val functionResponses = functionCalls.map { call ->
                val result = toolRegistry.executeTool(call.name, call.args)
                FunctionResponsePart(call.name, JsonObject(mapOf("result" to JsonPrimitive(result))))
            }

            response = activeChat.sendMessage(
                content("function") { functionResponses.forEach { part(it) } },
            )
        }

        return response.text.orEmpty()
    }

    override fun resetChat() {
        chat = null
    }
}
