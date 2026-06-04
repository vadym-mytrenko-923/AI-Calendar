package com.ai.calendar.demo.data.remote.interceptor

import com.ai.calendar.demo.BuildConfig
import com.ai.calendar.demo.data.remote.util.HttpLogger
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomHttpLoggingInterceptor @Inject constructor(
    private val customHttpLogger: HttpLogger
) : Interceptor {

    private val bodyLvlInterceptor by lazy {
        HttpLoggingInterceptor(customHttpLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val headersLvlInterceptor by lazy {
        HttpLoggingInterceptor(customHttpLogger).apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val request = chain.request()
        return@runBlocking if (request.url.toUrl().path.shouldAvoidBodyLogging()) {
            headersLvlInterceptor.intercept(chain)
        } else {
            bodyLvlInterceptor.intercept(chain)
        }
    }

    private fun String.shouldAvoidBodyLogging(): Boolean {
        return !BuildConfig.DEBUG
    }
}
