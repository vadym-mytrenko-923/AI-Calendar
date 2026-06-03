package com.agents.app.demo.data.local.storage.user

import com.agents.app.demo.data.features.auth.local.UserData
import kotlinx.coroutines.flow.Flow

interface UserStorage {
    val accessTokenFlow: Flow<String?>
    suspend fun setAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun setUserData(userData: UserData)
    suspend fun getUserData(): UserData?
    suspend fun clear()
}
