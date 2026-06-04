package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.DateRange
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SetDateRangeUseCase @Inject constructor(
    private val repository: CalendarRepository,
) {
    operator fun invoke(range: DateRange) = repository.setDateRange(range)
}
