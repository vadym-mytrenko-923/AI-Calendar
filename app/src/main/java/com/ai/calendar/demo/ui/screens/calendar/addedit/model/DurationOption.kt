package com.ai.calendar.demo.ui.screens.calendar.addedit.model

import androidx.annotation.StringRes
import com.ai.calendar.demo.R

enum class DurationOption(val minutes: Int, @param:StringRes val labelRes: Int) {
    THIRTY_MIN(30, R.string.editorDuration30m),
    ONE_HOUR(60, R.string.editorDuration1h),
    NINETY_MIN(90, R.string.editorDuration1_5h),
    TWO_HOURS(120, R.string.editorDuration2h);

    companion object {
        fun fromMinutes(minutes: Int): DurationOption = entries.firstOrNull { it.minutes == minutes } ?: ONE_HOUR
    }
}
