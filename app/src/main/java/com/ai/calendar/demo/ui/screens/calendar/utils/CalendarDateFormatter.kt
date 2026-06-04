package com.ai.calendar.demo.ui.screens.calendar.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class CalendarDateFormatter @Inject constructor() {
    private val selectedDayFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault())
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    fun formatSelectedDay(date: LocalDate): String = date.format(selectedDayFormatter)

    fun formatTime(time: LocalTime): String = time.format(timeFormatter)
}
