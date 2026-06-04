package com.agents.app.demo.di.calendar

import android.content.ContentResolver
import android.content.Context
import com.agents.app.demo.data.features.calendar.CalendarRepositoryImpl
import com.agents.app.demo.data.features.calendar.local.CalendarLocalDataSource
import com.agents.app.demo.data.features.calendar.local.CalendarProviderDataSource
import com.agents.app.demo.domain.features.calendar.CalendarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CalendarModule {

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    @Singleton
    fun provideCalendarLocalDataSource(impl: CalendarProviderDataSource): CalendarLocalDataSource = impl

    @Provides
    @Singleton
    fun provideCalendarRepository(impl: CalendarRepositoryImpl): CalendarRepository = impl
}
