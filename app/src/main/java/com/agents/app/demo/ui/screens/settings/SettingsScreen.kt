package com.agents.app.demo.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.agents.app.demo.R
import com.agents.app.demo.ui.core.composable.button.BtnPrimary
import com.agents.app.demo.ui.core.composable.dialog.AppAlertDialog
import com.agents.app.demo.ui.core.composable.dialog.model.DialogBtnParams
import com.agents.app.demo.ui.theme.marginPrimary2X

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        BtnPrimary(
            modifier = Modifier.align(Alignment.Center).fillMaxWidth().padding(horizontal = marginPrimary2X),
            text = stringResource(R.string.settingsBtnLogout),
            onClick = { viewModel.onUserIntent(SettingsIntent.LogoutBtnClicked) },
        )
    }

    if (uiState.showLogoutDialog) {
        AppAlertDialog(
            title = stringResource(R.string.settingsLogoutDialogTitle),
            message = stringResource(R.string.settingsLogoutDialogMessage),
            confirmButton = DialogBtnParams(
                text = stringResource(R.string.settingsBtnLogout),
                isDestructive = true,
                onClick = { viewModel.onUserIntent(SettingsIntent.LogoutConfirmed) },
            ),
            dismissButton = DialogBtnParams(
                text = stringResource(R.string.cancel),
                onClick = { viewModel.onUserIntent(SettingsIntent.LogoutDialogDismissed) },
            ),
            onDismiss = { viewModel.onUserIntent(SettingsIntent.LogoutDialogDismissed) },
        )
    }
}
