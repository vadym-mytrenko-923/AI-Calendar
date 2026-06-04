package com.ai.calendar.demo.ui.core.alert.model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.ai.calendar.demo.ui.theme.AppIcons

data class AppAlertParams(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = false,
    val title: String? = null,
    val description: String? = null,
    val icon: Int? = AppIcons.Warning,
    val type: AppAlertType = AppAlertType.Error
) : SnackbarVisuals

sealed interface AppAlertType {
    data object Error : AppAlertType
    data object Success : AppAlertType
}
