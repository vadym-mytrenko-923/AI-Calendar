package com.ai.calendar.demo.ui.screens.calendar.addedit

import com.ai.calendar.demo.di.calendar.CALENDAR_EDITOR_DATE_FORMATTER
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.usecase.CreateEventUseCase
import com.ai.calendar.demo.domain.features.calendar.usecase.DeleteEventUseCase
import com.ai.calendar.demo.domain.features.calendar.usecase.UpdateEventUseCase
import com.ai.calendar.demo.ui.base.BaseSubViewModel
import com.ai.calendar.demo.ui.screens.calendar.addedit.mapper.toDurationChips
import com.ai.calendar.demo.ui.screens.calendar.addedit.model.DurationOption
import com.ai.calendar.demo.ui.screens.calendar.utils.DateFormatter
import com.ai.calendar.demo.ui.screens.calendar.utils.TimeFormatter
import com.ai.calendar.demo.ui.screens.calendar.utils.isValidEmail
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named

class AddEditEventSvm @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    @param:Named(CALENDAR_EDITOR_DATE_FORMATTER) private val editorDateFormatter: DateFormatter,
    private val timeFormatter: TimeFormatter,
) : BaseSubViewModel<AddEditEventState, AddEditEventIntent, AddEditEventEffect>(
    initialState = AddEditEventState(),
) {

    @Suppress("CyclomaticComplexMethod")
    override fun reduceIntent(intent: AddEditEventIntent) {
        when (intent) {
            is AddEditEventIntent.TitleChanged -> updateUiState { it.copy(title = intent.title) }
            is AddEditEventIntent.NotesChanged -> updateUiState { it.copy(notes = intent.notes) }
            is AddEditEventIntent.DateChanged -> updateUiState { it.copy(date = intent.date) }
            is AddEditEventIntent.StartTimeChanged -> onStartTimeChanged(intent.time)
            is AddEditEventIntent.EndTimeChanged -> onEndTimeChanged(intent.time)
            is AddEditEventIntent.DurationChanged -> onDurationChanged(intent.option)
            is AddEditEventIntent.StartTimeFieldClicked -> showStartTimePicker()
            is AddEditEventIntent.EndTimeFieldClicked -> showEndTimePicker()
            is AddEditEventIntent.TimePickerDismissed -> dismissTimePickers()
            is AddEditEventIntent.AttendeeInputChanged -> onAttendeeInputChanged(intent.input)
            is AddEditEventIntent.AddAttendee -> addAttendee()
            is AddEditEventIntent.RemoveAttendee -> removeAttendee(intent.email)
            is AddEditEventIntent.SaveClicked -> saveEvent()
            is AddEditEventIntent.DeleteClicked -> deleteEvent()
        }
    }

    fun showCreationBottomSheet(date: LocalDate) {
        val startTime = LocalTime.now().withMinute(0).plusHours(1)
        val endTime = startTime.plusHours(1)
        updateUiState {
            AddEditEventState(
                date = date,
                startTime = startTime,
                endTime = endTime,
                formattedDate = editorDateFormatter.format(date),
                formattedStartTime = timeFormatter.format(startTime),
                formattedEndTime = timeFormatter.format(endTime),
            )
        }
    }

    fun showEditBottomSheet(event: CalendarEvent) {
        val zone = ZoneId.systemDefault()
        val startDateTime = Instant.ofEpochMilli(event.startMillis).atZone(zone)
        val endDateTime = Instant.ofEpochMilli(event.endMillis).atZone(zone)
        val durationMinutes = Duration.between(startDateTime, endDateTime).toMinutes().toInt()

        val date = startDateTime.toLocalDate()
        val start = startDateTime.toLocalTime()
        val end = endDateTime.toLocalTime()
        val durationOption = DurationOption.fromMinutes(durationMinutes)

        updateUiState {
            AddEditEventState(
                eventId = event.id,
                calendarId = event.calendarId,
                title = event.title,
                notes = event.description,
                date = date,
                startTime = start,
                endTime = end,
                selectedDurationOption = durationOption,
                attendees = event.attendees,
                formattedDate = editorDateFormatter.format(date),
                formattedStartTime = timeFormatter.format(start),
                formattedEndTime = timeFormatter.format(end),
                durationChips = DurationOption.entries.toDurationChips(durationOption),
            )
        }
    }

    private fun showStartTimePicker() = updateUiState {
        it.copy(showStartTimePicker = true)
    }

    private fun showEndTimePicker() = updateUiState {
        it.copy(showEndTimePicker = true)
    }

    private fun dismissTimePickers() = updateUiState {
        it.copy(showStartTimePicker = false, showEndTimePicker = false)
    }

    private fun onAttendeeInputChanged(input: String) = updateUiState {
        it.copy(attendeeInput = input, showAttendeeError = false)
    }

    private fun onStartTimeChanged(newStart: LocalTime) {
        val duration = Duration.between(uiState.startTime, uiState.endTime)
        val newEnd = newStart.plus(duration)
        val durationOption = DurationOption.fromMinutes(duration.toMinutes().toInt())
        updateUiState {
            it.copy(
                startTime = newStart,
                endTime = newEnd,
                formattedStartTime = timeFormatter.format(newStart),
                formattedEndTime = timeFormatter.format(newEnd),
                selectedDurationOption = durationOption,
                durationChips = DurationOption.entries.toDurationChips(durationOption),
                showStartTimePicker = false,
            )
        }
    }

    private fun onEndTimeChanged(newEnd: LocalTime) {
        val durationMinutes = Duration.between(uiState.startTime, newEnd).toMinutes().toInt()
        val clampedEnd = if (durationMinutes <= 0) uiState.startTime.plusMinutes(30) else newEnd
        val actualDuration = Duration.between(uiState.startTime, clampedEnd).toMinutes().toInt()
        val durationOption = DurationOption.fromMinutes(actualDuration)
        updateUiState {
            it.copy(
                endTime = clampedEnd,
                formattedEndTime = timeFormatter.format(clampedEnd),
                selectedDurationOption = durationOption,
                durationChips = DurationOption.entries.toDurationChips(durationOption),
                showEndTimePicker = false,
            )
        }
    }

    private fun onDurationChanged(option: DurationOption) {
        val newEnd = uiState.startTime.plusMinutes(option.minutes.toLong())
        updateUiState {
            it.copy(
                endTime = newEnd,
                formattedEndTime = timeFormatter.format(newEnd),
                selectedDurationOption = option,
                durationChips = DurationOption.entries.toDurationChips(option),
            )
        }
    }

    private fun addAttendee() {
        val email = uiState.attendeeInput.trim()
        if (!isValidEmail(email)) {
            updateUiState { it.copy(showAttendeeError = true) }
            return
        }

        if (email in uiState.attendees) return

        updateUiState {
            it.copy(
                attendees = it.attendees + email,
                attendeeInput = "",
                showAttendeeError = false,
            )
        }
    }

    private fun removeAttendee(email: String) {
        updateUiState { it.copy(attendees = it.attendees - email) }
    }

    private fun saveEvent() {
        if (!uiState.isValid) return

        val zone = ZoneId.systemDefault()
        val startMillis = uiState.date.atTime(uiState.startTime).atZone(zone).toInstant().toEpochMilli()
        val endMillis = uiState.date.atTime(uiState.endTime).atZone(zone).toInstant().toEpochMilli()
        val event = CalendarEvent(
            id = uiState.eventId ?: 0,
            calendarId = uiState.calendarId,
            title = uiState.title,
            description = uiState.notes,
            startMillis = startMillis,
            endMillis = endMillis,
            attendees = uiState.attendees,
        )

        launchSvmScope {
            updateUiState { it.copy(isSaving = true) }

            val result = if (uiState.isEditing) {
                updateEventUseCase(event)
            } else {
                createEventUseCase(event).map { }
            }

            result.onSuccess {
                sendUiEffect(AddEditEventEffect.Saved)
            }

            updateUiState { it.copy(isSaving = false) }
        }
    }

    private fun deleteEvent() {
        val eventId = uiState.eventId ?: return

        launchSvmScope {
            updateUiState { it.copy(isSaving = true) }

            deleteEventUseCase(eventId).onSuccess {
                sendUiEffect(AddEditEventEffect.Deleted)
            }

            updateUiState { it.copy(isSaving = false) }
        }
    }
}
