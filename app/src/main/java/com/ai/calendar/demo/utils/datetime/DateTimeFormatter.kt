package com.ai.calendar.demo.utils.datetime

import java.time.ZonedDateTime

fun interface DateTimeFormatter {
    fun format(dateTime: ZonedDateTime): String
}
