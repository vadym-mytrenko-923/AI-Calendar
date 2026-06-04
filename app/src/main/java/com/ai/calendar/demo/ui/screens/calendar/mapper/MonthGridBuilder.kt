package com.ai.calendar.demo.ui.screens.calendar.mapper

import com.ai.calendar.demo.ui.screens.calendar.model.CalendarEventUiModel
import com.ai.calendar.demo.ui.screens.calendar.model.DayCellUiModel
import com.ai.calendar.demo.ui.screens.calendar.model.MonthGridUiModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

private const val DAYS_IN_WEEK = 7

@Singleton
class MonthGridBuilder @Inject constructor() {
    fun buildMonthGrid(
        month: YearMonth,
        selectedDate: LocalDate,
        events: List<CalendarEventUiModel>,
    ): MonthGridUiModel {
        val today = LocalDate.now()
        val zone = ZoneId.systemDefault()
        val firstDay = month.atDay(1)
        val daysInMonth = month.lengthOfMonth()
        val startOffset = (firstDay.dayOfWeek.value - DayOfWeek.MONDAY.value + DAYS_IN_WEEK) % DAYS_IN_WEEK
        val totalWeeks = (startOffset + daysInMonth + DAYS_IN_WEEK - 1) / DAYS_IN_WEEK
        val eventsByDate = events.groupBy { event ->
            Instant.ofEpochMilli(event.startMillis).atZone(zone).toLocalDate()
        }

        val weeks = List(totalWeeks) { week ->
            List(DAYS_IN_WEEK) { dayOfWeek ->
                val dayIndex = week * DAYS_IN_WEEK + dayOfWeek - startOffset + 1
                if (dayIndex in 1..daysInMonth) {
                    val date = month.atDay(dayIndex)
                    val dayEvents = eventsByDate[date].orEmpty()
                    DayCellUiModel(
                        date = date,
                        day = dayIndex,
                        isSelected = date == selectedDate,
                        isToday = date == today,
                        hasEvents = dayEvents.isNotEmpty(),
                        events = dayEvents,
                    )
                } else {
                    null
                }
            }
        }

        return MonthGridUiModel(weeks = weeks)
    }
}
