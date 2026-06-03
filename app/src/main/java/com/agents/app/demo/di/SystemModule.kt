package com.agents.app.demo.di

import com.agents.app.demo.data.common.logger.AppLogger
import com.agents.app.demo.domain.base.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SystemModule {
    @Provides
    @Singleton
    fun provideLogger(appLogger: AppLogger): Logger = appLogger
}
