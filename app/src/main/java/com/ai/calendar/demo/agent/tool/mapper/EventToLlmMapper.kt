package com.ai.calendar.demo.agent.tool.mapper

import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")

fun List<CalendarEvent>.toHumanReadableMaps(): List<Map<String, Any?>> = map { it.toHumanReadableLlmMap() }

fun CalendarEvent.toHumanReadableLlmMap(): Map<String, Any?> {
    val zone = ZoneId.systemDefault()
    val start = Instant.ofEpochMilli(startMillis).atZone(zone)
    val end = Instant.ofEpochMilli(endMillis).atZone(zone)

    return buildMap {
        put("id", id)
        put("title", title)
        put("date", start.format(DATE_FORMAT))
        put("startTime", start.format(TIME_FORMAT))
        put("endTime", end.format(TIME_FORMAT))
        if (location.isNotBlank()) put("location", location)
        if (description.isNotBlank()) put("description", description)
        if (attendees.isNotEmpty()) put("attendees", attendees)
        if (isAllDay) put("isAllDay", true)
    }
}
