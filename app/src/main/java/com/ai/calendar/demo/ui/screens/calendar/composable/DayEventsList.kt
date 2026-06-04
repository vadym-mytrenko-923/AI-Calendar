package com.ai.calendar.demo.ui.screens.calendar.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ai.calendar.demo.R
import com.ai.calendar.demo.ui.screens.calendar.composable.preview.CalendarPreviewData
import com.ai.calendar.demo.ui.screens.calendar.model.CalendarEventUiModel
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.marginPrimary1_5X
import com.ai.calendar.demo.ui.theme.marginPrimary2X

@Composable
fun DayEventsList(
    modifier: Modifier = Modifier,
    events: List<CalendarEventUiModel>,
    onEventClick: (CalendarEventUiModel) -> Unit = {},
) {
    if (events.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(marginPrimary2X),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.calendarNoEvents),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = marginPrimary2X),
            verticalArrangement = Arrangement.spacedBy(marginPrimary1_5X),
        ) {
            items(events, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    onClick = { onEventClick(event) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayEventsListPreview() {
    AiCalendarTheme {
        DayEventsList(events = CalendarPreviewData.events)
    }
}

@Preview(showBackground = true)
@Composable
private fun DayEventsListEmptyPreview() {
    AiCalendarTheme {
        DayEventsList(events = emptyList())
    }
}
