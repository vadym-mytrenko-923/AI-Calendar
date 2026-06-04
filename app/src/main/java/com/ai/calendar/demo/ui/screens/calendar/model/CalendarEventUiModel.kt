package com.ai.calendar.demo.ui.screens.calendar.model

import android.os.Parcelable
import com.ai.calendar.demo.utils.StringResource
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarEventUiModel(
    val id: Long,
    val title: String,
    val timeRange: StringResource,
    val location: String,
    val isAllDay: Boolean,
    val startMillis: Long,
) : Parcelable
