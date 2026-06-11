package com.ai.calendar.demo.agent.client

import com.ai.calendar.demo.agent.model.GenerationConfig
import com.ai.calendar.demo.agent.model.LocalModelManager
import com.ai.calendar.demo.agent.tool.ToolRegistry
import com.ai.calendar.demo.domain.base.logger.Logger
import com.ai.calendar.demo.domain.features.ai.LlmClient
import com.llamatik.library.platform.LlamaBridge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "LlamatikLlm"
private const val MAX_ROUNDS = 5
private const val FALLBACK_RESPONSE = "I'm not sure what you need. " +
    "I can list your events, find the nearest one, or create a new event."
private const val ERROR_FEEDBACK = "The tool returned an error: %s. Please ask me for the missing info."

class LlamatikLlmClient @Inject constructor(
    private val modelManager: LocalModelManager,
    private val toolRegistry: ToolRegistry,
    private val generationConfig: GenerationConfig,
    private val logger: Logger,
) : LlmClient {

    private var isModelLoaded = false
    private val history = mutableListOf<Pair<String, String>>()

    override suspend fun sendMessage(prompt: String): String = withContext(Dispatchers.IO) {
        logger.log("$TAG: >>> '$prompt'")
        val startTime = System.currentTimeMillis()

        if (!ensureModelLoaded()) return@withContext "Model not available."

        history.add("user" to prompt)

        repeat(MAX_ROUNDS) { round ->
            logger.log("$TAG: [ROUND ${round + 1}]")
            val response = generate()
            val toolCall = extractToolCall(response, toolRegistry)

            if (toolCall == null) {
                val result = cleanLlmResponse(response).ifBlank { FALLBACK_RESPONSE }
                history.add("assistant" to result)
                logResult(startTime, result)
                return@withContext result
            }

            logger.log("$TAG: [TOOL] ${toolCall.name}(${toolCall.args})")
            val toolResult = toolRegistry.executeTool(toolCall.name, toolCall.args)

            if (!toolResult.startsWith("Error")) {
                val result = formatSuccessResponse(toolCall.name, toolResult)
                history.add("assistant" to result)
                logResult(startTime, result)
                return@withContext result
            }

            logger.log("$TAG: [ERROR] $toolResult")
            history.add("assistant" to toolResult)
            history.add("user" to ERROR_FEEDBACK.format(toolResult))
        }

        FALLBACK_RESPONSE
    }

    override fun resetChat() {
        history.clear()
        LlamaBridge.sessionReset()
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun ensureModelLoaded(): Boolean {
        if (isModelLoaded) return true

        modelManager.prepareModel()
        val path = modelManager.getModelPath() ?: return false

        return try {
            applyParams()
            isModelLoaded = LlamaBridge.initGenerateModel(path)
            if (isModelLoaded) applyParams()
            logger.log("$TAG: model loaded=$isModelLoaded")
            isModelLoaded
        } catch (e: Exception) {
            logger.logException(e)
            false
        }
    }

    private fun generate(): String {
        val messages = mutableListOf("system" to buildCalendarSystemPrompt(toolRegistry))
        messages.addAll(history)

        val prompt = LlamaBridge.applyChatTemplate(messages, addAssistantPrefix = true)
            ?: buildChatMlFallback(messages)

        val start = System.currentTimeMillis()
        val response = LlamaBridge.generate(prompt)
        logger.log("$TAG: [RESPONSE] (${System.currentTimeMillis() - start}ms) $response")
        return response
    }

    private fun buildChatMlFallback(messages: List<Pair<String, String>>): String = buildString {
        messages.forEach { (role, content) ->
            append("<|im_start|>$role\n$content<|im_end|>\n")
        }
        append("<|im_start|>assistant\n")
    }

    private fun applyParams() {
        with(generationConfig) {
            LlamaBridge.updateGenerateParams(
                contextLength = contextLength,
                numThreads = numThreads,
                useMmap = useMmap,
                flashAttention = flashAttention,
                temperature = temperature,
                maxTokens = maxTokens,
                topP = topP,
                topK = topK,
                repeatPenalty = repeatPenalty,
                batchSize = batchSize,
                gpuLayers = gpuLayers,
            )
        }
    }

    private fun logResult(startTime: Long, result: String) {
        logger.log("$TAG: <<< (${System.currentTimeMillis() - startTime}ms) $result")
    }
}
