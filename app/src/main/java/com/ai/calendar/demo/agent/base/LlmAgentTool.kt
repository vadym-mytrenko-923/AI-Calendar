package com.ai.calendar.demo.agent.base

import com.google.firebase.ai.type.Schema

interface LlmAgentTool {
    val name: String
    val description: String
    val parameters: Map<String, Schema> get() = emptyMap()
    suspend fun execute(args: Map<String, Any?>): String
}
