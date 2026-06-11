package com.ai.calendar.demo.agent.tool

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.agent.base.ParamType
import com.ai.calendar.demo.agent.base.ToolParam
import com.ai.calendar.demo.domain.base.logger.Logger
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.usecase.CreateEventUseCase
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

private const val TAG = "CreateEventTool"
private const val DEFAULT_DURATION = 60
private const val MILLIS_PER_MINUTE = 60_000L

class CreateEventTool @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
    private val logger: Logger,
) : LlmAgentTool {
    override val name: String = "create_event"

    override val description: String =
        "Create a calendar event. Only call when user provides title, date, time, and duration."

    override val parameters: List<ToolParam> = listOf(
        ToolParam("title", ParamType.STRING, "Event title as stated by the user"),
        ToolParam(
            "date",
            ParamType.STRING,
            "Date in YYYY-MM-DD. today=${LocalDate.now()}, tomorrow=${LocalDate.now().plusDays(1)}",
        ),
        ToolParam(
            "start_time",
            ParamType.STRING,
            "Start time in HH:mm 24h format. noon=12:00, 1pm=13:00, 2pm=14:00, 5pm=17:00, 9pm=21:00",
        ),
        ToolParam(
            "duration_minutes",
            ParamType.INTEGER,
            "Duration in minutes as stated by the user",
        ),
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
            onSuccess = {
                logger.log("$TAG: created '$title' on $dateStr at $startTimeStr")
                "Event '$title' on $dateStr at $startTimeStr created successfully."
            },
            onFailure = { error ->
                logger.logException(error)
                "Error creating event: ${error.message ?: error::class.simpleName}"
            },
        )
    }
}
