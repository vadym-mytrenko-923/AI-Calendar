package com.agents.app.demo.domain.features.auth.usecase

import com.agents.app.demo.domain.base.usecase.BaseNoParamsFlowUseCase
import com.agents.app.demo.domain.features.auth.AuthRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class IsUserLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseNoParamsFlowUseCase<Boolean>() {
    override fun execute(): Flow<Boolean> = authRepository.isUserLoggedInFlow()
}
