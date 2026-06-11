package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.agent.tool.mapper.toHumanReadableLlmMap
import com.ai.calendar.demo.domain.base.logger.Logger
import com.ai.calendar.demo.domain.features.calendar.usecase.GetUpcomingEventUseCase
import com.google.gson.Gson
import javax.inject.Inject

private const val TAG = "FindNearestTool"

class FindNearestEventTool @Inject constructor(
    private val getUpcomingEventUseCase: GetUpcomingEventUseCase,
    private val gson: Gson,
    private val logger: Logger,
) : LlmAgentTool {
    override val name: String = "find_nearest_event"

    override val description: String = "Find nearest upcoming event as JSON"

    override suspend fun execute(args: Map<String, Any?>): String =
        getUpcomingEventUseCase().fold(
            onSuccess = { event ->
                if (event != null) {
                    logger.log("$TAG: found '${event.title}'")
                    gson.toJson(event.toHumanReadableLlmMap())
                } else {
                    logger.log("$TAG: no upcoming events")
                    "No upcoming events found."
                }
            },
            onFailure = { error ->
                logger.logException(error)
                error.message.orEmpty()
            },
        )
}
