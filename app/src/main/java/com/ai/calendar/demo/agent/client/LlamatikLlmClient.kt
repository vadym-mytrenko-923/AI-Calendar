package com.ai.calendar.demo.agent.client

import com.ai.calendar.demo.agent.model.LocalModelManager
import com.ai.calendar.demo.agent.tool.ToolRegistry
import com.ai.calendar.demo.domain.base.logger.Logger
import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.llamatik.library.platform.LlamaBridge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "LlamatikLlm"
private const val MAX_ROUNDS = 5
private const val MAX_TOKENS = 128
private const val CONTEXT_LENGTH = 4096
private const val FALLBACK_RESPONSE = "I'm not sure what you need. " +
    "I can list your events, find the nearest one, or create a new event."

class LlamatikLlmClient @Inject constructor(
    private val modelManager: LocalModelManager,
    private val toolRegistry: ToolRegistry,
    private val logger: Logger,
) : LlmClient {

    private var isModelLoaded = false
    private val history = mutableListOf<Pair<String, String>>()

    @Suppress("NestedBlockDepth")
    override suspend fun sendMessage(prompt: String): String = withContext(Dispatchers.IO) {
        logger.log("$TAG: >>> sendMessage: '$prompt'")
        val startTime = System.currentTimeMillis()

        if (!ensureModelLoaded()) {
            return@withContext "Model not available."
        }

        history.add("user" to prompt)

        repeat(MAX_ROUNDS) { round ->
            logger.log("$TAG: [ROUND ${round + 1}] history=${history.size} turns")
            val response = generateFromHistory()

            val toolCall = extractToolCall(response)
            if (toolCall == null) {
                val cleaned = cleanResponse(response)
                val result = cleaned.ifBlank { FALLBACK_RESPONSE }
                history.add("assistant" to result)
                logger.log("$TAG: <<< TEXT (${System.currentTimeMillis() - startTime}ms): $result")
                return@withContext result
            }

            logger.log("$TAG: [TOOL] ${toolCall.name}(${toolCall.args})")
            val toolResult = toolRegistry.executeTool(toolCall.name, toolCall.args)

            if (!toolResult.startsWith("Error")) {
                val result = formatSuccessResponse(toolCall.name, toolResult)
                history.add("assistant" to result)
                logger.log("$TAG: <<< SUCCESS (${System.currentTimeMillis() - startTime}ms): $result")
                return@withContext result
            }

            logger.log("$TAG: [ERROR] $toolResult")
            history.add("assistant" to "TOOL_CALL failed: $toolResult")
            history.add("user" to "The tool returned an error: $toolResult. Please ask me for the missing info.")
        }

        FALLBACK_RESPONSE
    }

    override fun resetChat() {
        history.clear()
        LlamaBridge.sessionReset()
    }

    // region Model loading

    @Suppress("TooGenericExceptionCaught")
    private suspend fun ensureModelLoaded(): Boolean {
        if (isModelLoaded) return true

        modelManager.prepareModel()
        val path = modelManager.getModelPath() ?: return false

        return try {
            updateParams()
            isModelLoaded = LlamaBridge.initGenerateModel(path)
            if (isModelLoaded) updateParams()
            logger.log("$TAG: model loaded=$isModelLoaded, path=$path")
            isModelLoaded
        } catch (e: Exception) {
            logger.logException(e)
            false
        }
    }

    private fun generateFromHistory(): String {
        val messages = mutableListOf("system" to buildSystemPrompt())
        messages.addAll(history)

        val prompt = LlamaBridge.applyChatTemplate(messages, addAssistantPrefix = true)
            ?: buildString {
                messages.forEach { (role, content) ->
                    append("<|im_start|>$role\n$content<|im_end|>\n")
                }
                append("<|im_start|>assistant\n")
            }

        val start = System.currentTimeMillis()
        val response = LlamaBridge.generate(prompt)
        logger.log("$TAG: [RESPONSE] (${System.currentTimeMillis() - start}ms) $response")
        return response
    }

    private fun updateParams() {
        LlamaBridge.updateGenerateParams(
            contextLength = CONTEXT_LENGTH,
            numThreads = 4,
            useMmap = true,
            flashAttention = false,
            temperature = 0.1f,
            maxTokens = MAX_TOKENS,
            topP = 0.9f,
            topK = 40,
            repeatPenalty = 1.3f,
            batchSize = 512,
            gpuLayers = 0,
        )
    }

    // endregion

    // region System prompt

    private fun buildSystemPrompt(): String {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val tools = toolRegistry.toHermesToolsBlock()

        return "You are a helpful calendar assistant.\n" +
            "Today is $today. Tomorrow is $tomorrow.\n\n" +
            "You have access to the following tools:\n$tools\n\n" +
            "RESPONSE FORMAT (you MUST use one of these two prefixes):\n" +
            "- TOOL_CALL: {\"name\": \"tool_name\", \"arguments\": {\"param\": \"value\"}}\n" +
            "- TEXT: your plain text message to the user\n\n" +
            "STEP-BY-STEP PROCESS:\n" +
            "1. Read the user's message carefully.\n" +
            "2. Decide which tool to use (if any).\n" +
            "3. For the chosen tool, go through EACH required parameter one by one.\n" +
            "4. For each parameter, ask: did the user EXPLICITLY mention this value?\n" +
            "5. If ALL required parameters are found in the user's message, respond with TOOL_CALL.\n" +
            "6. If even ONE required parameter is missing, respond with TEXT: and ask the user to provide it.\n\n" +
            "STRICT RULES:\n" +
            "- NEVER guess, assume, or make up parameter values.\n" +
            "- If the user says \"create event\" but does not mention a time, you MUST ask for the time.\n" +
            "- If the user says \"meeting tomorrow\" but does not mention duration, you MUST ask for duration.\n" +
            "- Only use values that appear in the user's actual words.\n" +
            "- \"next week\" means 7 days from today. \"tomorrow\" means $tomorrow.\n" +
            "- Time conversion: noon=12:00, 1pm=13:00, 2pm=14:00, 3pm=15:00, 4pm=16:00, " +
            "5pm=17:00, 6pm=18:00, 7pm=19:00, 8pm=20:00, 9pm=21:00.\n" +
            "- Duration conversion: \"1 hour\"=60, \"30 minutes\"=30, \"2 hours\"=120, \"1.5 hours\"=90.\n" +
            "- Tools with no required parameters (like list_events) can be called immediately.\n" +
            "- Always respond with exactly one TOOL_CALL or one TEXT. Never both."
    }

    // endregion

    // region Response formatting

    private fun formatSuccessResponse(toolName: String, result: String): String = when {
        toolName == "create_event" ->
            result
                .replace("created successfully.", "")
                .replace("Event", "I created")
                .trim().trimEnd('.')
                .plus(".")
        toolName == "list_events" && result == "[]" -> "You have no events this month."
        toolName == "list_events" -> "Here are your events:\n$result"
        toolName == "find_nearest_event" && result.contains("No upcoming") -> result
        toolName == "find_nearest_event" -> "Your nearest event:\n$result"
        else -> result
    }

    // endregion

    // region Tool call extraction

    @Suppress("TooGenericExceptionCaught", "ReturnCount")
    private fun extractToolCall(response: String): ToolCall? {
        val stripped = response
            .replace(Regex("```\\w*\\n?"), "")
            .replace("<|im_end|>", "")
            .trim()

        // TOOL_CALL: prefix
        val toolCallPrefix = Regex("""TOOL_CALL:\s*(\{[\s\S]*\})""").find(stripped)
        if (toolCallPrefix != null) return parseToolCallJson(toolCallPrefix.groupValues[1].trim())

        // Hermes: <tool_call>JSON</tool_call>
        val hermesMatch = Regex("<tool_call>\\s*([\\s\\S]*?)\\s*</tool_call>").find(stripped)
        if (hermesMatch != null) return parseToolCallJson(hermesMatch.groupValues[1].trim())

        // Raw JSON with name field
        val braceStart = stripped.indexOf('{')
        if (braceStart != -1) {
            val braceEnd = findMatchingBrace(stripped, braceStart)
            if (braceEnd > braceStart) {
                val json = stripped.substring(braceStart, braceEnd + 1)
                if (json.contains("\"name\"")) return parseToolCallJson(json)
            }
        }

        // Flat JSON with create_event params
        if (braceStart != -1) {
            val flatResult = parseFlatToolCallJson(stripped)
            if (flatResult != null) return flatResult
        }

        // Function call syntax: tool_name()
        val funcMatch = Regex("""(\w+)\(\s*\)""").find(stripped)
        if (funcMatch != null) return ToolCall(funcMatch.groupValues[1], emptyMap())

        return null
    }

    @Suppress("TooGenericExceptionCaught")
    private fun parseFlatToolCallJson(text: String): ToolCall? = try {
        val braceStart = text.indexOf('{')
        if (braceStart == -1) return null
        val braceEnd = findMatchingBrace(text, braceStart)
        if (braceEnd <= braceStart) return null
        val obj = JSONObject(text.substring(braceStart, braceEnd + 1))
        val keys = mutableSetOf<String>()
        obj.keys().forEach { keys.add(it) }
        if (!keys.containsAll(listOf("title", "date"))) return null
        val args = mutableMapOf<String, Any?>()
        obj.keys().forEach { key -> args[key] = obj.opt(key) }
        ToolCall("create_event", args)
    } catch (_: Exception) {
        null
    }

    @Suppress("TooGenericExceptionCaught")
    private fun parseToolCallJson(json: String): ToolCall? = try {
        val obj = JSONObject(json)
        val name = obj.optString("name").takeIf { it.isNotBlank() } ?: return null
        val argsObj = obj.optJSONObject("arguments")
            ?: obj.optJSONObject("args")
            ?: JSONObject()
        val args = mutableMapOf<String, Any?>()
        argsObj.keys().forEach { key -> args[key] = argsObj.opt(key) }
        ToolCall(name, args)
    } catch (_: Exception) {
        null
    }

    // endregion

    private fun cleanResponse(response: String): String = response
        .replace(Regex("<think>[\\s\\S]*?</think>"), "")
        .replace("</think>", "")
        .replace(Regex("<tool_call>[\\s\\S]*?</tool_call>"), "")
        .replace(Regex("TOOL_CALL:\\s*\\{[\\s\\S]*\\}"), "")
        .replace("<|im_end|>", "")
        .replace(Regex("```\\w*\\n?"), "")
        .replace(Regex("^TEXT:\\s*", RegexOption.MULTILINE), "")
        .trim()

    private fun findMatchingBrace(text: String, openIndex: Int): Int {
        var depth = 0
        for (i in openIndex until text.length) {
            when (text[i]) {
                '{' -> depth++
                '}' -> {
                    depth--
                    if (depth == 0) return i
                }
            }
        }
        return -1
    }

    private data class ToolCall(val name: String, val args: Map<String, Any?>)
}
