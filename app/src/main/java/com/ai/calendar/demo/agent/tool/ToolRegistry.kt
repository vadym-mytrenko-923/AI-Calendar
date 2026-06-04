package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.Tool
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToolRegistry @Inject constructor(
    private val tools: Set<@JvmSuppressWildcards LlmAgentTool>,
) {
    fun toFirebaseTools(): List<Tool> = listOf(Tool.functionDeclarations(tools.map { it.toFunctionDeclaration() }))

    suspend fun executeTool(name: String, args: Map<String, Any?>): String {
        val tool = tools.firstOrNull { it.name == name } ?: return "Error: unknown tool '$name'"
        return tool.execute(args)
    }

    private fun LlmAgentTool.toFunctionDeclaration(): FunctionDeclaration = FunctionDeclaration(
        name = name,
        description = description,
        parameters = emptyMap(),
    )
}
