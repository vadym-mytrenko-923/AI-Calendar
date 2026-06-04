package com.ai.calendar.demo.ui.screens.calendar.mapper

import com.ai.calendar.demo.R
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.ui.screens.calendar.model.CalendarEventUiModel
import com.ai.calendar.demo.ui.screens.calendar.utils.CalendarDateFormatter
import com.ai.calendar.demo.utils.StringResource
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

class CalendarEventUiMapper @Inject constructor(
    private val dateFormatter: CalendarDateFormatter,
) {
    fun map(events: List<CalendarEvent>): List<CalendarEventUiModel> = events.map { map(it) }

    fun map(event: CalendarEvent): CalendarEventUiModel {
        val zone = ZoneId.systemDefault()
        val startTime = Instant.ofEpochMilli(event.startMillis).atZone(zone).toLocalTime()
        val endTime = Instant.ofEpochMilli(event.endMillis).atZone(zone).toLocalTime()

        val timeRange = if (event.isAllDay) {
            StringResource(R.string.calendarAllDay)
        } else {
            StringResource(
                R.string.calendarTimeRangeFormat,
                dateFormatter.formatTime(startTime),
                dateFormatter.formatTime(endTime),
            )
        }

        return CalendarEventUiModel(
            id = event.id,
            title = event.title,
            timeRange = timeRange,
            location = event.location,
            isAllDay = event.isAllDay,
            startMillis = event.startMillis,
        )
    }
}
