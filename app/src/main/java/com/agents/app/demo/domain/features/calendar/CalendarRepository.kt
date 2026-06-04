package com.agents.app.demo.domain.features.calendar

import com.agents.app.demo.domain.features.calendar.model.CalendarEvent
import com.agents.app.demo.domain.features.calendar.model.DateRange

interface CalendarRepository {
    suspend fun getEventsList(range: DateRange): List<CalendarEvent>
    suspend fun createEvent(event: CalendarEvent): Long
    suspend fun updateEvent(event: CalendarEvent)
    suspend fun deleteEvent(eventId: Long)
}
