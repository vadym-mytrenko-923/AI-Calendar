package com.agents.app.demo.ui.screens.calendar

import androidx.lifecycle.SavedStateHandle
import com.agents.app.demo.domain.features.calendar.model.DateRange
import com.agents.app.demo.domain.features.calendar.usecase.GetEventsListUseCase
import com.agents.app.demo.ui.base.BaseViewModel
import com.agents.app.demo.ui.screens.calendar.mapper.CalendarEventUiMapper
import com.agents.app.demo.ui.screens.calendar.mapper.MonthGridBuilder
import com.agents.app.demo.ui.screens.calendar.utils.CalendarDateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEventsListUseCase: GetEventsListUseCase,
    private val eventUiMapper: CalendarEventUiMapper,
    private val monthGridBuilder: MonthGridBuilder,
    private val dateFormatter: CalendarDateFormatter,
) : BaseViewModel<CalendarScreenState, CalendarIntent, CalendarEffect>(
    initialState = CalendarScreenState(),
    savedStateHandle = savedStateHandle,
) {
    init {
        updateSelectedDate(LocalDate.now(), YearMonth.now())
    }

    override fun reduceIntent(intent: CalendarIntent) {
        when (intent) {
            is CalendarIntent.PermissionGranted -> {
                updateUiState { it.copy(hasPermission = true) }
                loadEvents()
            }

            is CalendarIntent.PermissionDenied -> {
                updateUiState { it.copy(hasPermission = false) }
            }

            is CalendarIntent.PreviousMonthClicked -> changeMonth(uiState.currentMonth.minusMonths(1))
            is CalendarIntent.NextMonthClicked -> changeMonth(uiState.currentMonth.plusMonths(1))

            is CalendarIntent.DaySelected -> {
                updateSelectedDate(intent.date, uiState.currentMonth)
            }

            is CalendarIntent.TodayClicked -> {
                val today = LocalDate.now()
                updateSelectedDate(today, YearMonth.from(today))
                loadEvents()
            }
        }
    }

    private fun changeMonth(month: YearMonth) {
        val clampedDay = uiState.selectedDate.dayOfMonth.coerceAtMost(month.lengthOfMonth())
        updateSelectedDate(month.atDay(clampedDay), month)
        loadEvents()
    }

    private fun updateSelectedDate(date: LocalDate, month: YearMonth) {
        updateUiState {
            it.copy(
                currentMonth = month,
                selectedDate = date,
                selectedDayLabel = dateFormatter.formatSelectedDay(date),
            )
        }
        rebuildGrid()
    }

    private fun loadEvents() {
        launchViewModelScope {
            updateUiState { it.copy(isLoading = true) }
            val range = uiState.currentMonth.toDateRange()
            getEventsListUseCase(range).onSuccess { events ->
                updateUiState { it.copy(events = eventUiMapper.map(events), isLoading = false) }
                rebuildGrid()
            }.onFailure {
                updateUiState { it.copy(isLoading = false) }
            }
        }
    }

    private fun rebuildGrid() {
        val grid = monthGridBuilder.buildMonthGrid(
            month = uiState.currentMonth,
            selectedDate = uiState.selectedDate,
            events = uiState.events,
        )
        updateUiState { it.copy(monthGrid = grid) }
    }

    private fun YearMonth.toDateRange(): DateRange {
        val zone = ZoneId.systemDefault()
        val start = atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val end = atEndOfMonth().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
        return DateRange(startMillis = start, endMillis = end)
    }
}
