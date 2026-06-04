package com.ai.calendar.demo.ui.core.error.model

import com.ai.calendar.demo.ui.theme.AppIcons
import com.ai.calendar.demo.utils.StringResource

data class UiError(
    val title: StringResource? = null,
    val message: StringResource? = null,
    val icon: Int? = AppIcons.Warning,
)
