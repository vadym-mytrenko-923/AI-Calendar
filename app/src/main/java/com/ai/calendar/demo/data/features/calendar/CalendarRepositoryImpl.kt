package com.ai.calendar.demo.data.features.calendar

import com.ai.calendar.demo.data.features.calendar.local.CalendarLocalDataSource
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.model.DateRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val localDataSource: CalendarLocalDataSource,
) : CalendarRepository {
    private val currentDateRangeFlow = MutableStateFlow<DateRange?>(null)
    private val eventsRefreshTriggerFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private var cachedEvents: List<CalendarEvent> = emptyList()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val eventsFlow: Flow<List<CalendarEvent>> = combine(
        currentDateRangeFlow,
        eventsRefreshTriggerFlow.onStart { emit(Unit) },
    ) { range, _ -> range }.flatMapLatest { range ->
        flow {
            if (range == null) {
                emit(emptyList())
            } else {
                val events = localDataSource.getEventsList(range)
                cachedEvents = events
                emit(events)
            }
        }
    }

    override fun setDateRange(range: DateRange) {
        currentDateRangeFlow.value = range
    }

    override suspend fun getEventById(id: Long): CalendarEvent? {
        return cachedEvents.firstOrNull { it.id == id }
    }

    override suspend fun createEvent(event: CalendarEvent): Long {
        val id = localDataSource.createEvent(event)
        eventsRefreshTriggerFlow.emit(Unit)
        return id
    }

    override suspend fun updateEvent(event: CalendarEvent) {
        localDataSource.updateEvent(event)
        eventsRefreshTriggerFlow.emit(Unit)
    }

    override suspend fun deleteEvent(eventId: Long) {
        localDataSource.deleteEvent(eventId)
        eventsRefreshTriggerFlow.emit(Unit)
    }
}
