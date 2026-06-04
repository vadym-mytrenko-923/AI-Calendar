package com.ai.calendar.demo.ui.screens.calendar.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val SELECTED_DAY_PATTERN = "EEEE, d MMMM"

class SelectedDayFormatter : DateFormatter {
    private val formatter = DateTimeFormatter.ofPattern(SELECTED_DAY_PATTERN, Locale.getDefault())

    override fun format(date: LocalDate): String = date.format(formatter)
}
