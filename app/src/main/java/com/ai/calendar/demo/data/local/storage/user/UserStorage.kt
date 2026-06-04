package com.ai.calendar.demo.data.local.storage.user

import kotlinx.coroutines.flow.Flow

interface UserStorage {
    val accessTokenFlow: Flow<String?>
    suspend fun setAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun setUserData(userData: UserData)
    suspend fun getUserData(): UserData?
    suspend fun clear()
}
