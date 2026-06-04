package com.ai.calendar.demo.ui.screens.calendar.utils

import com.ai.calendar.demo.utils.time.TimeFormatter
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val TIME_PATTERN = "HH:mm"

class EventTimeFormatter : TimeFormatter {
    private val formatter = DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.getDefault())

    override fun format(time: LocalTime): String = time.format(formatter)
}
