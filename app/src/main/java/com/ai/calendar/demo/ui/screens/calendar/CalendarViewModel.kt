package com.ai.calendar.demo.ui.screens.calendar

import androidx.lifecycle.SavedStateHandle
import com.ai.calendar.demo.di.calendar.CALENDAR_SELECTED_DAY_FORMATTER
import com.ai.calendar.demo.domain.features.calendar.model.DateRange
import com.ai.calendar.demo.domain.features.calendar.usecase.GetEventByIdUseCase
import com.ai.calendar.demo.domain.features.calendar.usecase.GetEventsListUseCase
import com.ai.calendar.demo.domain.features.calendar.usecase.SetDateRangeUseCase
import com.ai.calendar.demo.ui.base.BaseViewModel
import com.ai.calendar.demo.ui.base.SubViewModelEntry
import com.ai.calendar.demo.ui.screens.calendar.addedit.AddEditEventEffect
import com.ai.calendar.demo.ui.screens.calendar.addedit.AddEditEventSvm
import com.ai.calendar.demo.ui.screens.calendar.mapper.CalendarEventUiMapper
import com.ai.calendar.demo.ui.screens.calendar.mapper.MonthGridBuilder
import com.ai.calendar.demo.ui.screens.chat.AiChatIntent
import com.ai.calendar.demo.ui.screens.chat.AiChatSvm
import com.ai.calendar.demo.utils.date.DateFormatter
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
    private val setDateRangeUseCase: SetDateRangeUseCase,
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val eventUiMapper: CalendarEventUiMapper,
    private val monthGridBuilder: MonthGridBuilder,
    @param:Named(CALENDAR_SELECTED_DAY_FORMATTER) private val selectedDayFormatter: DateFormatter,
    val addEditEventSvm: AddEditEventSvm,
    val aiChatSvm: AiChatSvm,
) : BaseViewModel<CalendarScreenState, CalendarIntent, CalendarEffect>(
    initialState = CalendarScreenState(),
    savedStateHandle = savedStateHandle,
    subViewModels = listOf(SubViewModelEntry(addEditEventSvm), SubViewModelEntry(aiChatSvm)),
) {
    init {
        updateSelectedDate(LocalDate.now(), YearMonth.now())
        observeEvents()
        observeAddEditBottomSheetEffects()
    }

    override fun reduceIntent(intent: CalendarIntent) {
        when (intent) {
            is CalendarIntent.PermissionGranted -> {
                updateUiState { it.copy(hasPermission = true) }
                setDateRange(uiState.currentMonth)
            }

            is CalendarIntent.PermissionDenied -> {
                updateUiState { it.copy(hasPermission = false) }
            }

            is CalendarIntent.PreviousMonthClicked -> changeMonth(uiState.currentMonth.minusMonths(1))

            is CalendarIntent.NextMonthClicked -> changeMonth(uiState.currentMonth.plusMonths(1))

            is CalendarIntent.DaySelected -> updateSelectedDate(intent.date, uiState.currentMonth)

            is CalendarIntent.TodayClicked -> {
                val today = LocalDate.now()
                changeMonth(YearMonth.from(today))
                updateSelectedDate(today, YearMonth.from(today))
            }

            is CalendarIntent.AddFabClicked -> {
                addEditEventSvm.showCreationBottomSheet(uiState.selectedDate)
                updateUiState { it.copy(isAddEditBottomSheetVisible = true) }
            }

            is CalendarIntent.EventClicked -> openEventEditor(intent.event.id)

            is CalendarIntent.AddEditBottomSheetDismissed -> {
                updateUiState { it.copy(isAddEditBottomSheetVisible = false) }
            }

            is CalendarIntent.AiChatFabClicked -> {
                aiChatSvm.onUserIntent(AiChatIntent.Reset)
                updateUiState { it.copy(isAiChatBottomSheetVisible = true) }
            }

            is CalendarIntent.AiChatBottomSheetDismissed -> {
                updateUiState { it.copy(isAiChatBottomSheetVisible = false) }
            }
        }
    }

    private fun openEventEditor(eventId: Long) {
        launchViewModelScope {
            getEventByIdUseCase(eventId).onSuccess { event ->
                event ?: return@onSuccess
                addEditEventSvm.showEditBottomSheet(event)
                updateUiState { it.copy(isAddEditBottomSheetVisible = true) }
            }
        }
    }

    private fun observeEvents() {
        launchViewModelScope {
            getEventsListUseCase().collect { events ->
                updateUiState { it.copy(events = eventUiMapper.map(events), isLoading = false) }
                rebuildGrid()
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
                    }
                }
            }
        }
    }

    private fun changeMonth(month: YearMonth) {
        val clampedDay = uiState.selectedDate.dayOfMonth.coerceAtMost(month.lengthOfMonth())
        updateSelectedDate(month.atDay(clampedDay), month)
        setDateRange(month)
    }

    private fun setDateRange(month: YearMonth) {
        val zone = ZoneId.systemDefault()
        val start = month.atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val end = month.atEndOfMonth().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
        setDateRangeUseCase(DateRange(startMillis = start, endMillis = end))
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

    private fun rebuildGrid() {
        val grid = monthGridBuilder.buildMonthGrid(
            month = uiState.currentMonth,
            selectedDate = uiState.selectedDate,
            events = uiState.events,
        )
        updateUiState { it.copy(monthGrid = grid) }
    }
}
