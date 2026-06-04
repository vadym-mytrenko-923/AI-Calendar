package com.ai.calendar.demo.di.calendar

import android.content.ContentResolver
import android.content.Context
import com.ai.calendar.demo.data.features.calendar.CalendarRepositoryImpl
import com.ai.calendar.demo.data.features.calendar.local.CalendarLocalDataSource
import com.ai.calendar.demo.data.features.calendar.local.CalendarProviderDataSource
import com.ai.calendar.demo.domain.features.calendar.CalendarRepository
import com.ai.calendar.demo.ui.screens.calendar.utils.EditorDateFormatter
import com.ai.calendar.demo.ui.screens.calendar.utils.EventDateTimeFormatter
import com.ai.calendar.demo.ui.screens.calendar.utils.EventTimeFormatter
import com.ai.calendar.demo.ui.screens.calendar.utils.SelectedDayFormatter
import com.ai.calendar.demo.utils.date.DateFormatter
import com.ai.calendar.demo.utils.datetime.DateTimeFormatter
import com.ai.calendar.demo.utils.time.TimeFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

const val CALENDAR_SELECTED_DAY_FORMATTER = "calendarSelectedDayFormatter"
const val CALENDAR_EDITOR_DATE_FORMATTER = "calendarEditorDateFormatter"

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

    @Provides
    @Singleton
    @Named(CALENDAR_SELECTED_DAY_FORMATTER)
    fun provideSelectedDayFormatter(): DateFormatter = SelectedDayFormatter()

    @Provides
    @Singleton
    @Named(CALENDAR_EDITOR_DATE_FORMATTER)
    fun provideEditorDateFormatter(): DateFormatter = EditorDateFormatter()

    @Provides
    @Singleton
    fun provideTimeFormatter(): TimeFormatter = EventTimeFormatter()

    @Provides
    @Singleton
    fun provideEventDateTimeFormatter(): DateTimeFormatter = EventDateTimeFormatter()
}
