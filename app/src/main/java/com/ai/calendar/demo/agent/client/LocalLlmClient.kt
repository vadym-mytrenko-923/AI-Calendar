package com.ai.calendar.demo.agent.client

import com.ai.calendar.demo.agent.model.LocalModelManager
import com.ai.calendar.demo.agent.tool.ToolRegistry
import com.ai.calendar.demo.di.calendar.CALENDAR_FULL_DATE_FORMATTER
import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.ai.calendar.demo.utils.date.DateFormatter
import com.ai.calendar.demo.utils.time.TimeFormatter
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

private const val TAG = "LocalLlm"
private const val MAX_TOOL_ROUNDS = 3
private const val MAX_TOKENS = 4096
private const val TOOL_CALL_OPEN = "<tool_call>"
private const val TOOL_CALL_CLOSE = "</tool_call>"

class LocalLlmClient @Inject constructor(
    private val modelManager: LocalModelManager,
    private val toolRegistry: ToolRegistry,
    @Named(CALENDAR_FULL_DATE_FORMATTER) private val fullDateFormatter: DateFormatter,
    private val timeFormatter: TimeFormatter,
) : LlmClient {

    private var engine: Engine? = null
    private var conversation: Conversation? = null
    private var initError: String? = null

    override suspend fun sendMessage(prompt: String): String = withContext(Dispatchers.IO) {
        initError?.let { return@withContext it }

        Timber.tag(TAG).d(">>> sendMessage: '$prompt'")
        val startTime = System.currentTimeMillis()

        val conv = getOrCreateConversation()
            ?: return@withContext (initError ?: "Model not available.")

        val fullPrompt = buildFullPrompt(prompt)
        Timber.tag(TAG).d("Prompt built (%d chars), sending to model...", fullPrompt.length)

        val inferenceStart = System.currentTimeMillis()
        var response = conv.sendMessage(fullPrompt).contents.toString()
        val inferenceMs = System.currentTimeMillis() - inferenceStart
        Timber.tag(TAG).d("Model responded in %dms (%d chars)", inferenceMs, response.length)
        Timber.tag(TAG).d("Raw response: %s", response)

        repeat(MAX_TOOL_ROUNDS) { round ->
            val toolCall = extractToolCall(response)
            if (toolCall == null) {
                Timber.tag(TAG).d("No tool call found, returning text response")
                val totalMs = System.currentTimeMillis() - startTime
                Timber.tag(TAG).d("<<< Total time: %dms", totalMs)
                return@withContext cleanResponse(response)
            }

            Timber.tag(TAG).d("Tool call [round %d]: %s(%s)", round + 1, toolCall.name, toolCall.args)
            val toolStart = System.currentTimeMillis()
            val toolResult = toolRegistry.executeTool(toolCall.name, toolCall.args)
            val toolMs = System.currentTimeMillis() - toolStart
            Timber.tag(TAG).d("Tool '%s' executed in %dms, result:\n%s", toolCall.name, toolMs, toolResult)

            Timber.tag(TAG).d("Sending tool result back to model...")
            val followUpStart = System.currentTimeMillis()
            response = conv.sendMessage("Tool result for ${toolCall.name}: $toolResult").contents.toString()
            val followUpMs = System.currentTimeMillis() - followUpStart
            Timber.tag(TAG).d("Follow-up response in %dms: %s", followUpMs, response)
        }

        val totalMs = System.currentTimeMillis() - startTime
        Timber.tag(TAG).d("<<< Total time: %dms (max rounds reached)", totalMs)
        cleanResponse(response)
    }

    override fun resetChat() {
        Timber.tag(TAG).d("Resetting chat session")
        conversation?.close()
        conversation = null
        engine?.close()
        engine = null
    }

    private suspend fun getOrCreateConversation(): Conversation? {
        conversation?.let { return it }

        Timber.tag(TAG).d("Preparing model...")
        val prepareStart = System.currentTimeMillis()
        modelManager.prepareModel()
        val prepareMs = System.currentTimeMillis() - prepareStart
        Timber.tag(TAG).d("Model prepared in %dms", prepareMs)

        val path = modelManager.getModelPath()
        if (path == null) {
            Timber.tag(TAG).e("Model path is null")
            return null
        }
        Timber.tag(TAG).d("Model path: %s", path)

        val eng = engine ?: run {
            Timber.tag(TAG).d("Creating engine (maxTokens=%d)...", MAX_TOKENS)
            val engineStart = System.currentTimeMillis()
            val config = EngineConfig(modelPath = path, maxNumTokens = MAX_TOKENS)
            Engine(config).also {
                Timber.tag(TAG).d("Initializing engine...")
                it.initialize()
                val engineMs = System.currentTimeMillis() - engineStart
                Timber.tag(TAG).d("Engine initialized in %dms", engineMs)
                engine = it
            }
        }

        Timber.tag(TAG).d("Creating conversation...")
        return eng.createConversation().also {
            Timber.tag(TAG).d("Conversation created")
            conversation = it
        }
    }

    private fun buildFullPrompt(userMessage: String): String {
        val today = fullDateFormatter.format(LocalDate.now())
        val time = timeFormatter.format(LocalTime.now())
        val tools = toolRegistry.toPromptDescription()

        return "<start_of_turn>user\n" +
            "You are a calendar assistant. Today: $today, time: $time.\n" +
            "$tools\n" +
            "To use a tool, reply with ONLY this JSON: {\"name\":\"tool_name\",\"args\":{}}\n" +
            "NEVER invent data. Always use tools.\n" +
            "$userMessage<end_of_turn>\n" +
            "<start_of_turn>model\n"
    }

    @Suppress("TooGenericExceptionCaught")
    private fun extractToolCall(response: String): ToolCall? {
        val json = extractTaggedJson(response) ?: extractBareJson(response) ?: return null
        return try {
            val obj = JSONObject(json)
            val name = obj.optString("name").takeIf { it.isNotBlank() } ?: return null
            val argsObj = obj.optJSONObject("args") ?: JSONObject()
            val args = mutableMapOf<String, Any?>()
            argsObj.keys().forEach { key -> args[key] = argsObj.opt(key) }
            ToolCall(name, args)
        } catch (_: Exception) {
            null
        }
    }

    private fun cleanResponse(response: String): String = response
        .replace(Regex("<think>[\\s\\S]*?</think>"), "")
        .replace(Regex("$TOOL_CALL_OPEN.*?$TOOL_CALL_CLOSE"), "")
        .trim()

    private fun extractTaggedJson(response: String): String? {
        val start = response.indexOf(TOOL_CALL_OPEN)
        if (start == -1) return null
        val end = response.indexOf(TOOL_CALL_CLOSE)
        return if (end > start) {
            response.substring(start + TOOL_CALL_OPEN.length, end).trim()
        } else {
            response.substring(start + TOOL_CALL_OPEN.length).trim()
        }
    }

    private fun extractBareJson(response: String): String? {
        val cleaned = response.replace(Regex("<think>[\\s\\S]*?</think>"), "").trim()
        val braceStart = cleaned.indexOf('{')
        if (braceStart == -1) return null
        val braceEnd = cleaned.lastIndexOf('}')
        if (braceEnd <= braceStart) return null
        val json = cleaned.substring(braceStart, braceEnd + 1)
        return if (json.contains("\"name\"")) json else null
    }

    private data class ToolCall(val name: String, val args: Map<String, Any?>)
}
