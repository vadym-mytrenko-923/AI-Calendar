package com.ai.calendar.demo.agent.client

import com.ai.calendar.demo.agent.tool.ToolRegistry
import org.json.JSONObject

private val TOOL_CALL_PREFIX = Regex("""TOOL_CALL:\s*(\{[\s\S]*\})""")
private val HERMES_TOOL_CALL = Regex("""<tool_call>\s*([\s\S]*?)\s*</tool_call>""")
private val FUNCTION_CALL_SYNTAX = Regex("""(\w+)\(\s*\)""")
private val CODE_BLOCK = Regex("```\\w*\\n?")

private val CLEAN_PATTERNS = listOf(
    Regex("<think>[\\s\\S]*?</think>"),
    Regex("</think>"),
    Regex("<tool_call>[\\s\\S]*?</tool_call>"),
    Regex("TOOL_CALL:\\s*\\{[\\s\\S]*\\}"),
    Regex("<\\|im_end\\|>"),
    CODE_BLOCK,
    Regex("^TEXT:\\s*", RegexOption.MULTILINE),
)

data class ToolCall(val name: String, val args: Map<String, Any?>)

fun extractToolCall(response: String, toolRegistry: ToolRegistry): ToolCall? {
    val stripped = response
        .replace(CODE_BLOCK, "")
        .replace("<|im_end|>", "")
        .trim()

    return parseByPrefix(stripped)
        ?: parseHermes(stripped)
        ?: parseNamedJson(stripped)
        ?: parseFlatJson(stripped, toolRegistry)
        ?: parseFunctionSyntax(stripped)
}

fun cleanLlmResponse(response: String): String {
    var result = response
    CLEAN_PATTERNS.forEach { result = result.replace(it, "") }
    return result.trim()
}

private fun parseByPrefix(text: String): ToolCall? =
    TOOL_CALL_PREFIX.find(text)?.let { parseJson(it.groupValues[1].trim()) }

private fun parseHermes(text: String): ToolCall? =
    HERMES_TOOL_CALL.find(text)?.let { parseJson(it.groupValues[1].trim()) }

private fun parseNamedJson(text: String): ToolCall? {
    val json = extractFirstJson(text) ?: return null
    if (!json.contains("\"name\"")) return null
    return parseJson(json)
}

private fun parseFlatJson(text: String, toolRegistry: ToolRegistry): ToolCall? {
    val json = extractFirstJson(text) ?: return null
    return try {
        val obj = JSONObject(json)
        val keys = obj.keys().asSequence().toSet()
        val toolName = toolRegistry.matchToolByKeys(keys) ?: return null
        ToolCall(toolName, obj.toMap())
    } catch (_: Exception) {
        null
    }
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
    ToolCall(name, argsObj.toMap())
} catch (_: Exception) {
    null
}

private fun extractFirstJson(text: String): String? {
    val start = text.indexOf('{')
    if (start == -1) return null
    val end = findClosingBrace(text, start) ?: return null
    return text.substring(start, end + 1)
}

private fun findClosingBrace(text: String, openIndex: Int): Int? {
    var depth = 0
    for (i in openIndex until text.length) {
        when (text[i]) {
            '{' -> depth++
            '}' -> if (--depth == 0) return i
        }
    }
    return null
}

private fun JSONObject.toMap(): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    keys().forEach { key -> map[key] = opt(key) }
    return map
}
