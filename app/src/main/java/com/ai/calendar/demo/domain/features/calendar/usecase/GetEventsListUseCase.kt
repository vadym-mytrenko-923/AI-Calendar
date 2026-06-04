package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseUseCase
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import com.ai.calendar.demo.domain.features.calendar.model.DateRange
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetEventsListUseCase @Inject constructor(
    private val repository: CalendarRepository,
) : BaseUseCase<DateRange, Result<List<CalendarEvent>>>() {
    override suspend fun execute(parameters: DateRange): Result<List<CalendarEvent>> = useResultWrapper {
        repository.getEventsList(parameters)
    }
}
