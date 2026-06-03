package com.agents.app.demo.ui.screens.settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsScreenState(
    val isLoading: Boolean = false,
    val showLogoutDialog: Boolean = false,
) : Parcelable

sealed interface SettingsIntent {
    data object LogoutBtnClicked : SettingsIntent
    data object LogoutConfirmed : SettingsIntent
    data object LogoutDialogDismissed : SettingsIntent
}
