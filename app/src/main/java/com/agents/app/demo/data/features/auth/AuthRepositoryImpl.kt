package com.agents.app.demo.data.features.auth

import com.agents.app.demo.data.features.auth.mapper.toDataModel
import com.agents.app.demo.data.features.auth.mapper.toRequestDto
import com.agents.app.demo.data.features.auth.remote.api.AuthApi
import com.agents.app.demo.data.local.storage.user.UserStorage
import com.agents.app.demo.domain.features.auth.AuthRepository
import com.agents.app.demo.domain.features.auth.model.LoginParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val userStorage: UserStorage
) : AuthRepository {
    override suspend fun makeLogin(params: LoginParams) {
        val result = api.login(params.toRequestDto())
        userStorage.setUserData(result.toDataModel())
        userStorage.setAccessToken(result.accessToken)
    }

    override suspend fun logout() {
        userStorage.clear()
    }

    override fun isUserLoggedInFlow(): Flow<Boolean> = userStorage.accessTokenFlow.map { !it.isNullOrEmpty() }
}
