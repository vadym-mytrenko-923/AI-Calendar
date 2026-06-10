package com.ai.calendar.demo.di.ai

import com.ai.calendar.demo.agent.base.LlmAgentTool
import com.ai.calendar.demo.agent.client.LlamatikLlmClient
import com.ai.calendar.demo.agent.tool.CreateEventTool
import com.ai.calendar.demo.agent.tool.FindNearestEventTool
import com.ai.calendar.demo.agent.tool.ListEventsTool
import com.ai.calendar.demo.domain.features.ai.LlmClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AiModule {

    @Provides
    @Singleton
    fun provideLlmClient(impl: LlamatikLlmClient): LlmClient = impl

    @Provides
    @IntoSet
    fun provideListEventsTool(tool: ListEventsTool): LlmAgentTool = tool

    @Provides
    @IntoSet
    fun provideFindNearestEventTool(tool: FindNearestEventTool): LlmAgentTool = tool

    @Provides
    @IntoSet
    fun provideCreateEventTool(tool: CreateEventTool): LlmAgentTool = tool
}
