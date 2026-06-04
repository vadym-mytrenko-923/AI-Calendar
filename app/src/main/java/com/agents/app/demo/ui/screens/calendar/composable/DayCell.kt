package com.agents.app.demo.ui.screens.calendar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.appColorsScheme
import com.agents.app.demo.ui.theme.calendarDayCellSize
import com.agents.app.demo.ui.theme.eventIndicatorOffset
import com.agents.app.demo.ui.theme.eventIndicatorSize

@Composable
fun DayCell(
    modifier: Modifier = Modifier,
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    hasEvents: Boolean,
    onClick: () -> Unit,
) {
    val todayAccent = MaterialTheme.appColorsScheme.calendarTodayAccent

    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.onSurface
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.surface
        isToday -> todayAccent
        else -> MaterialTheme.colorScheme.onSurface
    }

    val fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(calendarDayCellSize)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = fontWeight,
            )
        }

        if (hasEvents) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = eventIndicatorOffset)
                    .size(eventIndicatorSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayCellDefaultPreview() {
    AiAgenticAppTheme {
        DayCell(day = 15, isSelected = false, isToday = false, hasEvents = false, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun DayCellTodayPreview() {
    AiAgenticAppTheme {
        DayCell(day = 21, isSelected = false, isToday = true, hasEvents = false, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun DayCellSelectedPreview() {
    AiAgenticAppTheme {
        DayCell(day = 9, isSelected = true, isToday = false, hasEvents = true, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun DayCellWithEventsPreview() {
    AiAgenticAppTheme {
        DayCell(day = 18, isSelected = false, isToday = false, hasEvents = true, onClick = {})
    }
}
