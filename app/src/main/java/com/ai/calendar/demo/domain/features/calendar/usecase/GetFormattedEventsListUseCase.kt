package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseNoParamsUseCase
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.domain.features.calendar.mapper.CalendarEventTextMapper
import dagger.Reusable
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@Reusable
class GetFormattedEventsListUseCase @Inject constructor(
    private val repository: CalendarRepository,
    private val textMapper: CalendarEventTextMapper,
) : BaseNoParamsUseCase<Result<String>>() {

    override suspend fun execute(): Result<String> = useResultWrapper {
        val events = repository.eventsFlow.first()
        textMapper.format(events)
    }
}
