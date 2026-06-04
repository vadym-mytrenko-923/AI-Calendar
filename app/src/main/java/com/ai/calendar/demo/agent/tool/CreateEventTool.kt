package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.usecase.CreateEventUseCase
import com.google.firebase.ai.type.Schema
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

private const val DEFAULT_DURATION = 60
private const val MILLIS_PER_MINUTE = 60_000L

class CreateEventTool @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
) : LlmAgentTool {
    override val name: String = "create_event"

    override val description: String =
        "Creates a new calendar event. Requires title, date (YYYY-MM-DD), start_time (HH:mm), and duration_minutes."

    override val parameters: Map<String, Schema> = mapOf(
        "title" to Schema.string("The title of the event"),
        "date" to Schema.string("The date of the event in YYYY-MM-DD format"),
        "start_time" to Schema.string("The start time in HH:mm format (24-hour)"),
        "duration_minutes" to Schema.integer("Duration of the event in minutes"),
    )

    @Suppress("ReturnCount")
    override suspend fun execute(args: Map<String, Any?>): String {
        val title = args["title"]?.toString() ?: return "Error: title is required"
        val dateStr = args["date"]?.toString()?.trim('"') ?: return "Error: date is required"
        val startTimeStr = args["start_time"]?.toString()?.trim('"') ?: return "Error: start_time is required"
        val durationMinutes = try {
            (args["duration_minutes"] as? Number)?.toInt()
                ?: args["duration_minutes"]?.toString()?.toIntOrNull()
                ?: DEFAULT_DURATION
        } catch (_: Exception) {
            DEFAULT_DURATION
        }

        val date = runCatching { LocalDate.parse(dateStr) }.getOrElse {
            return "Error: invalid date '$dateStr', use YYYY-MM-DD format"
        }
        val startTime = runCatching { LocalTime.parse(startTimeStr) }.getOrElse {
            return "Error: invalid time '$startTimeStr', use HH:mm format"
        }

        val zone = ZoneId.systemDefault()
        val startMillis = date.atTime(startTime).atZone(zone).toInstant().toEpochMilli()
        val endMillis = startMillis + durationMinutes * MILLIS_PER_MINUTE

        val event = CalendarEvent(
            title = title,
            startMillis = startMillis,
            endMillis = endMillis,
        )

        return createEventUseCase(event).fold(
            onSuccess = { id -> "Event '$title' created successfully with id=$id." },
            onFailure = { "Error creating event: ${it.message ?: it::class.simpleName}" },
        )
    }
}
