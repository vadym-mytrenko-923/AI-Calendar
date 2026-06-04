package com.ai.calendar.demo.ui.screens.calendar.addedit.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DurationChipUiModel(
    val option: DurationOption,
    val isSelected: Boolean,
) : Parcelable
