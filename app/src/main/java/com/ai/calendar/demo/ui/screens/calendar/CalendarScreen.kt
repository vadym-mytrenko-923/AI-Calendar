package com.ai.calendar.demo.ui.screens.calendar

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ai.calendar.demo.R
import com.ai.calendar.demo.ui.screens.calendar.addedit.AddEditEventBottomSheet
import com.ai.calendar.demo.ui.screens.calendar.addedit.AddEditEventIntent
import com.ai.calendar.demo.ui.screens.calendar.addedit.AddEditEventState
import com.ai.calendar.demo.ui.screens.calendar.composable.CalendarContent
import com.ai.calendar.demo.ui.screens.calendar.composable.preview.CalendarPreviewData
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.AppIcons

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val addEditState by viewModel.addEditEventSvm.uiStateFlow.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        viewModel.onUserIntent(
            if (permissions.values.all { it }) CalendarIntent.PermissionGranted else CalendarIntent.PermissionDenied
        )
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR))
    }

    CalendarScreenContent(
        state = state,
        addEditEventState = addEditState,
        onUserIntent = viewModel::onUserIntent,
        onAddEditEventUserIntent = viewModel.addEditEventSvm::onUserIntent
    )
}

@Composable
private fun CalendarScreenContent(
    state: CalendarScreenState,
    addEditEventState: AddEditEventState,
    onUserIntent: (CalendarIntent) -> Unit,
    onAddEditEventUserIntent: (AddEditEventIntent) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (state.hasPermission) {
                FloatingActionButton(
                    onClick = { onUserIntent(CalendarIntent.AddFabClicked) },
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(AppIcons.Add),
                        contentDescription = stringResource(R.string.editorFabContentDescription),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = scaffoldPadding.calculateTopPadding(),
                    bottom = scaffoldPadding.calculateBottomPadding()
                ),
        ) {
            CalendarContent(
                modifier = Modifier.weight(1f),
                state = state,
                onIntent = onUserIntent,
            )
        }

        if (state.isAddEditBottomSheetVisible) {
            AddEditEventBottomSheet(
                bottomPadding = scaffoldPadding.calculateBottomPadding(),
                state = addEditEventState,
                onIntent = onAddEditEventUserIntent,
                onDismiss = { onUserIntent(CalendarIntent.AddEditBottomSheetDismissed) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarScreenPreview() {
    AiCalendarTheme {
        Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                CalendarContent(
                    modifier = Modifier.weight(1f),
                    state = CalendarPreviewData.state,
                    onIntent = {},
                )
            }
        }
    }
}
