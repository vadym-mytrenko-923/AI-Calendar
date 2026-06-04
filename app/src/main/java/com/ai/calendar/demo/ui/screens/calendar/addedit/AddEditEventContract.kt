package com.ai.calendar.demo.ui.screens.calendar.addedit

import android.os.Parcelable
import com.ai.calendar.demo.ui.screens.calendar.addedit.mapper.toDurationChips
import com.ai.calendar.demo.ui.screens.calendar.addedit.model.DurationChipUiModel
import com.ai.calendar.demo.ui.screens.calendar.addedit.model.DurationOption
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class AddEditEventState(
    val eventId: Long? = null,
    val calendarId: Long = 0,
    val title: String = "",
    val notes: String = "",
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now().withMinute(0).plusHours(1),
    val endTime: LocalTime = LocalTime.now().withMinute(0).plusHours(2),
    val selectedDurationOption: DurationOption = DurationOption.ONE_HOUR,
    val attendees: List<String> = emptyList(),
    val attendeeInput: String = "",
    val showAttendeeError: Boolean = false,
    val isSaving: Boolean = false,
    val formattedDate: String = "",
    val formattedStartTime: String = "",
    val formattedEndTime: String = "",
    val durationChips: List<DurationChipUiModel> = DurationOption.ONE_HOUR.toDurationChips(),
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false,
) : Parcelable {
    val isEditing: Boolean get() = eventId != null
    val isValid: Boolean get() = title.isNotBlank()
}

sealed interface AddEditEventIntent {
    data class TitleChanged(val title: String) : AddEditEventIntent
    data class NotesChanged(val notes: String) : AddEditEventIntent
    data class DateChanged(val date: LocalDate) : AddEditEventIntent
    data class StartTimeChanged(val time: LocalTime) : AddEditEventIntent
    data class EndTimeChanged(val time: LocalTime) : AddEditEventIntent
    data class DurationChanged(val option: DurationOption) : AddEditEventIntent
    data object StartTimeFieldClicked : AddEditEventIntent
    data object EndTimeFieldClicked : AddEditEventIntent
    data object TimePickerDismissed : AddEditEventIntent
    data class AttendeeInputChanged(val input: String) : AddEditEventIntent
    data object AddAttendee : AddEditEventIntent
    data class RemoveAttendee(val email: String) : AddEditEventIntent
    data object SaveClicked : AddEditEventIntent
    data object DeleteClicked : AddEditEventIntent
}

sealed interface AddEditEventEffect {
    data object Saved : AddEditEventEffect
    data object Deleted : AddEditEventEffect
}
