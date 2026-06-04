package com.ai.calendar.demo.ui.screens.calendar.utils

import com.ai.calendar.demo.utils.date.DateFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val FULL_DATE_PATTERN = "EEEE, MMMM d, yyyy"

class FullDateFormatter : DateFormatter {
    private val formatter = DateTimeFormatter.ofPattern(FULL_DATE_PATTERN, Locale.getDefault())

    override fun format(date: LocalDate): String = date.format(formatter)
}
