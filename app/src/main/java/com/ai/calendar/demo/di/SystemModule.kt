package com.ai.calendar.demo.di

import com.ai.calendar.demo.data.common.logger.AppLogger
import com.ai.calendar.demo.domain.base.logger.Logger
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
