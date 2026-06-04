package com.ai.calendar.demo.ui.screens.calendar.addedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.ai.calendar.demo.R
import com.ai.calendar.demo.ui.core.composable.button.BtnPrimary
import com.ai.calendar.demo.ui.core.composable.button.PrimaryTextButton
import com.ai.calendar.demo.ui.screens.calendar.addedit.composable.DurationChip
import com.ai.calendar.demo.ui.screens.calendar.addedit.composable.EventTimePicker
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.AppIcons
import com.ai.calendar.demo.ui.theme.marginPrimary
import com.ai.calendar.demo.ui.theme.marginPrimary2X
import com.ai.calendar.demo.ui.theme.marginPrimaryHalf
import com.ai.calendar.demo.ui.theme.marginZero
import com.ai.calendar.demo.ui.theme.smallIconSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventBottomSheet(
    modifier: Modifier = Modifier,
    bottomPadding: Dp = marginZero,
    state: AddEditEventState,
    onIntent: (AddEditEventIntent) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        AddEditEventContent(
            bottomPadding = bottomPadding,
            state = state,
            onIntent = onIntent,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddEditEventContent(
    modifier: Modifier = Modifier,
    bottomPadding: Dp = marginZero,
    state: AddEditEventState,
    onIntent: (AddEditEventIntent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = marginPrimary2X)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(marginPrimary),
    ) {
        Text(
            text = stringResource(if (state.isEditing) R.string.editorTitleEdit else R.string.editorTitleNew),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(marginPrimaryHalf))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = { onIntent(AddEditEventIntent.TitleChanged(it)) },
            label = { Text(stringResource(R.string.editorLabelTitle)) },
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.formattedDate,
            onValueChange = {},
            label = { Text(stringResource(R.string.editorLabelDate)) },
            readOnly = true,
            enabled = false,
            singleLine = true,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(marginPrimary)) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onIntent(AddEditEventIntent.StartTimeFieldClicked) },
                value = state.formattedStartTime,
                onValueChange = {},
                label = { Text(stringResource(R.string.editorLabelStartTime)) },
                readOnly = true,
                enabled = false,
                singleLine = true,
            )

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onIntent(AddEditEventIntent.EndTimeFieldClicked) },
                value = state.formattedEndTime,
                onValueChange = {},
                label = { Text(stringResource(R.string.editorLabelEndTime)) },
                readOnly = true,
                enabled = false,
                singleLine = true,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(marginPrimaryHalf)) {
            state.durationChips.forEach { chip ->
                DurationChip(
                    label = stringResource(chip.option.labelRes),
                    selected = chip.isSelected,
                    onClick = { onIntent(AddEditEventIntent.DurationChanged(chip.option)) },
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.attendeeInput,
            onValueChange = { onIntent(AddEditEventIntent.AttendeeInputChanged(it)) },
            label = { Text(stringResource(R.string.editorLabelAttendees)) },
            placeholder = { Text(stringResource(R.string.editorAttendeePlaceholder)) },
            singleLine = true,
            isError = state.showAttendeeError,
            supportingText = if (state.showAttendeeError) {
                { Text(stringResource(R.string.editorAttendeeError)) }
            } else {
                null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onIntent(AddEditEventIntent.AddAttendee) },
            ),
            trailingIcon = {
                if (state.attendeeInput.isNotBlank()) {
                    IconButton(onClick = { onIntent(AddEditEventIntent.AddAttendee) }) {
                        Icon(painterResource(AppIcons.Add), contentDescription = null)
                    }
                }
            },
        )

        if (state.attendees.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(marginPrimaryHalf),
                verticalArrangement = Arrangement.spacedBy(marginPrimaryHalf),
            ) {
                state.attendees.forEach { email ->
                    AssistChip(
                        onClick = { onIntent(AddEditEventIntent.RemoveAttendee(email)) },
                        label = { Text(email, style = MaterialTheme.typography.bodySmall) },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.size(smallIconSize),
                                painter = painterResource(AppIcons.Close),
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(marginPrimary * 12),
            value = state.notes,
            onValueChange = { onIntent(AddEditEventIntent.NotesChanged(it)) },
            label = { Text(stringResource(R.string.editorLabelNotes)) },
        )

        Spacer(modifier = Modifier.height(marginPrimary))

        BtnPrimary(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (!state.isEditing) Modifier.padding(bottom = marginPrimary2X + bottomPadding) else Modifier),
            onClick = { onIntent(AddEditEventIntent.SaveClicked) },
            isEnabled = state.isValid && !state.isSaving,
            text = stringResource(R.string.editorBtnSave),
        )

        if (state.isEditing) {
            Spacer(modifier = Modifier.height(marginPrimaryHalf))

            PrimaryTextButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = marginPrimary2X + bottomPadding),
                onClick = { onIntent(AddEditEventIntent.DeleteClicked) },
                isEnabled = !state.isSaving,
                text = stringResource(R.string.editorBtnDelete),
                defaultColor = MaterialTheme.colorScheme.error,
                pressedColor = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                disabledColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
            )
        }
    }

    if (state.showStartTimePicker) {
        EventTimePicker(
            initialTime = state.startTime,
            onConfirm = { onIntent(AddEditEventIntent.StartTimeChanged(it)) },
            onDismiss = { onIntent(AddEditEventIntent.TimePickerDismissed) },
        )
    }

    if (state.showEndTimePicker) {
        EventTimePicker(
            initialTime = state.endTime,
            onConfirm = { onIntent(AddEditEventIntent.EndTimeChanged(it)) },
            onDismiss = { onIntent(AddEditEventIntent.TimePickerDismissed) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditEventContentNewPreview() {
    AiCalendarTheme {
        AddEditEventContent(state = AddEditEventState(), onIntent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditEventContentEditPreview() {
    AiCalendarTheme {
        AddEditEventContent(
            state = AddEditEventState(
                eventId = 1,
                title = "Team standup",
                notes = "Daily sync with the team",
                attendees = listOf("john@example.com", "sarah@example.com"),
            ),
            onIntent = {},
        )
    }
}
