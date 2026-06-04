package com.ai.calendar.demo.data.remote.util

import com.ai.calendar.demo.domain.base.logger.Logger
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

private const val NETWORK_TAG = "NetworkAPI"

@Singleton
class HttpLogger @Inject constructor(private val logger: Logger) : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        logger.log("$NETWORK_TAG: $message")
    }
}
