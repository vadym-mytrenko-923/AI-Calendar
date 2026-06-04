package com.ai.calendar.demo.domain.features.calendar

import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.model.DateRange
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    val eventsFlow: Flow<List<CalendarEvent>>
    suspend fun getEventsList(): List<CalendarEvent>
    fun setDateRange(range: DateRange)
    suspend fun getEventById(id: Long): CalendarEvent?
    suspend fun createEvent(event: CalendarEvent): Long
    suspend fun updateEvent(event: CalendarEvent)
    suspend fun deleteEvent(eventId: Long)
}
