package com.agents.app.demo.data.local.storage.app

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.crypto.tink.Aead
import com.google.gson.Gson
import com.agents.app.demo.data.local.getFlowValue
import com.agents.app.demo.data.local.putValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val ONBOARDING_SHOWN = stringPreferencesKey("onboarding_shown")

class AppStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val encryption: Aead,
    private val gson: Gson,
) : AppStorage {
    override val onboardingShownFlow: Flow<Boolean> = dataStore.getFlowValue(ONBOARDING_SHOWN, encryption).map {
        it.toBoolean()
    }

    override suspend fun setOnboardingShown(shown: Boolean) {
        dataStore.putValue(ONBOARDING_SHOWN, encryption, shown.toString())
    }

    override suspend fun wasOnboardingShown(): Boolean = onboardingShownFlow.firstOrNull() ?: false

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
