package com.agents.app.demo.data.local.storage.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.agents.app.demo.data.local.getFlowValue
import com.agents.app.demo.data.local.getJsonValue
import com.agents.app.demo.data.local.putJsonValue
import com.agents.app.demo.data.local.putValue
import com.google.crypto.tink.Aead
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

private val ACCESS_TOKEN = stringPreferencesKey("access_token")
private val USER_DATA = stringPreferencesKey("user_data")

class UserStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val encryption: Aead,
    private val gson: Gson,
) : UserStorage {
    override val accessTokenFlow: Flow<String?> = dataStore.getFlowValue(ACCESS_TOKEN, encryption)

    override suspend fun setAccessToken(token: String) {
        dataStore.putValue(ACCESS_TOKEN, encryption, token)
    }

    override suspend fun getAccessToken(): String? = accessTokenFlow.firstOrNull()

    override suspend fun setUserData(userData: UserData) {
        dataStore.putJsonValue(USER_DATA, encryption, gson, userData)
    }

    override suspend fun getUserData(): UserData? {
        return dataStore.getJsonValue(USER_DATA, encryption, gson, UserData::class.java)
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
