package com.ai.calendar.demo.ui.screens.calendar.addedit.mapper

import com.ai.calendar.demo.ui.screens.calendar.addedit.model.DurationChipUiModel
import com.ai.calendar.demo.ui.screens.calendar.addedit.model.DurationOption

fun List<DurationOption>.toDurationChips(selected: DurationOption): List<DurationChipUiModel> = map {
    DurationChipUiModel(option = it, isSelected = it == selected)
}
