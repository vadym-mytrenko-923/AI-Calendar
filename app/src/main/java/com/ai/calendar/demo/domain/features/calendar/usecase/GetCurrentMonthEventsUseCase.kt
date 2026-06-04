package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseNoParamsUseCase
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetCurrentMonthEventsUseCase @Inject constructor(
    private val repository: CalendarRepository,
) : BaseNoParamsUseCase<Result<List<CalendarEvent>>>() {
    override suspend fun execute(): Result<List<CalendarEvent>> = useResultWrapper {
        repository.getEventsList()
    }
}
