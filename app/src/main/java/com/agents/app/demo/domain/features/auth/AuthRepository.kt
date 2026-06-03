package com.agents.app.demo.domain.features.auth

import com.agents.app.demo.domain.features.auth.model.LoginParams
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun makeLogin(params: LoginParams)
    suspend fun logout()
    fun isUserLoggedInFlow(): Flow<Boolean>
}
