package com.ai.calendar.demo.agent.client

import com.ai.calendar.demo.agent.tool.ToolRegistry
import org.json.JSONObject
import javax.inject.Inject

data class ToolCall(val name: String, val args: Map<String, Any?>)

interface ToolCallParser {
    fun parse(response: String): ToolCall?
    fun cleanResponse(response: String): String
}

class LlmResponseParser @Inject constructor(
    private val toolRegistry: ToolRegistry,
) : ToolCallParser {

    override fun parse(response: String): ToolCall? {
        val stripped = stripTokens(response)

        return PARSERS.firstNotNullOfOrNull { it(stripped) }
            ?: parseFlatJson(stripped)
    }

    override fun cleanResponse(response: String): String {
        var result = response
        CLEAN_PATTERNS.forEach { result = result.replace(it, "") }
        return result.trim()
    }

    private fun parseFlatJson(text: String): ToolCall? {
        val json = extractFirstJson(text) ?: return null
        return try {
            val obj = JSONObject(json)
            val keys = obj.keys().asSequence().toSet()
            val toolName = toolRegistry.matchToolByKeys(keys) ?: return null
            ToolCall(toolName, obj.toArgsMap())
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        private val CODE_BLOCK = Regex("```\\w*\\n?")
        private val TOOL_CALL_PREFIX = Regex("""TOOL_CALL:\s*(\{[\s\S]*\})""")
        private val HERMES_TOOL_CALL = Regex("""<tool_call>\s*([\s\S]*?)\s*</tool_call>""")
        private val FUNCTION_CALL_SYNTAX = Regex("""(\w+)\(\s*\)""")

        private val CLEAN_PATTERNS = listOf(
            Regex("<think>[\\s\\S]*?</think>"),
            Regex("</think>"),
            Regex("<tool_call>[\\s\\S]*?</tool_call>"),
            Regex("TOOL_CALL:\\s*\\{[\\s\\S]*\\}"),
            Regex("<\\|im_end\\|>"),
            CODE_BLOCK,
            Regex("^TEXT:\\s*", RegexOption.MULTILINE),
        )

        private val PARSERS: List<(String) -> ToolCall?> = listOf(
            ::parseToolCallPrefix,
            ::parseHermes,
            ::parseNamedJson,
            ::parseFunctionSyntax,
        )

        private fun stripTokens(response: String): String = response
            .replace(CODE_BLOCK, "")
            .replace("<|im_end|>", "")
            .trim()

        private fun parseToolCallPrefix(text: String): ToolCall? =
            TOOL_CALL_PREFIX.find(text)?.let { parseJson(it.groupValues[1].trim()) }

        private fun parseHermes(text: String): ToolCall? =
            HERMES_TOOL_CALL.find(text)?.let { parseJson(it.groupValues[1].trim()) }

        private fun parseNamedJson(text: String): ToolCall? {
            val json = extractFirstJson(text) ?: return null
            if (!json.contains("\"name\"")) return null
            return parseJson(json)
        }

        private fun parseFunctionSyntax(text: String): ToolCall? =
            FUNCTION_CALL_SYNTAX.find(text)?.let { ToolCall(it.groupValues[1], emptyMap()) }

        @Suppress("TooGenericExceptionCaught")
        private fun parseJson(json: String): ToolCall? = try {
            val obj = JSONObject(json)
            val name = obj.optString("name").takeIf { it.isNotBlank() } ?: return null
            val argsObj = obj.optJSONObject("arguments")
                ?: obj.optJSONObject("args")
                ?: JSONObject()
            ToolCall(name, argsObj.toArgsMap())
        } catch (_: Exception) {
            null
        }

        private fun extractFirstJson(text: String): String? {
            val start = text.indexOf('{')
            if (start == -1) return null
            var depth = 0
            for (i in start until text.length) {
                when (text[i]) {
                    '{' -> depth++
                    '}' -> if (--depth == 0) return text.substring(start, i + 1)
                }
            }
            return null
        }

        private fun JSONObject.toArgsMap(): Map<String, Any?> {
            val map = mutableMapOf<String, Any?>()
            keys().forEach { key -> map[key] = opt(key) }
            return map
        }
    }
}
