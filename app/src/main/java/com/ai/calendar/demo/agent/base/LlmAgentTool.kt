package com.ai.calendar.demo.agent.base

interface LlmAgentTool {
    val name: String
    val description: String
    val parameters: List<ToolParam> get() = emptyList()
    val identifyingKeys: Set<String> get() = parameters.filter { it.required }.map { it.name }.toSet()
    suspend fun execute(args: Map<String, Any?>): String
}
