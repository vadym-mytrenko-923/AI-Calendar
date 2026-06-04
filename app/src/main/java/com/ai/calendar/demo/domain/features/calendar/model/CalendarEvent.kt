package com.ai.calendar.demo.domain.features.calendar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarEvent(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val location: String = "",
    val startMillis: Long,
    val endMillis: Long,
    val isAllDay: Boolean = false,
    val calendarId: Long = 0,
    val attendees: List<String> = emptyList(),
) : Parcelable
