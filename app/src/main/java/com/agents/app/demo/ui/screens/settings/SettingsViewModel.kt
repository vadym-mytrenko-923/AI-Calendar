package com.agents.app.demo.ui.screens.settings

import androidx.lifecycle.SavedStateHandle
import com.agents.app.demo.domain.features.auth.usecase.LogoutUseCase
import com.agents.app.demo.ui.base.BaseViewModel
import com.agents.app.demo.ui.navigation.app.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appNavigator: AppNavigator,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<SettingsScreenState, SettingsIntent, Nothing>(
    initialState = SettingsScreenState(),
    savedStateHandle = savedStateHandle
) {
    override fun reduceIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.LogoutBtnClicked -> updateUiState { it.copy(showLogoutDialog = true) }
            SettingsIntent.LogoutDialogDismissed -> updateUiState { it.copy(showLogoutDialog = false) }
            SettingsIntent.LogoutConfirmed -> launchViewModelScope {
                updateUiState { it.copy(showLogoutDialog = false) }
                logoutUseCase()
            }
        }
    }
}
