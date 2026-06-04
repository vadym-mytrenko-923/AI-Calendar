package com.agents.app.demo.ui.screens.calendar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class DayCellUiModel(
    val date: LocalDate,
    val day: Int,
    val isSelected: Boolean,
    val isToday: Boolean,
    val hasEvents: Boolean,
    val events: List<CalendarEventUiModel> = emptyList(),
) : Parcelable
