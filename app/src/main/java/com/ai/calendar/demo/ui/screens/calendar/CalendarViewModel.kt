package com.ai.calendar.demo.ui.screens.calendar

import androidx.lifecycle.SavedStateHandle
import com.ai.calendar.demo.di.calendar.CALENDAR_SELECTED_DAY_FORMATTER
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.model.DateRange
import com.ai.calendar.demo.domain.features.calendar.usecase.GetEventsListUseCase
import com.ai.calendar.demo.ui.base.BaseViewModel
import com.ai.calendar.demo.ui.base.SubViewModelEntry
import com.ai.calendar.demo.ui.screens.calendar.addedit.AddEditEventEffect
import com.ai.calendar.demo.ui.screens.calendar.addedit.AddEditEventSvm
import com.ai.calendar.demo.ui.screens.calendar.mapper.CalendarEventUiMapper
import com.ai.calendar.demo.ui.screens.calendar.mapper.MonthGridBuilder
import com.ai.calendar.demo.ui.screens.calendar.utils.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEventsListUseCase: GetEventsListUseCase,
    private val eventUiMapper: CalendarEventUiMapper,
    private val monthGridBuilder: MonthGridBuilder,
    @param:Named(CALENDAR_SELECTED_DAY_FORMATTER) private val selectedDayFormatter: DateFormatter,
    val addEditEventSvm: AddEditEventSvm,
) : BaseViewModel<CalendarScreenState, CalendarIntent, CalendarEffect>(
    initialState = CalendarScreenState(),
    savedStateHandle = savedStateHandle,
    subViewModels = listOf(SubViewModelEntry(addEditEventSvm)),
) {
    private var cachedDomainEvents: List<CalendarEvent> = emptyList()

    init {
        updateSelectedDate(LocalDate.now(), YearMonth.now())
        observeAddEditBottomSheetEffects()
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

            is CalendarIntent.DaySelected -> updateSelectedDate(intent.date, uiState.currentMonth)

            is CalendarIntent.TodayClicked -> {
                val today = LocalDate.now()
                updateSelectedDate(today, YearMonth.from(today))
                loadEvents()
            }

            is CalendarIntent.AddFabClicked -> {
                addEditEventSvm.showCreationBottomSheet(uiState.selectedDate)
                updateUiState { it.copy(isAddEditBottomSheetVisible = true) }
            }

            is CalendarIntent.EventClicked -> {
                val domainEvent = cachedDomainEvents.firstOrNull { it.id == intent.event.id } ?: return
                addEditEventSvm.showEditBottomSheet(domainEvent)
                updateUiState { it.copy(isAddEditBottomSheetVisible = true) }
            }

            is CalendarIntent.AddEditBottomSheetDismissed -> {
                updateUiState { it.copy(isAddEditBottomSheetVisible = false) }
            }
        }
    }

    private fun observeAddEditBottomSheetEffects() {
        launchViewModelScope {
            addEditEventSvm.uiEffectFlow.collect { effect ->
                when (effect) {
                    is AddEditEventEffect.Saved,
                    is AddEditEventEffect.Deleted -> {
                        updateUiState { it.copy(isAddEditBottomSheetVisible = false) }
                        loadEvents()
                    }
                }
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
                selectedDayLabel = selectedDayFormatter.format(date),
            )
        }
        rebuildGrid()
    }

    private fun loadEvents() {
        launchViewModelScope {
            updateUiState { it.copy(isLoading = true) }
            val range = uiState.currentMonth.toDateRange()
            getEventsListUseCase(range).onSuccess { events ->
                cachedDomainEvents = events
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
