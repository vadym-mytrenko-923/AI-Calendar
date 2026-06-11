package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToolRegistry @Inject constructor(
    private val tools: Set<@JvmSuppressWildcards LlmAgentTool>,
) {

    fun toHermesToolsBlock(): String {
        val toolsArray = JSONArray()
        tools.forEach { tool ->
            val properties = JSONObject()
            val required = JSONArray()
            tool.parameters.forEach { param ->
                properties.put(
                    param.name,
                    JSONObject().apply {
                        put("type", param.type.label)
                        put("description", param.description)
                    },
                )
                if (param.required) required.put(param.name)
            }
            toolsArray.put(
                JSONObject().apply {
                    put("type", "function")
                    put(
                        "function",
                        JSONObject().apply {
                            put("name", tool.name)
                            put("description", tool.description)
                            put(
                                "parameters",
                                JSONObject().apply {
                                    put("type", "object")
                                    put("properties", properties)
                                    put("required", required)
                                },
                            )
                        },
                    )
                },
            )
        }
        return toolsArray.toString(2)
    }

    fun matchToolByKeys(jsonKeys: Set<String>): String? =
        tools.firstOrNull { tool ->
            tool.identifyingKeys.isNotEmpty() && jsonKeys.containsAll(tool.identifyingKeys)
        }?.name

    suspend fun executeTool(name: String, args: Map<String, Any?>): String {
        val normalized = name.trim().lowercase().replace(" ", "_")
        val tool = tools.firstOrNull { it.name == normalized }
            ?: tools.firstOrNull { it.name.contains(normalized) || normalized.contains(it.name) }
            ?: return "Error: unknown tool '$name'"
        return tool.execute(args)
    }
}
