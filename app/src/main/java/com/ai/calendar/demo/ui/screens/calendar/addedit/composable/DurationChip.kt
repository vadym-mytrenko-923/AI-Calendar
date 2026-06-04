package com.ai.calendar.demo.ui.screens.calendar.addedit.composable

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ai.calendar.demo.ui.core.modifier.shadowGlow
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.appColorsScheme
import com.ai.calendar.demo.ui.theme.btnCornerRadius
import com.ai.calendar.demo.ui.theme.marginPrimary
import com.ai.calendar.demo.ui.theme.marginPrimaryHalf

@Composable
fun DurationChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    if (selected) {
        Button(
            modifier = Modifier.shadowGlow(
                color = MaterialTheme.appColorsScheme.btnPrimaryShadow,
                blurRadius = marginPrimary,
                offsetY = marginPrimaryHalf,
                borderRadius = btnCornerRadius,
            ),
            onClick = {},
            contentPadding = ButtonDefaults.TextButtonContentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            contentPadding = ButtonDefaults.TextButtonContentPadding,
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DurationChipSelectedPreview() {
    AiCalendarTheme {
        DurationChip(label = "1h", selected = true, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun DurationChipUnselectedPreview() {
    AiCalendarTheme {
        DurationChip(label = "30m", selected = false, onClick = {})
    }
}
