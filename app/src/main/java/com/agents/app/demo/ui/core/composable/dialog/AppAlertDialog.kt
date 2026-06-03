package com.agents.app.demo.ui.core.composable.dialog

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.ui.core.composable.button.BaseTextButton
import com.agents.app.demo.ui.core.composable.button.PrimaryTextButton
import com.agents.app.demo.ui.core.composable.dialog.model.DialogBtnParams
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.appColorsScheme
import com.agents.app.demo.ui.theme.marginPrimary

@Composable
fun AppAlertDialog(
    modifier: Modifier = Modifier,
    message: String,
    title: String? = null,
    confirmButton: DialogBtnParams,
    dismissButton: DialogBtnParams? = null,
    isDismissable: Boolean = true,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = { if (isDismissable) onDismiss() },
        containerColor = MaterialTheme.appColorsScheme.background,
        title = title?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.appColorsScheme.textPrimary,
                )
            }
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.appColorsScheme.textSecondary,
            )
        },
        confirmButton = { confirmButton.DialogButton() },
        dismissButton = dismissButton?.let { { it.DialogButton() } },
    )
}

@Composable
private fun DialogBtnParams.DialogButton() {
    if (isDestructive) {
        BaseTextButton(
            text = text,
            onClick = onClick,
            modifier = Modifier,
            isEnabled = true,
            contentPadding = PaddingValues(horizontal = marginPrimary, vertical = marginPrimary),
            defaultColor = MaterialTheme.appColorsScheme.error,
            pressedColor = MaterialTheme.appColorsScheme.error.copy(alpha = 0.7f),
            disabledColor = MaterialTheme.appColorsScheme.textPrimaryDisabled,
        )
    } else {
        PrimaryTextButton(
            text = text,
            onClick = onClick,
        )
    }
}

@Preview
@Composable
private fun AppAlertDialogPreview() {
    AiAgenticAppTheme {
        AppAlertDialog(
            title = "Confirm Action",
            message = "Are you sure you want to proceed?",
            confirmButton = DialogBtnParams(text = "OK", onClick = {}),
            dismissButton = DialogBtnParams(text = "Cancel", onClick = {}),
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun AppAlertDialogDestructivePreview() {
    AiAgenticAppTheme {
        AppAlertDialog(
            title = "Log Out",
            message = "Are you sure you want to log out?",
            confirmButton = DialogBtnParams(text = "Log Out", isDestructive = true, onClick = {}),
            dismissButton = DialogBtnParams(text = "Cancel", onClick = {}),
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun AppAlertDialogMessageOnlyPreview() {
    AiAgenticAppTheme {
        AppAlertDialog(
            message = "Profile updated successfully.",
            confirmButton = DialogBtnParams(text = "OK", onClick = {}),
            onDismiss = {},
        )
    }
}
