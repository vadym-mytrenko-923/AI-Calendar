package com.ai.calendar.demo.domain.features.calendar.usecase

import com.ai.calendar.demo.domain.base.result.useResultWrapper
import com.ai.calendar.demo.domain.base.usecase.BaseUseCase
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class DeleteEventUseCase @Inject constructor(
    private val repository: CalendarRepository,
) : BaseUseCase<Long, Result<Unit>>() {
    override suspend fun execute(parameters: Long): Result<Unit> = useResultWrapper {
        repository.deleteEvent(parameters)
    }
}
