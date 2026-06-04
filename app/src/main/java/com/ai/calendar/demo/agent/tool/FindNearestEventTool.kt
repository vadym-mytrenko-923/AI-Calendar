package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.domain.features.calendar.usecase.GetNearestEventUseCase
import javax.inject.Inject

class FindNearestEventTool @Inject constructor(
    private val getNearestEventUseCase: GetNearestEventUseCase,
) : LlmAgentTool {
    override val name: String = "find_nearest_event"

    override val description: String =
        "Finds the nearest upcoming event from the current time. Returns event details or a message if no upcoming events exist."

    override suspend fun execute(args: Map<String, Any?>): String = getNearestEventUseCase().getOrElse {
        it.message.orEmpty()
    }
}
