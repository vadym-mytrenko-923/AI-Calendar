package com.ai.calendar.demo.agent.base

interface LlmAgentTool {
    val name: String
    val description: String
    val parameters: List<ToolParam> get() = emptyList()
    suspend fun execute(args: Map<String, Any?>): String
}
