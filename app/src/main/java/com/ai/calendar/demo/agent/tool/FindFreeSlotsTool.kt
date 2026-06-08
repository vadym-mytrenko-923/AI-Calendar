package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.agent.base.ParamType
import com.ai.calendar.demo.agent.base.ToolParam
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val DEFAULT_DURATION = 60

class FindFreeSlotsTool @Inject constructor() : LlmAgentTool {
    override val name: String = "find_free_slots"

    override val description: String = "Find free time slots on a date"

    override val parameters: List<ToolParam> = listOf(
        ToolParam("date", ParamType.STRING, "The date in YYYY-MM-DD format"),
        ToolParam("duration_minutes", ParamType.INTEGER, "Required duration in minutes"),
    )

    override suspend fun execute(args: Map<String, Any?>): String {
        val dateStr = args["date"]?.toString()?.trim('"') ?: LocalDate.now().toString()
        val duration =
            (args["duration_minutes"] as? Number)?.toInt() ?: args["duration_minutes"]?.toString()?.toIntOrNull() ?: DEFAULT_DURATION
        val now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val isToday = dateStr == LocalDate.now().toString()

        val timeHint = if (isToday) " after $now" else ""
        return "Call list_events, filter date=$dateStr, find ${duration}min gaps between 09:00-18:00$timeHint. Suggest 2-3 slots."
    }
}
