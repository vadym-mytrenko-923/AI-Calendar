package com.ai.calendar.demo.agent.base

data class ToolParam(
    val name: String,
    val type: ParamType,
    val description: String,
)

enum class ParamType(val label: String) {
    STRING("string"),
    INTEGER("integer"),
    BOOLEAN("boolean"),
}
