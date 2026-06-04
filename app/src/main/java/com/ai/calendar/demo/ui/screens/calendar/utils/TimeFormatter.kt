package com.ai.calendar.demo.ui.screens.calendar.utils

import java.time.LocalTime

fun interface TimeFormatter {
    fun format(time: LocalTime): String
}
