package com.ai.calendar.demo.ui.screens.calendar.utils

import java.time.LocalDate

fun interface DateFormatter {
    fun format(date: LocalDate): String
}
