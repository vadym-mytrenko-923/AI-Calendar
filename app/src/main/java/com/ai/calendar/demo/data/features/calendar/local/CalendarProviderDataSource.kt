package com.ai.calendar.demo.data.features.calendar.local

import android.accounts.Account
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.model.DateRange
import javax.inject.Inject

private const val GOOGLE_ACCOUNT_TYPE = "com.google"

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

        val selection = "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ?" +
            " AND ${CalendarContract.Events.DELETED} != 1"
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
                val event = cursor.toCalendarEvent()
                val attendees = getAttendeesForEvent(event.id)
                events.add(event.copy(attendees = attendees))
            }
        }

        return events
    }

    override suspend fun createEvent(event: CalendarEvent): Long {
        val calendarId = event.calendarId.takeIf { it > 0 } ?: getDefaultCalendarId()
        val values = event.copy(calendarId = calendarId).toContentValues()
        val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        val eventId = uri?.lastPathSegment?.toLongOrNull() ?: -1

        if (eventId > 0 && event.attendees.isNotEmpty()) {
            syncAttendees(eventId, event.attendees)
        }

        requestCalendarSync()
        return eventId
    }

    override suspend fun updateEvent(event: CalendarEvent) {
        val values = event.toContentValues()
        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.id)
        contentResolver.update(uri, values, null, null)
        syncAttendees(event.id, event.attendees)
        requestCalendarSync()
    }

    override suspend fun deleteEvent(eventId: Long) {
        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        contentResolver.delete(uri, null, null)
        requestCalendarSync()
    }

    private fun getAttendeesForEvent(eventId: Long): List<String> {
        val attendees = mutableListOf<String>()
        val projection = arrayOf(CalendarContract.Attendees.ATTENDEE_EMAIL)
        val selection = "${CalendarContract.Attendees.EVENT_ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())

        contentResolver.query(
            CalendarContract.Attendees.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null,
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val email = cursor.getString(0).orEmpty()
                if (email.isNotBlank()) {
                    attendees.add(email)
                }
            }
        }
        return attendees
    }

    private fun syncAttendees(eventId: Long, attendees: List<String>) {
        contentResolver.delete(
            CalendarContract.Attendees.CONTENT_URI,
            "${CalendarContract.Attendees.EVENT_ID} = ?",
            arrayOf(eventId.toString()),
        )

        attendees.forEach { email ->
            val values = ContentValues().apply {
                put(CalendarContract.Attendees.EVENT_ID, eventId)
                put(CalendarContract.Attendees.ATTENDEE_EMAIL, email)
                put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_ATTENDEE)
                put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_REQUIRED)
                put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED)
            }
            contentResolver.insert(CalendarContract.Attendees.CONTENT_URI, values)
        }
    }

    private fun requestCalendarSync() {
        val projection = arrayOf(
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE,
        )
        contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            "${CalendarContract.Calendars.ACCOUNT_TYPE} = ?",
            arrayOf(GOOGLE_ACCOUNT_TYPE),
            null,
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val accountName = cursor.getString(0)
                val accountType = cursor.getString(1)
                val account = Account(accountName, accountType)
                val extras = Bundle().apply {
                    putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
                    putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
                }
                ContentResolver.requestSync(account, CalendarContract.AUTHORITY, extras)
            }
        }
    }

    private fun getDefaultCalendarId(): Long {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.Calendars.OWNER_ACCOUNT,
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
        )

        var googleOwnerId: Long? = null
        contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            null,
            null,
            null,
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(0)
                val accountName = cursor.getString(1).orEmpty()
                val accountType = cursor.getString(2).orEmpty()
                val ownerAccount = cursor.getString(3).orEmpty()
                val accessLevel = cursor.getInt(4)

                if (googleOwnerId == null && isGoogleOwnerCalendar(accountType, ownerAccount, accountName, accessLevel)) {
                    googleOwnerId = id
                }
            }
        }

        if (googleOwnerId != null) return googleOwnerId!!

        contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            arrayOf(CalendarContract.Calendars._ID),
            "${CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL} >= ?",
            arrayOf(CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR.toString()),
            null,
        )?.use { cursor ->
            if (cursor.moveToFirst()) return cursor.getLong(0)
        }

        return 1
    }

    private fun isGoogleOwnerCalendar(
        accountType: String,
        ownerAccount: String,
        accountName: String,
        accessLevel: Int,
    ): Boolean = accountType == GOOGLE_ACCOUNT_TYPE &&
        ownerAccount == accountName &&
        accessLevel >= CalendarContract.Calendars.CAL_ACCESS_OWNER

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
