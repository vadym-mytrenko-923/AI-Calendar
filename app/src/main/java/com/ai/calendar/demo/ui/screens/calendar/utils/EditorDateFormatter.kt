package com.ai.calendar.demo.ui.screens.calendar.utils

import com.ai.calendar.demo.utils.date.DateFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val EDITOR_DATE_PATTERN = "EEE, d MMM yyyy"

class EditorDateFormatter : DateFormatter {
    private val formatter = DateTimeFormatter.ofPattern(EDITOR_DATE_PATTERN, Locale.getDefault())

    override fun format(date: LocalDate): String = date.format(formatter)
}
