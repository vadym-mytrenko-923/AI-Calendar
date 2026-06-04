package com.agents.app.demo.ui.screens.calendar.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.marginPrimary2X
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayOfWeekHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = marginPrimary2X),
    ) {
        DayOfWeek.entries.forEach { day ->
            Text(
                modifier = Modifier.weight(1f),
                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekHeaderPreview() {
    AiAgenticAppTheme {
        DayOfWeekHeader()
    }
}
