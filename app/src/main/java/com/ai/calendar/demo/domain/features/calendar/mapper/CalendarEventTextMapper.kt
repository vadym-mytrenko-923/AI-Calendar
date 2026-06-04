package com.ai.calendar.demo.domain.features.calendar.mapper

import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.utils.datetime.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

class CalendarEventTextMapper @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter,
) {
    fun format(event: CalendarEvent): String {
        val zone = ZoneId.systemDefault()
        val start = Instant.ofEpochMilli(event.startMillis).atZone(zone)
        val end = Instant.ofEpochMilli(event.endMillis).atZone(zone)
        val parts =
            mutableListOf("${event.title}: ${dateTimeFormatter.format(start)} – ${dateTimeFormatter.format(end)}")
        if (event.location.isNotBlank()) parts.add("Location: ${event.location}")
        if (event.attendees.isNotEmpty()) parts.add("Attendees: ${event.attendees.joinToString()}")
        return parts.joinToString("\n")
    }

    fun format(events: List<CalendarEvent>): String {
        if (events.isEmpty()) return "No events found."
        return events.joinToString(separator = "\n") { "- ${format(it)}" }
    }
}
