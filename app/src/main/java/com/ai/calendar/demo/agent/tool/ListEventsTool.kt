package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.agent.tool.mapper.toHumanReadableMaps
import com.ai.calendar.demo.domain.base.logger.Logger
import com.ai.calendar.demo.domain.features.calendar.usecase.GetCurrentMonthEventsUseCase
import com.google.gson.Gson
import javax.inject.Inject

private const val TAG = "ListEventsTool"

class ListEventsTool @Inject constructor(
    private val getCurrentMonthEventsUseCase: GetCurrentMonthEventsUseCase,
    private val gson: Gson,
    private val logger: Logger,
) : LlmAgentTool {

    override val name: String = "list_events"

    override val description: String = "List current month events as JSON"

    override suspend fun execute(args: Map<String, Any?>): String =
        getCurrentMonthEventsUseCase().fold(
            onSuccess = { events ->
                logger.log("$TAG: found ${events.size} events")
                gson.toJson(events.toHumanReadableMaps())
            },
            onFailure = { error ->
                logger.logException(error)
                error.message.orEmpty()
            },
        )
}
