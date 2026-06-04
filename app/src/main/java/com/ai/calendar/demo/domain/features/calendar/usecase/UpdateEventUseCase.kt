package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseUseCase
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import dagger.Reusable
import javax.inject.Inject

@Reusable
class UpdateEventUseCase @Inject constructor(
    private val repository: CalendarRepository,
) : BaseUseCase<CalendarEvent, Result<Unit>>() {
    override suspend fun execute(parameters: CalendarEvent): Result<Unit> = useResultWrapper {
        repository.updateEvent(parameters)
    }
}
