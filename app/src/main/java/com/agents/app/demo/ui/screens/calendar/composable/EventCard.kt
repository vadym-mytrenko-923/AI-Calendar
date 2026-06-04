package com.agents.app.demo.ui.screens.calendar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.ui.screens.calendar.composable.preview.CalendarPreviewData
import com.agents.app.demo.ui.screens.calendar.model.CalendarEventUiModel
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.containerShapeSmall
import com.agents.app.demo.ui.theme.marginPrimary
import com.agents.app.demo.ui.theme.marginPrimary1_5X
import com.agents.app.demo.ui.theme.marginPrimary2X
import com.agents.app.demo.ui.theme.marginPrimaryHalf

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    event: CalendarEventUiModel,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = containerShapeSmall,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(modifier = Modifier.padding(horizontal = marginPrimary2X, vertical = marginPrimary1_5X)) {
            Text(
                text = event.timeRange.getString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(marginPrimaryHalf))

            Text(
                text = event.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (event.location.isNotBlank()) {
                Spacer(modifier = Modifier.height(marginPrimaryHalf))

                Text(
                    text = event.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventCardPreview() {
    AiAgenticAppTheme {
        EventCard(event = CalendarPreviewData.events.first())
    }
}

@Preview(showBackground = true)
@Composable
private fun EventCardNoLocationPreview() {
    AiAgenticAppTheme {
        EventCard(event = CalendarPreviewData.events.last())
    }
}
