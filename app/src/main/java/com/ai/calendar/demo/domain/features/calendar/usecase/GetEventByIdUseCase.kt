package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseUseCase
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetEventByIdUseCase @Inject constructor(
    private val repository: CalendarRepository,
) : BaseUseCase<Long, Result<CalendarEvent?>>() {
    override suspend fun execute(parameters: Long): Result<CalendarEvent?> = useResultWrapper {
        repository.getEventById(parameters)
    }
}
