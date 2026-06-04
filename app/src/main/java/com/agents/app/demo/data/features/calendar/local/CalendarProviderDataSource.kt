package com.agents.app.demo.data.features.calendar.local

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.provider.CalendarContract
import com.agents.app.demo.domain.features.calendar.model.CalendarEvent
import com.agents.app.demo.domain.features.calendar.model.DateRange
import javax.inject.Inject

class CalendarProviderDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
) : CalendarLocalDataSource {

    override suspend fun getEventsList(range: DateRange): List<CalendarEvent> {
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.CALENDAR_ID,
        )

        val selection = "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ?"
        val selectionArgs = arrayOf(range.startMillis.toString(), range.endMillis.toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        val events = mutableListOf<CalendarEvent>()

        contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder,
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                events.add(cursor.toCalendarEvent())
            }
        }

        return events
    }

    override suspend fun createEvent(event: CalendarEvent): Long {
        val values = event.toContentValues()
        val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        return uri?.lastPathSegment?.toLongOrNull() ?: -1
    }

    override suspend fun updateEvent(event: CalendarEvent) {
        val values = event.toContentValues()
        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.id)
        contentResolver.update(uri, values, null, null)
    }

    override suspend fun deleteEvent(eventId: Long) {
        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        contentResolver.delete(uri, null, null)
    }

    private fun Cursor.toCalendarEvent(): CalendarEvent = CalendarEvent(
        id = getLong(getColumnIndexOrThrow(CalendarContract.Events._ID)),
        title = getString(getColumnIndexOrThrow(CalendarContract.Events.TITLE)).orEmpty(),
        description = getString(getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)).orEmpty(),
        location = getString(getColumnIndexOrThrow(CalendarContract.Events.EVENT_LOCATION)).orEmpty(),
        startMillis = getLong(getColumnIndexOrThrow(CalendarContract.Events.DTSTART)),
        endMillis = getLong(getColumnIndexOrThrow(CalendarContract.Events.DTEND)),
        isAllDay = getInt(getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY)) == 1,
        calendarId = getLong(getColumnIndexOrThrow(CalendarContract.Events.CALENDAR_ID)),
    )

    private fun CalendarEvent.toContentValues(): ContentValues = ContentValues().apply {
        put(CalendarContract.Events.TITLE, title)
        put(CalendarContract.Events.DESCRIPTION, description)
        put(CalendarContract.Events.EVENT_LOCATION, location)
        put(CalendarContract.Events.DTSTART, startMillis)
        put(CalendarContract.Events.DTEND, endMillis)
        put(CalendarContract.Events.ALL_DAY, if (isAllDay) 1 else 0)
        put(CalendarContract.Events.CALENDAR_ID, calendarId)
        put(CalendarContract.Events.EVENT_TIMEZONE, java.util.TimeZone.getDefault().id)
    }
}
