package com.ai.calendar.demo.ui.screens.calendar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MonthGridUiModel(
    val weeks: List<List<DayCellUiModel?>> = emptyList(),
) : Parcelable
