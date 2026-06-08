package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.agent.tool.mapper.toHumanReadableLlmMap
import com.ai.calendar.demo.domain.features.calendar.usecase.GetUpcomingEventUseCase
import com.google.gson.Gson
import javax.inject.Inject

class FindNearestEventTool @Inject constructor(
    private val getUpcomingEventUseCase: GetUpcomingEventUseCase,
    private val gson: Gson,
) : LlmAgentTool {
    override val name: String = "find_nearest_event"

    override val description: String =
        "Finds the nearest upcoming event from the current time. Returns event with human-readable date/time fields."

    override suspend fun execute(args: Map<String, Any?>): String = getUpcomingEventUseCase().fold(
        onSuccess = { event ->
            event?.let { gson.toJson(it.toHumanReadableLlmMap()) } ?: "No upcoming events found."
        },
        onFailure = { it.message.orEmpty() },
    )
}
