package com.ai.calendar.demo.di.ai

import com.ai.calendar.demo.agent.llm.FirebaseLlmClient
import com.ai.calendar.demo.domain.features.ai.LlmClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AiModule {
    @Provides
    @Singleton
    fun provideLlmClient(impl: FirebaseLlmClient): LlmClient = impl
}
