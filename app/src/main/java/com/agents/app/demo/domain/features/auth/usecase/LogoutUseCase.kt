package com.agents.app.demo.domain.features.auth.usecase

import com.agents.app.demo.domain.base.result.useResultWrapper
import com.agents.app.demo.domain.base.usecase.BaseNoParamsUseCase
import com.agents.app.demo.domain.features.auth.AuthRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseNoParamsUseCase<Result<Unit>>() {
    override suspend fun execute(): Result<Unit> = useResultWrapper {
        authRepository.logout()
    }
}
