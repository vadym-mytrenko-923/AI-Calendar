package com.agents.app.demo.ui.screens.calendar.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.R
import com.agents.app.demo.ui.screens.calendar.CalendarIntent
import com.agents.app.demo.ui.screens.calendar.CalendarScreenState
import com.agents.app.demo.ui.screens.calendar.composable.preview.CalendarPreviewData
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.marginPrimary
import com.agents.app.demo.ui.theme.marginPrimary2X

@Composable
fun CalendarContent(
    modifier: Modifier = Modifier,
    state: CalendarScreenState,
    onIntent: (CalendarIntent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(marginPrimary),
    ) {
        MonthHeader(
            month = state.currentMonth,
            showTodayButton = !state.isToday,
            onPrevious = { onIntent(CalendarIntent.PreviousMonthClicked) },
            onNext = { onIntent(CalendarIntent.NextMonthClicked) },
            onTodayClicked = { onIntent(CalendarIntent.TodayClicked) },
        )

        DayOfWeekHeader()

        MonthGrid(
            grid = state.monthGrid,
            onDaySelected = { onIntent(CalendarIntent.DaySelected(it)) },
        )

        Spacer(modifier = Modifier.height(marginPrimary))

        if (!state.hasPermission) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.calendarPermissionRequired),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            Text(
                modifier = Modifier.padding(horizontal = marginPrimary2X),
                text = state.selectedDayLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
            )

            AnimatedContent(
                targetState = state.selectedDayEvents,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "eventsTransition",
            ) { events ->
                DayEventsList(
                    modifier = Modifier.weight(1f),
                    events = events,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarContentPreview() {
    AiAgenticAppTheme {
        CalendarContent(state = CalendarPreviewData.state, onIntent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarContentEmptyPreview() {
    AiAgenticAppTheme {
        CalendarContent(state = CalendarPreviewData.emptyState, onIntent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarContentNoPermissionPreview() {
    AiAgenticAppTheme {
        CalendarContent(state = CalendarPreviewData.noPermissionState, onIntent = {})
    }
}
