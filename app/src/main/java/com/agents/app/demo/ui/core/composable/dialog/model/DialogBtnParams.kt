package com.agents.app.demo.ui.core.composable.dialog.model

data class DialogBtnParams(
    val text: String,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit,
)
