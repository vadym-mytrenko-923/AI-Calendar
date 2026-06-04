package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.base.usecase.BaseNoParamsFlowUseCase
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.model.CalendarEvent
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetEventsListUseCase @Inject constructor(
    private val repository: CalendarRepository,
) : BaseNoParamsFlowUseCase<List<CalendarEvent>>() {
    override fun execute(): Flow<List<CalendarEvent>> = repository.eventsFlow
}
