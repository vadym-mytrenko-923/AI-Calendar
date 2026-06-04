package com.ai.calendar.demo.ui.screens.calendar

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ai.calendar.demo.ui.screens.calendar.composable.CalendarContent
import com.ai.calendar.demo.ui.screens.calendar.composable.preview.CalendarPreviewData
import com.ai.calendar.demo.ui.theme.AiCalendarTheme

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onUserIntent(
            if (granted) CalendarIntent.PermissionGranted else CalendarIntent.PermissionDenied
        )
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_CALENDAR)
    }

    CalendarScreenContent(state = state, onIntent = viewModel::onUserIntent)
}

@Composable
private fun CalendarScreenContent(
    modifier: Modifier = Modifier,
    state: CalendarScreenState,
    onIntent: (CalendarIntent) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            CalendarContent(
                modifier = Modifier.weight(1f),
                state = state,
                onIntent = onIntent,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarScreenContentPreview() {
    AiCalendarTheme {
        CalendarScreenContent(state = CalendarPreviewData.state, onIntent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarScreenContentEmptyPreview() {
    AiCalendarTheme {
        CalendarScreenContent(state = CalendarPreviewData.emptyState, onIntent = {})
    }
}
