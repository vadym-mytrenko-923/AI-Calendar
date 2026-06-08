package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.google.firebase.ai.type.Schema
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val DEFAULT_DURATION = 60

class FindFreeSlotsTool @Inject constructor() : LlmAgentTool {
    override val name: String = "find_free_slots"

    override val description: String =
        "Returns a strategy for finding free time slots. " +
            "ALWAYS call this before scheduling, then call list_events and follow the strategy."

    override val parameters: Map<String, Schema> = mapOf(
        "date" to Schema.string("The date to search in YYYY-MM-DD format"),
        "duration_minutes" to Schema.integer("Required duration in minutes"),
    )

    override suspend fun execute(args: Map<String, Any?>): String {
        val dateStr = args["date"]?.toString()?.trim('"') ?: LocalDate.now().toString()
        val duration =
            (args["duration_minutes"] as? Number)?.toInt() ?: args["duration_minutes"]?.toString()?.toIntOrNull() ?: DEFAULT_DURATION
        val now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val isToday = dateStr == LocalDate.now().toString()

        return """
            STEP 1: Call list_events NOW to get real calendar data. Do NOT skip this step.

            STEP 2: From the results, filter events where date == "$dateStr".
            Events have "date", "startTime", "endTime" fields in HH:mm format.

            STEP 3: Find $duration-minute gaps:
            - Working hours: 09:00 to 18:00
            ${if (isToday) "- Current time is $now — only suggest slots AFTER this time" else ""}
            - Check gap from 09:00 to first event's startTime
            - Check gap between each event's endTime and next event's startTime
            - Check gap from last event's endTime to 18:00
            - A slot is free if the gap is >= $duration minutes and does NOT overlap any event

            STEP 4: Suggest 2-3 best slots formatted as "HH:mm – HH:mm"
            STEP 5: Wait for user to choose, then call create_event
        """.trimIndent()
    }
}
