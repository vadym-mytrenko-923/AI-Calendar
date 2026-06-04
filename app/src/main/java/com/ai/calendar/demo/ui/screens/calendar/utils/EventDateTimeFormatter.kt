package com.ai.calendar.demo.ui.screens.calendar.utils

import com.ai.calendar.demo.utils.datetime.DateTimeFormatter
import java.time.ZonedDateTime
import java.util.Locale

private const val EVENT_DATE_TIME_PATTERN = "EEE, MMM d 'at' HH:mm"

class EventDateTimeFormatter : DateTimeFormatter {
    private val formatter =
        java.time.format.DateTimeFormatter.ofPattern(EVENT_DATE_TIME_PATTERN, Locale.getDefault())

    override fun format(dateTime: ZonedDateTime): String = dateTime.format(formatter)
}
