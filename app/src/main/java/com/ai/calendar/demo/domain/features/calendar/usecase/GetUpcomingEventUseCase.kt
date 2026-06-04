package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseNoParamsUseCase
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetUpcomingEventUseCase @Inject constructor(
    private val repository: CalendarRepository,
) : BaseNoParamsUseCase<Result<CalendarEvent?>>() {

    override suspend fun execute(): Result<CalendarEvent?> = useResultWrapper {
        val now = System.currentTimeMillis()
        repository.getEventsList()
            .filter { it.endMillis > now }
            .minByOrNull { it.startMillis }
    }
}
