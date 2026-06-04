package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.domain.features.calendar.usecase.GetCurrentMonthEventsUseCase
import com.google.gson.Gson
import javax.inject.Inject

class ListEventsTool @Inject constructor(
    private val getCurrentMonthEventsUseCase: GetCurrentMonthEventsUseCase,
    private val gson: Gson,
) : LlmAgentTool {

    override val name: String = "list_events"

    override val description: String =
        "Lists all calendar events for the current month. Returns event details as JSON."

    override suspend fun execute(args: Map<String, Any?>): String = getCurrentMonthEventsUseCase().fold(
        onSuccess = { events -> gson.toJson(events) },
        onFailure = { it.message.orEmpty() },
    )
}
