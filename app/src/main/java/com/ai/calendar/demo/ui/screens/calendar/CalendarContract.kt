package com.ai.calendar.demo.ui.screens.calendar

import android.os.Parcelable
import com.ai.calendar.demo.ui.screens.calendar.model.CalendarEventUiModel
import com.ai.calendar.demo.ui.screens.calendar.model.MonthGridUiModel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.YearMonth

@Parcelize
data class CalendarScreenState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val events: List<CalendarEventUiModel> = emptyList(),
    val monthGrid: MonthGridUiModel = MonthGridUiModel(),
    val selectedDayLabel: String = "",
    val isLoading: Boolean = false,
    val hasPermission: Boolean = false,
    val isAddEditBottomSheetVisible: Boolean = false,
) : Parcelable {
    val isToday: Boolean
        get() = selectedDate == LocalDate.now() && currentMonth == YearMonth.now()
    val selectedDayEvents: List<CalendarEventUiModel>
        get() = monthGrid.weeks.flatten().filterNotNull().firstOrNull { it.isSelected }?.events.orEmpty()
}

sealed interface CalendarIntent {
    data object PermissionGranted : CalendarIntent
    data object PermissionDenied : CalendarIntent
    data object PreviousMonthClicked : CalendarIntent
    data object NextMonthClicked : CalendarIntent
    data class DaySelected(val date: LocalDate) : CalendarIntent
    data object TodayClicked : CalendarIntent
    data object AddFabClicked : CalendarIntent
    data class EventClicked(val event: CalendarEventUiModel) : CalendarIntent
    data object AddEditBottomSheetDismissed : CalendarIntent
}

sealed interface CalendarEffect
