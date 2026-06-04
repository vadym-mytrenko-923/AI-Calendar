package com.agents.app.demo.ui.screens.calendar.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.ui.screens.calendar.composable.preview.CalendarPreviewData
import com.agents.app.demo.ui.screens.calendar.mapper.MonthGridBuilder
import com.agents.app.demo.ui.screens.calendar.model.MonthGridUiModel
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.marginPrimary
import com.agents.app.demo.ui.theme.marginPrimary2X
import java.time.LocalDate

@Composable
fun MonthGrid(
    modifier: Modifier = Modifier,
    grid: MonthGridUiModel,
    onDaySelected: (LocalDate) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = marginPrimary2X),
        verticalArrangement = Arrangement.spacedBy(marginPrimary),
    ) {
        grid.weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { cell ->
                    if (cell != null) {
                        DayCell(
                            modifier = Modifier.weight(1f),
                            day = cell.day,
                            isSelected = cell.isSelected,
                            isToday = cell.isToday,
                            hasEvents = cell.hasEvents,
                            onClick = { onDaySelected(cell.date) },
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthGridPreview() {
    AiAgenticAppTheme {
        val grid = MonthGridBuilder().buildMonthGrid(
            month = CalendarPreviewData.state.currentMonth,
            selectedDate = CalendarPreviewData.state.selectedDate,
            events = CalendarPreviewData.events,
        )

        MonthGrid(grid = grid, onDaySelected = {})
    }
}
