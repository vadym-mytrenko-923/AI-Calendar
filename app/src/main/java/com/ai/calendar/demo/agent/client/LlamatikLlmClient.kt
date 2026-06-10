package com.ai.calendar.demo.agent.client

import com.ai.calendar.demo.agent.model.LocalModelManager
import com.ai.calendar.demo.agent.tool.ToolRegistry
import com.ai.calendar.demo.di.calendar.CALENDAR_FULL_DATE_FORMATTER
import com.ai.calendar.demo.domain.base.logger.Logger
import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.ai.calendar.demo.utils.date.DateFormatter
import com.ai.calendar.demo.utils.time.TimeFormatter
import com.llamatik.library.platform.LlamaBridge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

private const val TAG = "LlamatikLlm"
private const val MAX_TOOL_ROUNDS = 3
private const val MAX_TOKENS = 256
private const val CONTEXT_LENGTH = 4096
private const val FALLBACK_RESPONSE = "I'm not sure what you need. " +
    "I can list your events, find the nearest one, or create a new event."

class LlamatikLlmClient @Inject constructor(
    private val modelManager: LocalModelManager,
    private val toolRegistry: ToolRegistry,
    private val logger: Logger,
    @Named(CALENDAR_FULL_DATE_FORMATTER) private val fullDateFormatter: DateFormatter,
    private val timeFormatter: TimeFormatter,
) : LlmClient {

    private var isModelLoaded = false

    override suspend fun sendMessage(prompt: String): String = withContext(Dispatchers.IO) {
        logger.log("$TAG: sendMessage prompt='$prompt'")
        val startTime = System.currentTimeMillis()

        if (!ensureModelLoaded()) {
            return@withContext "Model not available."
        }

        val fullPrompt = buildPrompt(buildSystemPrompt(), prompt)
        var response = generate(fullPrompt)

        var lastToolName: String? = null
        repeat(MAX_TOOL_ROUNDS) { round ->
            val toolCall = extractToolCall(response)
            if (toolCall == null) {
                val totalMs = System.currentTimeMillis() - startTime
                logger.log("$TAG: no tool call [round ${round + 1}], total=${totalMs}ms")
                val cleaned = cleanResponse(response)
                return@withContext cleaned.ifBlank { FALLBACK_RESPONSE }
            }

            if (toolCall.name == lastToolName) {
                logger.log("$TAG: same tool '${toolCall.name}' repeated, breaking")
                return@withContext cleanResponse(response)
            }
            lastToolName = toolCall.name

            logger.log("$TAG: tool call [round ${round + 1}] name=${toolCall.name} args=${toolCall.args}")
            val toolResult = toolRegistry.executeTool(toolCall.name, toolCall.args)
            logger.log("$TAG: tool result=$toolResult")

            val isError = toolResult.startsWith("Error")
            response = generate(buildFollowUpPrompt(toolCall.name, toolResult, isError))
        }

        val totalMs = System.currentTimeMillis() - startTime
        logger.log("$TAG: complete, total=${totalMs}ms")
        cleanResponse(response)
    }

    override fun resetChat() {
        LlamaBridge.sessionReset()
    }

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

    private fun generate(prompt: String): String = LlamaBridge.generate(prompt)

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
            repeatPenalty = 1.1f,
            batchSize = 512,
            gpuLayers = 0,
        )
    }

    private fun buildSystemPrompt(): String {
        val today = fullDateFormatter.format(LocalDate.now())
        val time = timeFormatter.format(LocalTime.now())
        val tools = toolRegistry.toHermesToolsBlock()

        return "You are a calendar assistant. Today is $today, current time is $time.\n\n" +
            "You have access to the following tools:\n$tools\n\n" +
            "RULES:\n" +
            "- Before calling any tool, check that the user explicitly provided ALL required parameters. " +
            "If ANY required parameter is missing or unclear, do NOT call the tool. " +
            "Instead reply in plain text asking for the missing information.\n" +
            "- Only tools with no required parameters can be called directly.\n\n" +
            "When you call a tool, ONLY reply in this format:\n\n" +
            "<tool_call>\n{\"name\": \"function_name\", \"arguments\": {\"param\": \"value\"}}\n</tool_call>"
    }

    private fun buildPrompt(system: String, userMessage: String): String =
        "<|im_start|>system\n$system<|im_end|>\n" +
            "<|im_start|>user\n$userMessage<|im_end|>\n" +
            "<|im_start|>assistant\n"

    private fun buildFollowUpPrompt(toolName: String, toolResult: String, isError: Boolean): String {
        val instruction = if (isError) {
            "The action failed. Ask the user for the missing information. Be brief."
        } else {
            "Tell the user what you did in first person (e.g. \"I created...\", \"I found...\", " +
                "\"You have no events...\"). Be brief and friendly."
        }
        return "<|im_start|>system\n" +
            "You are a calendar assistant. $instruction " +
            "Do NOT mention tools, functions, IDs, or technical details. " +
            "Do NOT call any tools. Do NOT output JSON.<|im_end|>\n" +
            "<|im_start|>user\nAction: $toolName\nResult: $toolResult<|im_end|>\n" +
            "<|im_start|>assistant\n"
    }

    @Suppress("TooGenericExceptionCaught", "ReturnCount")
    private fun extractToolCall(response: String): ToolCall? {
        val stripped = response.replace(Regex("```\\w*\\n?"), "").trim()

        val toolCallMatch = Regex("<tool_call>\\s*([\\s\\S]*?)\\s*</tool_call>").find(stripped)
        if (toolCallMatch != null) {
            return parseToolCallJson(toolCallMatch.groupValues[1].trim())
        }

        val braceStart = stripped.indexOf('{')
        if (braceStart != -1) {
            val braceEnd = findMatchingBrace(stripped, braceStart)
            if (braceEnd > braceStart) {
                val json = stripped.substring(braceStart, braceEnd + 1)
                if (json.contains("\"name\"")) {
                    return parseToolCallJson(json)
                }
            }
        }

        val funcMatch = Regex("""(\w+)\(\s*\)""").find(stripped)
        if (funcMatch != null) {
            return ToolCall(funcMatch.groupValues[1], emptyMap())
        }

        return null
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

    private fun cleanResponse(response: String): String = response
        .replace(Regex("<think>[\\s\\S]*?</think>"), "")
        .replace("</think>", "")
        .replace(Regex("<tool_call>[\\s\\S]*?</tool_call>"), "")
        .replace("<|im_end|>", "")
        .replace(Regex("```\\w*\\n?"), "")
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
