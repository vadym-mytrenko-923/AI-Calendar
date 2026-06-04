package com.ai.calendar.demo.ui.screens.calendar.addedit.mapper

import com.ai.calendar.demo.ui.screens.calendar.addedit.model.DurationChipUiModel
import com.ai.calendar.demo.ui.screens.calendar.addedit.model.DurationOption

// TODO pass selected option as param, while map on List<DurationOption>
fun DurationOption.toDurationChips(): List<DurationChipUiModel> = DurationOption.entries.map {
    DurationChipUiModel(option = it, isSelected = it == this)
}
