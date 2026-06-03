package com.agents.app.demo.domain.features.auth.usecase

import com.agents.app.demo.domain.base.result.useResultWrapper
import com.agents.app.demo.domain.base.usecase.BaseUseCase
import com.agents.app.demo.domain.features.auth.AuthRepository
import com.agents.app.demo.domain.features.auth.model.LoginParams
import dagger.Reusable
import javax.inject.Inject

@Reusable
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<LoginParams, Result<Unit>>() {
    override suspend fun execute(parameters: LoginParams): Result<Unit> = useResultWrapper {
        authRepository.makeLogin(params = parameters)
    }
}
