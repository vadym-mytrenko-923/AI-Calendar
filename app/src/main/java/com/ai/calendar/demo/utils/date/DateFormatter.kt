package com.ai.calendar.demo.utils.date

import java.time.LocalDate

fun interface DateFormatter {
    fun format(date: LocalDate): String
}
