package com.agents.app.demo.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.agents.app.demo.BuildConfig
import com.agents.app.demo.data.features.auth.remote.api.AuthApi
import com.agents.app.demo.data.features.products.remote.api.ProductsApi
import com.agents.app.demo.data.remote.interceptor.AuthInterceptor
import com.agents.app.demo.data.remote.interceptor.ConnectionTimeoutInterceptor
import com.agents.app.demo.data.remote.interceptor.CustomHttpLoggingInterceptor
import com.agents.app.demo.data.remote.interceptor.GzipDecodingInterceptor
import com.agents.app.demo.data.remote.util.HttpLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideProductsApi(retrofit: Retrofit): ProductsApi = retrofit.create(ProductsApi::class.java)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideHttpUrl(): HttpUrl = BuildConfig.BASE_URL.toHttpUrl()

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(customHttpLogger: HttpLogger): CustomHttpLoggingInterceptor {
        return CustomHttpLoggingInterceptor(customHttpLogger)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggerInterceptor: CustomHttpLoggingInterceptor,
        gzipDecodingInterceptor: GzipDecodingInterceptor,
        connectionTimeoutInterceptor: ConnectionTimeoutInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(gzipDecodingInterceptor)
            .addInterceptor(connectionTimeoutInterceptor)
            .addNetworkInterceptor(loggerInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        httpUrl: HttpUrl,
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(httpUrl)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}
