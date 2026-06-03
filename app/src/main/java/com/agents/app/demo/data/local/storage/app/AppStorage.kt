package com.agents.app.demo.data.local.storage.app

import kotlinx.coroutines.flow.Flow

interface AppStorage {
    val onboardingShownFlow: Flow<Boolean>
    suspend fun setOnboardingShown(shown: Boolean)
    suspend fun wasOnboardingShown(): Boolean
    suspend fun clear()
}
