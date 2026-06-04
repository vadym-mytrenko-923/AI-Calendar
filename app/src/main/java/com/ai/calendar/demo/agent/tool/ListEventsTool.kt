package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.domain.features.calendar.usecase.GetCurrentMonthEventsSummaryUseCase
import javax.inject.Inject

class ListEventsTool @Inject constructor(
    private val getFormattedEventsListUseCase: GetCurrentMonthEventsSummaryUseCase,
) : LlmAgentTool {
    override val name: String = "list_events"

    override val description: String =
        "Lists all calendar events for the current month. Returns event details including title, date, time, location, and attendees."

    override suspend fun execute(args: Map<String, Any?>): String = getFormattedEventsListUseCase().getOrElse {
        it.message.orEmpty()
    }
}
