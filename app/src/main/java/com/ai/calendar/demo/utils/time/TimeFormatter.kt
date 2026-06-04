package com.ai.calendar.demo.utils.time

import java.time.LocalTime

fun interface TimeFormatter {
    fun format(time: LocalTime): String
}
