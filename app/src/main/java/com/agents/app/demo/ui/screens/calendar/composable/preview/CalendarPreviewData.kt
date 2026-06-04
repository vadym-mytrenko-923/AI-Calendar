package com.agents.app.demo.ui.screens.calendar.composable.preview

import com.agents.app.demo.ui.screens.calendar.CalendarScreenState
import com.agents.app.demo.ui.screens.calendar.model.CalendarEventUiModel
import com.agents.app.demo.utils.NonTranslatableStringResource
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

object CalendarPreviewData {
    private val today = LocalDate.now()
    private val zone = ZoneId.systemDefault()

    val events = listOf(
        CalendarEventUiModel(
            id = 1,
            title = "Team standup",
            timeRange = NonTranslatableStringResource("09:00 – 09:30"),
            location = "Zoom",
            isAllDay = false,
            startMillis = today.atTime(9, 0).atZone(zone).toInstant().toEpochMilli(),
        ),
        CalendarEventUiModel(
            id = 2,
            title = "Lunch with Sarah",
            timeRange = NonTranslatableStringResource("12:00 – 13:00"),
            location = "Downtown Cafe",
            isAllDay = false,
            startMillis = today.atTime(12, 0).atZone(zone).toInstant().toEpochMilli(),
        ),
        CalendarEventUiModel(
            id = 3,
            title = "Sprint review",
            timeRange = NonTranslatableStringResource("15:00 – 16:00"),
            location = "",
            isAllDay = false,
            startMillis = today.atTime(15, 0).atZone(zone).toInstant().toEpochMilli(),
        ),
    )

    val state = CalendarScreenState(
        currentMonth = YearMonth.now(),
        selectedDate = today,
        events = events,
        hasPermission = true,
    )

    val emptyState = CalendarScreenState(
        currentMonth = YearMonth.now(),
        selectedDate = today,
        hasPermission = true,
    )

    val noPermissionState = CalendarScreenState(
        currentMonth = YearMonth.now(),
        selectedDate = today,
        hasPermission = false,
    )
}
