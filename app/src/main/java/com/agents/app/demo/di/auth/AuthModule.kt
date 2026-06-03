package com.agents.app.demo.di.auth

import com.agents.app.demo.data.features.auth.AuthRepositoryImpl
import com.agents.app.demo.domain.features.auth.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository = repositoryImpl
}
