package com.ai.calendar.demo.ui.screens.calendar.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ai.calendar.demo.R
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.AppIcons
import com.ai.calendar.demo.ui.theme.appColorsScheme
import com.ai.calendar.demo.ui.theme.borderWidthDefault
import com.ai.calendar.demo.ui.theme.defaultIconSize
import com.ai.calendar.demo.ui.theme.marginPrimary2X
import com.ai.calendar.demo.ui.theme.marginPrimaryHalf
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthHeader(
    modifier: Modifier = Modifier,
    month: YearMonth,
    showTodayButton: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onTodayClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = marginPrimary2X),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = month.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
        )

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = showTodayButton,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            OutlinedButton(
                onClick = onTodayClicked,
                border = BorderStroke(borderWidthDefault, MaterialTheme.appColorsScheme.calendarTodayAccent),
            ) {
                Text(
                    text = stringResource(R.string.calendarTodayBtn),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.appColorsScheme.calendarTodayAccent,
                )
            }
        }

        Spacer(modifier = Modifier.width(marginPrimaryHalf))

        IconButton(onClick = onPrevious) {
            Icon(
                modifier = Modifier.size(defaultIconSize),
                painter = painterResource(AppIcons.ChevronLeft),
                contentDescription = stringResource(R.string.calendarPreviousMonth),
            )
        }

        IconButton(onClick = onNext) {
            Icon(
                modifier = Modifier.size(defaultIconSize),
                painter = painterResource(AppIcons.ChevronRight),
                contentDescription = stringResource(R.string.calendarNextMonth),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthHeaderPreview() {
    AiCalendarTheme {
        MonthHeader(
            month = YearMonth.now(),
            showTodayButton = false,
            onPrevious = {},
            onNext = {},
            onTodayClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthHeaderWithTodayPreview() {
    AiCalendarTheme {
        MonthHeader(
            month = YearMonth.of(2026, 1),
            showTodayButton = true,
            onPrevious = {},
            onNext = {},
            onTodayClicked = {},
        )
    }
}
