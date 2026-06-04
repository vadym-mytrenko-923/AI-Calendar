package com.ai.calendar.demo.domain.base.usecase

import kotlinx.coroutines.flow.Flow

abstract class BaseNoParamsFlowUseCase<OUT> {
    operator fun invoke(): Flow<OUT> = execute()

    protected abstract fun execute(): Flow<OUT>
}
