package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToolRegistry @Inject constructor(
    private val tools: Set<@JvmSuppressWildcards LlmAgentTool>,
) {

    fun toPromptDescription(): String = buildString {
        appendLine("Available tools:")
        tools.forEach { tool ->
            appendLine("- ${tool.name}: ${tool.description}")
            if (tool.parameters.isNotEmpty()) {
                appendLine("  Parameters:")
                tool.parameters.forEach { param ->
                    appendLine("    ${param.name} (${param.type.label}): ${param.description}")
                }
            }
        }
    }

    suspend fun executeTool(name: String, args: Map<String, Any?>): String {
        val tool = tools.firstOrNull { it.name == name } ?: return "Error: unknown tool '$name'"
        return tool.execute(args)
    }
}
