package com.agents.app.demo.ui.core.error.model

import com.agents.app.demo.ui.theme.AppIcons
import com.agents.app.demo.utils.StringResource

data class UiError(
    val title: StringResource? = null,
    val message: StringResource? = null,
    val icon: Int? = AppIcons.Warning,
)
