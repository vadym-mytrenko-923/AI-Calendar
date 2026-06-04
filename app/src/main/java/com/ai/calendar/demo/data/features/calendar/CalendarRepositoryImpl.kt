package com.ai.calendar.demo.data.features.calendar

import com.ai.calendar.demo.data.features.calendar.local.CalendarLocalDataSource
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.model.DateRange
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val localDataSource: CalendarLocalDataSource,
) : CalendarRepository {
    override suspend fun getEventsList(range: DateRange): List<CalendarEvent> =
        localDataSource.getEventsList(range)

    override suspend fun createEvent(event: CalendarEvent): Long =
        localDataSource.createEvent(event)

    override suspend fun updateEvent(event: CalendarEvent) =
        localDataSource.updateEvent(event)

    override suspend fun deleteEvent(eventId: Long) =
        localDataSource.deleteEvent(eventId)
}
